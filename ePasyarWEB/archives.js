
  import { initializeApp } from "https://www.gstatic.com/firebasejs/9.17.1/firebase-app.js";
  import { getFirestore, getDocs, getDoc, setDoc, doc, collection, updateDoc } from "https://www.gstatic.com/firebasejs/9.17.1/firebase-firestore.js";
  
  
  
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

  let bck = document.getElementById("bck");
  bck.addEventListener('click', () => {
      window.location = 'events.html'
  })
  let event = document.getElementById("event");
  event.addEventListener('click', () => {
      window.location = 'events.html'
  })
  // FINAL
  var tbody = document.getElementById('tbody1');
  
  const querySnapshot = await getDocs(collection(db, "events"));
    querySnapshot.forEach(doc => {
  
      if(doc.data().user_Status == "Inactive"){
        var trow = document.createElement('tr'); 
        let td1 = document.createElement('td');
        let td2 = document.createElement('td');
        let td3 = document.createElement('td'); 
        let td4 = document.createElement('td'); 
        let td5 = document.createElement('td');
        let td6 = document.createElement('td');
  
        td1.innerHTML = doc.data().user_DeletedBy;
        td2.innerHTML = doc.data().user_LN + " " + doc.data().user_FN + " " + doc.data().user_MN;
        td3.innerHTML = doc.data().user_Type;
        td4.innerHTML = doc.data().user_EID;
        td5.innerHTML = doc.data().user_E;
        td6.innerHTML = doc.data().user_DeletedDate;
        
  
        trow.appendChild(td1);
        trow.appendChild(td2);
        trow.appendChild(td3);
        trow.appendChild(td4);
        trow.appendChild(td5);
        trow.appendChild(td6);
  
  
        tbody.appendChild(trow);
  
  
        trow.addEventListener('click', (e) =>{
  
          localStorage.setItem('ID', doc.id)
          //console.log(doc.id)
  
          //HIGHLIGHT TABLE ROW WHEN CLICKED - FINAL
          var table = document.getElementById("table");
          var rows = document.getElementsByTagName('tr');
          for(i = 1; i < rows.length; i++){
            var currentRow = table.rows[i];
            currentRow.onclick = function(){
              Array.from(this.parentElement.children).forEach(function(el){
                el.classList.remove('selected-row');
                
              });
  
              // [...this.parentElement.children].forEach((el) => el.classList.remove('selected-row'));
              this.classList.add('selected-row');
  
                  document.getElementById("enabled").disabled = false;
  
                  
          }
          
        }
      });
  
      }
    });

   
      
  
  // window.onload = GetAllDataOnce;
  
  document.getElementById("enabled").disabled = true; 
  
  enabled.addEventListener('click', () => {
      document.getElementById('cnfrm_modal_enable').style.visibility = "visible";
  });
  cnl.addEventListener('click', (e) => {
      document.getElementById('cnfrm_modal_enable').style.visibility = "hidden";
    });
  
  const querySnapshot2 = await getDocs(collection(db,"events"));
  querySnapshot2.forEach(doc2 => {   
  
        cnfrm.addEventListener('click', (e) => {
        const updateStat = doc(db, "events", doc2.id)
        var userID = localStorage.getItem("ID")
  
        if(userID == doc2.id){
          updateDoc(updateStat, {
              user_Status: "Active",
              user_DeletedBy: "",
              user_DeletedDate: ""
          }).then(() => {
            window.location = "archives.html"
          })
        }
  
        });
  
      }); 
  