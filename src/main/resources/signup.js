var registerBtn = document.getElementById("register");
var idwarning = document.getElementById("idDuplicate");
var pwwarning = document.getElementById("passwordError");
var mtwarning = document.getElementById("emptyError");

$("#signupId").select();

registerBtn.onclick = function(){
	register();
}

function register(){
	var id = safe($("#signupId").val());
	var email = safe($("#signupEmail").val());
	var pw1 = safe($("#signupPw1").val());
	var pw2 = safe($("#signupPw2").val());

	mtwarning.style.display = "none";
	pwwarning.style.display = "none";
	idwarning.style.display = "none";
	$("#emailError").hide();
	
	if(id != "" && email != "" && pw1 != "" && pw2 != ""){
		if(email.includes("@") && email.split("@")[1].includes(".")){
			if(pw1 == pw2){
				var newUserRef = database.ref("user/accounts/"+id);
				var currentdate = new Date();
				var datetime = currentdate.getDate() + "/"
	            				+ (currentdate.getMonth()+1)  + "/" 
				                + currentdate.getFullYear() + " @ "  
				                + currentdate.getHours() + ":"  
				                + currentdate.getMinutes() + ":" 
				                + currentdate.getSeconds();
				database.ref("user/accounts/"+id).once('value', function(snapshot){
					if(snapshot.val() == undefined){
						newUserRef.set({
							contributions: 0,
							score: 0,
							profilePic: "",
						});
						database.ref("user/accounts/"+id+"/%%%"+pw1).set({
							email: email,
							registeredDate: datetime,
						})
						localStorage.setItem("id", id);
						alert("<Melodize>\n\nWelcome "+id+"!\nYou are successfully registered.\n");
						window.location.href = "./songInfo.html";
					}
					else{
						$("#signupId").select();
						idwarning.style.display = "";
					}
				});
			}
			else{
				$("#signupPw2").select();
				pwwarning.style.display = "";
			}
		}else{
			$("#emailError").show();
			$("#signupEmail").select();
		}
	}
	else{
		mtwarning.style.display = "";
	}
};