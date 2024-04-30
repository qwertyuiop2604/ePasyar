import { initializeApp } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-app.js";
import { getFirestore } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-firestore.js"
import { getAuth } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-auth.js"


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

var form = document.getElementById('register-form')

form.addEventListener('submit', function(event) {
  event.preventDefault();
  var email = document.getElementById('email').value;
  var password = document.getElementById('password').value;
  var name = document.getElementById('name').value;
  var country = document.getElementById('country').value;
  
  if (email === ''){
    console.log("email is empty")
  } else if (password === ''){
    console.log("password is empty")
  } else if (name === ''){
    console.log("name is empty")
  } else if (country === ''){
    console.log("country is empty")
  } else {
     querySnapshot.forEach(doc => {
      if(doc.data().email === email && doc.data().password === password){
        window.location = "index.html";
      }
     });
  } 
});

      // document.querySelector('.login-container').style.display = 'none';
      // document.querySelector('.home-container').style.display = 'block';
      // document.getElementById('email-display').textContent = email;

      
 // }
//);

//function registrationAccount() {
  
  // window.location.href = 'index.html';
//}
