document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    
    if (email === 'home' && password === 'password') {
        document.getElementById('login-form').reset();
        document.querySelector('.login-container').style.display = 'none';
        document.querySelector('.home-container').style.display = 'block';
        document.getElementById('email-display').textContent = email;
    } else {
        alert('Invalid email or password');
    }
});

function loginform(e) {
  e.preventDefault();
  var email = getElementVal('email');
  var password = getElementVal('password');
  
  console.log(email,password);
  }

  

document.getElementById('logout-btn').addEventListener('click', function() {
    document.querySelector('.home-container').style.display = 'none';
    document.querySelector('.login-container').style.display = 'block';
});

document.addEventListener('DOMContentLoaded', function() {
    const signupForm = document.getElementById('signup-form');
    
    signupForm.addEventListener('submit', function(event) {
        event.preventDefault(); 
        
        
        const email = document.getElementById('email').value;
        const name = document.getElementById('name').value;
        const country = document.getElementById('country').value;
        const password = document.getElementById('password').value;
        const confirm = document.getElementById('confirm').value;
        
        
        if (password !== confirm) {
            alert("Passwords do not match");
            return;
        }
        
        
        
       
        const newUser = {
            email: email,
            name: name,
            country: country,
            password: password
        };
        
       
        signupForm.reset();
        
        
        alert("Account created successfully!");
    });
})

function addRowToUserTable(name, email, country) {
    const table = document.getElementById("user-table");
    const row = table.insertRow(-1); 
    const nameCell = row.insertCell(0);
    const emailCell = row.insertCell(1);
    const countryCell = row.insertCell(2);

    nameCell.innerHTML = name;
    emailCell.innerHTML = email;
    countryCell.innerHTML = country;
}


document.getElementById("login-form").addEventListener("submit", function(event) {
    event.preventDefault();
    
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    
    if (email && password) {
        document.querySelector(".dashboard-container").style.display = "block";
        document.querySelector(".login-container").style.display = "none";
        document.getElementById("dashboard-email").textContent = email;
    } else {
        alert("Invalid email or password. Please try again.");
    }
});
function editTourist(touristId) {
    alert("Editing tourist with ID: " + touristId);
  }
  
  function searchFunction() {
    var searchValue = document.getElementById("search-bar").value;
    alert("Searching for: " + searchValue);
  }
  document.getElementById('createAccountForm').addEventListener('submit', function(event) {
    // Prevent the form from submitting normally
    event.preventDefault();
  
    // Perform your account creation logic here
    // For demonstration purposes, let's assume the account creation is successful
  
    // Redirect to the dashboard
    window.location.href = 'dashboard.html';
  });

  function rlogin() {
    
    window.location.href ='dashboard/dash.html';
  }
  

document.addEventListener('DOMContentLoaded', function() {
    var registerButton = document.getElementById('register-button');
    registerButton.addEventListener('click', function() {
      window.location.href = 'registration/register.html';
    });
  });

  document.addEventListener('DOMContentLoaded', function() {
    var registerButton = document.getElementById('btnLogin');
    registerButton.addEventListener('submit', function() {
      window.location.href = 'dash.html';
    });
  });

  function registerAccount() {
    // dito nlng logic mo sa pagadd ng account
    // if (condition)
    window.location.href = '/dashboard/dash.html';
    // else false
}