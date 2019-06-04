var projectRef = database.ref("projects");
var totalSongs = 0;
var songList = [];
var songList_initialThreshold = 10;
var songList_showCount = songList_initialThreshold;
var songList_window = 10;
var koreanText =   [["li:nth-child(5) > a","홈"],
					["li:nth-child(4) > a","갤러리"],
					["li:nth-child(3) > a","프로필"],
					[".section-home:nth-child(1) > h1", "Melodize에 환영합니다"],
					[".section-home:nth-child(2) > div:nth-child(1)","<h1>시작하기</h1><br>직접 작사한 가사를 공유하세요<br><br>"+
																	"자신이 정한 테마, 분위기에 따라<br>실시간으로 멜로디가 완성되는 것을 확인하세요<br><br>"+
																	"<button class='green-btn' id='requesterBtn'>가사 내기</button>"],
					[".section-home:nth-child(3) > div:nth-child(2)","<h1>표현하기</h1><br>제시된 가사들 중에 마음에 드는 것을 고르고<br><br>"+
																	"자신이 생각하는 멜로디를 만드세요<br><br>"+
																	"<button class='green-btn' id='requesterBtn'>작곡 하기</button>"],
					[".section-home:nth-child(3) > div > h1", "표현하기"],
					[".section-home:nth-child(4) > div > h1", "둘러보기"],
					[".section-home:nth-child(5) > div > h1", "더 알아보기"],];

$("#requesterBtn").on('click', function() {
	logged_in = localStorage.getItem("id");
	if(logged_in)
		window.location.href = "./request.html";
	else{
		$("#id").val("");
		$("#pw").val("");
	   	$('#warning_msg').hide();
	    $('#loginModal').show();
	    $("#id").select();
	    pageAfterLogin = "./request.html";
	}
})

$("#workerBtn").on('click', function() {
	window.location.href = "./songInfo.html";
})

$("#listBtn").on('click', function() {
	window.location.href = "./songInfo.html";
})

$("#learnBtn").on('click', function() {
	//$('html, body').animate({ scrollTop: $('#learnStart').offset().top }, 'slow');
	window.location.href = "./about.html";
})

function init(){
	$("#songList-showMore").hide();
}

init();