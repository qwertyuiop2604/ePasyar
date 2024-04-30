
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-app.js";
import { getFirestore, getDocs, collection } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-firestore.js"
import { getAuth, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-auth.js"


const firebaseConfig = {
  apiKey: "AIzaSyA6U1In2wlItYioP3yl43C3hCgiXUZ4oKI",
  authDomain: "epasyar-aa569.firebaseapp.com",
  databaseURL: "https://epasyar-aa569-default-rtdb.firebaseio.com",
  projectId: "epasyar-aa569",
  storageBucket: "epasyar-aa569.appspot.com",
  messagingSenderId: "1004550371893",
  appId: "1:1004550371893:web:692e667675470640980f7c"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);
const auth = getAuth(app);

const querySnapshot = await getDocs(collection(db,"users","admin","admin_account"));

var form = document.getElementById('login-form')

form.addEventListener('submit', function(event) {
  event.preventDefault();
  var email = document.getElementById('email').value;
  var password = document.getElementById('password').value;
  
  if (email === ''){
    console.log("email is empty")
 }
  else if (password === ''){
    console.log("password is empty")
  }
  else {
     querySnapshot.forEach(doc => {
      if(doc.data().email === email && doc.data().password === password){
        window.location = "dash.html"
        
    }
     });

  } 
      // document.querySelector('.login-container').style.display = 'none';
      // document.querySelector('.home-container').style.display = 'block';
      // document.getElementById('email-display').textContent = email;

      
  }
);



//document.addEventListener('DOMContentLoaded', function() {

  //const signupForm = document.getElementById('signup-form');
//})
  
  
//   signupForm.addEventListener('submit', function(event) {
//       event.preventDefault(); 
      
      
//       const email = document.getElementById('email').value;
//       const name = document.getElementById('name').value;
//       const country = document.getElementById('country').value;
//       const password = document.getElementById('password').value;
//       const confirm = document.getElementById('confirm').value;
      
      
//       if (password !== confirm) {
//           alert("Passwords do not match");
//           return;
//       }
      
      
      
     
//       const newUser = {
//           email: email,
//           name: name,
//           country: country,
//           password: password
//       };
      
     
//       signupForm.reset();
      
      
//       alert("Account created successfully!");
//   });
// })

// function addRowToUserTable(name, email, country) {
//   const table = document.getElementById("user-table");
//   const row = table.insertRow(-1); 
//   const nameCell = row.insertCell(0);
//   const emailCell = row.insertCell(1);
//   const countryCell = row.insertCell(2);

//   nameCell.innerHTML = name;
//   emailCell.innerHTML = email;
//   countryCell.innerHTML = country;
// }




// document.getElementById('createAccountForm').addEventListener('submit', function(event) {
  
//   event.preventDefault();

 
//   window.location.href = 'dashboard.html';
// });

// function rlogin() {
  
//   window.location.href ='dashboard/dash.html';
// }


// document.addEventListener('DOMContentLoaded', function() {
//   var registerButton = document.getElementById('register-button');
//   registerButton.addEventListener('click', function() {
//     window.location.href = 'registration/register.html';
//   });
// });

// document.addEventListener('DOMContentLoaded', function() {
//   var registerButton = document.getElementById('btnLogin');
//   registerButton.addEventListener('submit', function() {
//     window.location.href = 'dash.html';
//   });
// });

// function registerAccount() {
//   // dito nlng logic mo sa pagadd ng account
//   // if (condition)
//   window.location.href = 'dash.html';
//   // else false
// }
// function redirectToDash() {
// var email = document.getElementById('email').value;
// var password = document.getElementById('password').value;

// if (email.trim() !== '' && password.trim() !== '') {
//     window.location.href = 'dash.html?email=' + email + '&password=' + password;
// } else {
//     alert('Please enter both email and password.');
// }
// }

    // function redirectToForgotPassword() {
    //     window.location.href = 'forgot_password.html';
    // }


    // function hideForgotPasswordForm() {
    //     document.getElementById('forgot-password-form').style.display = 'none';
    //     document.getElementById('login-form').style.display = 'block';
    // }

    // document.getElementById("forgot").addEventListener("click", function() {
    //     window.location.href = "forgot_password.html";
    // });
