    package com.example.epasyaaar;

    import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.util.Patterns;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
    import com.google.firebase.auth.FirebaseAuthUserCollisionException;
    import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.Arrays;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class Register extends AppCompatActivity {

        TextInputEditText editTextFname, editTextLname, editTextEmail, editTextCountry, editTextPass, editTextConfPass;
        Button buttonReg;
        FirebaseAuth mAuth;

        FirebaseFirestore fStore;
        String userID;

        int code;
        private AutoCompleteTextView autoCompleteTextViewSearch;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            mAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

            editTextFname = findViewById(R.id.txtbox_Fname);
            editTextLname = findViewById(R.id.txtbox_Lname);
            editTextEmail = findViewById(R.id.txtbox_email);
            autoCompleteTextViewSearch = findViewById(R.id.autoCompleteTextView_search);
            editTextPass = findViewById(R.id.txtbox_pass);
            editTextConfPass = findViewById(R.id.txtbox_confpass);
            buttonReg = findViewById(R.id.btnRegister);

            String[] itemsArray = getResources().getStringArray(R.array.countries_names);
            List<String> items = Arrays.asList(itemsArray);

            CustomAdapter adapter = new CustomAdapter(this, items);
            autoCompleteTextViewSearch.setAdapter(adapter);

            autoCompleteTextViewSearch.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


            buttonReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Fname, Lname, email, country, pass, confpass;

                    Fname = String.valueOf(editTextFname.getText());
                    Lname = String.valueOf(editTextLname.getText());
                    email = String.valueOf(editTextEmail.getText());
                    country = autoCompleteTextViewSearch.getText().toString();
                    pass = String.valueOf(editTextPass.getText());
                    confpass = String.valueOf(editTextConfPass.getText());

                    // Regular expression to match only letters (no numbers or symbols)
                    String regex = "^[a-zA-Z]+$";

                    if (TextUtils.isEmpty(Fname)) {
                        editTextFname.setError("First Name is required");
                        editTextFname.requestFocus();
                        return;
                    } else if (!Fname.matches(regex)) {
                        editTextFname.setError("First Name should contain only letters");
                        editTextFname.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(Lname)) {
                        editTextLname.setError("Last Name is required");
                        editTextLname.requestFocus();
                        return;
                    } else if (!Lname.matches(regex)) {
                        editTextLname.setError("Last Name should contain only letters");
                        editTextLname.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(email)) {
                        editTextEmail.setError("Email is required");
                        editTextEmail.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(country)) {
                        editTextCountry.setError("Select your country");
                        autoCompleteTextViewSearch.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(pass)) {
                        editTextPass.setError("Password is required");
                        editTextPass.requestFocus();
                        return;
                    } else if (pass.length() < 6) {
                        editTextPass.setError("Password should be at least 6 digits");
                        editTextPass.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(confpass)) {
                        editTextConfPass.setError("Confirmation of your is required");
                        editTextConfPass.requestFocus();
                        return;
                    } else if (!pass.equals(confpass)) {
                        editTextPass.setError("Password must be the same");
                        editTextPass.requestFocus();
                        editTextConfPass.setError("Password must be the same");
                        editTextConfPass.requestFocus();
                        editTextPass.setText("");
                        editTextConfPass.setText("");
                        return;
                    }

                    registerUser(Fname, Lname, email, country, pass, confpass);
                }
            });

        }

        private void registerUser(String fname, String lname, String email, String country, String pass, String confpass) {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            userID = currentUser.getUid(); // Assign userID here

                            Toast.makeText(Register.this, "Account Created. Verification email sent.", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fname);
                            user.put("lName", lname);
                            user.put("email", email);
                            user.put("country", country);
                            user.put("password", pass);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                }
                            });

                            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> emailTask) {
                                    if (emailTask.isSuccessful()) {
                                        // Email verification sent successfully
                                        startActivity(new Intent(Register.this, Login.class));
                                        Intent intent = new Intent(Register.this, Login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); // Close Register Activity
                                    } else {
                                        // Email verification not sent
                                        Toast.makeText(Register.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Register", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Failed to create account",
                                //+ task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            editTextPass.setError("Password is too weak. Add numbers, Capitalized words, and special characters ");
                            editTextPass.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            editTextEmail.setError("Your email is invalid");
                            editTextEmail.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            editTextEmail.setError("This email is already registered");
                            editTextEmail.requestFocus();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
