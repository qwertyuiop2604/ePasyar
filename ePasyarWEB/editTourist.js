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

let profile = document.getElementById('profile')

profile.addEventListener('click', () =>{
  window.location = 'profile.html'
})

let events = document.getElementById('events')

events.addEventListener('click', () =>{
  window.location = 'events.html'
})

let dashboard = document.getElementById('dashboard')

dashboard.addEventListener('click', () =>{
  window.location = 'events.html'
})

let reviews = document.getElementById('reviews')

reviews.addEventListener('click', () =>{
  window.location = 'reviews.html'
})