//(function(){
var currentIndex = localStorage.getItem("currentIndex");
var FuddingBtn = document.getElementById("FuddingBtn");
FuddingBtn.addEventListener("click",function(){
	document.querySelector("#recipePage").style.display = "none";
	document.querySelector("#addSectionchangerPage").style.display = "block";
	
	
});
