var songs = [];
var projectRef = database.ref("projects");
var completedNum = 0;
var leaderboard = [];
var userNum = 0;

var userCount;
var showUserCount;
var userWindow = 15;

function addSong(title, participants, album, key){
	var song = '<div class="inBox">'+
	      			'<div class="imgFrame">'+
	        			'<img onerror="this.src =`./img/default-cover-art.png`" src="'+album+'">'+
	      			'</div>'+
	      			'<div style="font-size: 18px">'+title+'</div>'+
	      			'<div style="color: #818181">Participants: <statnum>'+participants+'</statnum></div>'+
	      			'<button id="checkBtn" class="btn btn-default" style="margin: 12px; width: 150px" key="'+key+'"">Check</button> '+
    			'</div>';

	$("#board").append(song);
	console.log("added");
}

projectRef.on('child_added', function(snapshot){
	var key = snapshot.key;
	var value = snapshot.val();
	if(value.completeNote == value.length){
		var title = safe(value.title);
		var participants = safe(value.participants);
		var album = value.album;
		addSong(title, participants, album, key);
		completedNum++;
		$("#completed").html(completedNum);
	}
});

$(document).on('click', '#checkBtn', function(){
	var key = this.getAttribute("key");
	localStorage.setItem("melodize-cur-key", key);
	window.location.href = "./checkSong.html";
});

function showMoreUser(){
	var old_showCount = showUserCount;
	showUserCount += userWindow;
	if(showUserCount > userCount){
		showUserCount = userCount;
		$("#showMore").hide();
	}
	for(var i = old_showCount; i < showUserCount; i++){
		showUser(i);
	}
}

function showUser(index){
	var profile = leaderboard[index].profile;
	var id = safe(leaderboard[index].id);
	var score = safe(leaderboard[index].score);

	var code = '<tr><td>'+(index+1)+'</td>'+
				'<td><img class="profile-img" src="'+profile+'" onerror="this.src = `./img/default-avatar.jpg`">'+
	           '<div style="padding-top: 15px"><idLink>'+id+'</idLink></div></td>'+
	           '<td>'+score+'</td></tr>';
	$("#leaderboard").append(code);
}


$("#showMore").on('click', function(){
	showMoreUser();
	console.log("show more");
})

function init(){
	var userRef = database.ref("user/accounts");
	var projectRef = database.ref("projects");
	showUserCount = 0;
	userCount = 0;
	userRef.once("value", function(snapshot){
		userNum = snapshot.numChildren();
		$("#totalUsers").html(userNum);
	}).then(function(){
		var cnt = 0;
		userRef.on("child_added", function(snapshot){
			leaderboard.push({
				id: safe(snapshot.key),
				score: safe(snapshot.val().score),
				profile: snapshot.val().profilePic,
			});
			leaderboard.sort(function(a, b){return b.score-a.score});
			userCount++;
			if(userCount == userNum){
				userRef.off("child_added");
				showMoreUser();
			}
		});
	});
	projectRef.once("value", function(snapshot){
		$("#requests").html(snapshot.numChildren());
	}).then(function(){
		document.getElementById("loader").style.display = "none";
  		document.getElementById("mainDiv").style.display = "block";
  		console.log("loaded");
  	});
}

init();