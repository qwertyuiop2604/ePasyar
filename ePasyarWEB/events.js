
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-app.js";
import { getFirestore, addDoc, doc, collection, setDoc, getDocs } from "https://www.gstatic.com/firebasejs/9.1.1/firebase-firestore.js"


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


let dash = document.getElementById("dash");
dash.addEventListener('click', () =>{
  window.location = 'dash.html'
})
let profile = document.getElementById("profile");
profile.addEventListener('click', () =>{
  window.location = 'profile.html'
})
let notifs = document.getElementById("notifs");
notifs.addEventListener('click', () =>{
  window.location = 'notifs.html'
})

// CREATE FORM POPUP
const createAcc = document.getElementById('user-create');
const openPop = document.querySelector('.add_acc');
const closePop = document.querySelector('.close-modal');

openPop.addEventListener('click', () => {
  createAcc.style.display = 'block';
});
closePop.addEventListener('click', () => {
  createAcc.style.display = 'none';
});

// FOR REGISTER FORM - ADD TO FIREBASE
const formCreate = document.getElementById('create-form');
const name = document.getElementById('name');
const date = document.getElementById('date');
const description = document.getElementById('description');

formCreate.addEventListener('submit', (e) => {
  e.preventDefault();
  if (name.value == '') {
    alert("ENTER EVENT NAME")
  } else if (date.value == '') {
    alert("ENTER EVENT DATE")
  } else if (description.value == '') {
    alert("ENTER EVENT DESCRIPTION")
  } else {
    addDoc(collection(db, "festivals"), {
      Name: name.value,
      Date: date.value,
      Description: description.value, // Use description.value for textarea
      Status: "not done"
    }).then(() => {
      createAcc.style.display = 'none';
    }).catch((error) => {
      console.error("Error adding document: ", error);
    });
  }
});

// FOR EDIT MODAL CONFIRMATION - FINAL
const confirmation = document.getElementById('cnfrm_edit')
const cancel = document.querySelector('.cnl')
const confirm = document.querySelector('.cnfrm')

cancel.addEventListener('click', () => {
    confirmation.style.display = 'none'
    modalEdit.style.display = 'block';
    confirm.style.display = 'none';
});



//Edit FORM POPUP
const editAcc = document.getElementById('user-edit');
const oPop = document.querySelector('.edit_acc');
const cPop = document.querySelector('.close-modal-edit');

oPop.addEventListener('click', () => {
  editAcc.style.display = 'block';
});
cPop.addEventListener('click', () => {
  editAcc.style.display = 'none';
});

// FOR EDIT FORM - UPDATE TO FIREBASE
const formEdit = document.getElementById('edit-form');
const name1 = document.getElementById('name');
const date1 = document.getElementById('date');
const description1 = document.getElementById('description');


formEdit.addEventListener('submit', (e) => {
  e.preventDefault();
  if (name1.value == '') {
    alert("ENTER EVENT NAME")
  } else if (date1.value == '') {
    alert("ENTER EVENT DATE")
  } else if (description1.value == '') {
alert("ENTER EVENT DESCRIPTION")
  } else {
    setDoc(doc(db, "events"), {
      name: name1.value,
      date: date1.value,
      description: description1.value
      
    }).then(() => {
      editAcc.style.display = 'none';
    }).catch((error) => {
      console.error("Error updating document: ", error);
    });
  }
});

// FINAL
var tbody = document.getElementById('tbody1');

const querySnapshot = await getDocs(collection(db, "festivals"));
  querySnapshot.forEach(doc => {

  if (doc.data().Status == "not done") {
    var trow = document.createElement('tr');
    let td1 = document.createElement('td');
    let td2 = document.createElement('td');
    let td3 = document.createElement('td');

    td1.innerHTML = doc.data().Name;
    td2.innerHTML = doc.data().Date;
    td3.innerHTML = doc.data().Description;

    trow.appendChild(td1);
    trow.appendChild(td2);
    trow.appendChild(td3);

    tbody.appendChild(trow);
  }
})

// Event Listener for delete account button - FINAL
delete_acc.addEventListener('click', () => {
  document.getElementById('delete_acc_modal').style.visibility = "visible";
});
cnl2.addEventListener('click', (e) => {
  document.getElementById('delete_acc_modal').style.visibility = "hidden";
  window.location = "events.html"
});

//Button to see archived accounts
archived_acc.addEventListener('click', (e) => {
  window.location = "archives.html"
})


    
    
