package com.example.epasyaaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button login, noAcc;
    TextView forgpass;
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.txtbox_email);
        editTextPassword = findViewById(R.id.txtbox_password);

        login = findViewById(R.id.btnLogin);
        noAcc = findViewById(R.id.btnNoacc);
        forgpass = findViewById(R.id.txtforgpassword);

        login.setOnClickListener(this);
        noAcc.setOnClickListener(this);
        forgpass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnLogin) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!password.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null && user.isEmailVerified()) {
                                            // User is logged in and email is verified
                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Login.this, Dashboard.class));
                                        } else {
                                            // Email is not verified, prompt user to verify
                                            Toast.makeText(Login.this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            editTextPassword.setError("Incorrect Password");
                                            editTextPassword.requestFocus();
                                            editTextPassword.setText("");
                                            Toast.makeText(Login.this, "Please check if your email or password is correct", Toast.LENGTH_LONG).show();

                                        }
                                        // Authentication failed
                                        //Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    editTextPassword.setError("Password cannot be empty");
                    editTextPassword.requestFocus();
                }
            } else if (email.isEmpty()) {
                editTextEmail.setError("Please enter your Email");
                editTextEmail.requestFocus();
            } else {
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
            }


        } else if (viewId == R.id.btnNoacc) {
            startActivity(new Intent(Login.this, Register.class));
        } else if (viewId == R.id.txtforgpassword) {
            startActivity(new Intent(Login.this, ForgotPass.class));
        }
    }
}
