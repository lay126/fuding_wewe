/*    
 * Copyright (c) 2014 Samsung Electronics Co., Ltd.   
 * All rights reserved.   
 *   
 * Redistribution and use in source and binary forms, with or without   
 * modification, are permitted provided that the following conditions are   
 * met:   
 *   
 *     * Redistributions of source code must retain the above copyright   
 *        notice, this list of conditions and the following disclaimer.  
 *     * Redistributions in binary form must reproduce the above  
 *       copyright notice, this list of conditions and the following disclaimer  
 *       in the documentation and/or other materials provided with the  
 *       distribution.  
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its  
 *       contributors may be used to endorse or promote products derived from  
 *       this software without specific prior written permission.  
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

var SAAgent = null;
var SASocket = null;
var CHANNELID = 104;
var ProviderAppName = "FudingAndroid";

var foodstr = null;
var datastr = null;
var foodcnt = 0;

window.indexedDB = window.indexedDB;

window.IDBTransaction = window.IDBTransaction || window.webkitIDBTransaction
		|| window.msIDBTransaction;
window.IDBKeyRange = window.IDBKeyRange || window.webkitIDBKeyRange
		|| window.msIDBKeyRange;

var page = document.getElementById("addSectionchangerPage"), changer = document
		.getElementById("addSectionchanger"), sectionWrap = document
		.getElementById("addSectionWrap"), removeBtn = document
		.getElementById("removeBtn"), testBtn = document
		.getElementById("testBtn"), fbtn = document.getElementById("fbutton"), sectionChanger, idx = 1;

function createHTML(log_string) {
	var log = document.getElementById('resultBoard');
	log.innerHTML = log.innerHTML + "<br> : " + log_string;
}

function onerror(err) {
	console.log("err [" + err + "]");
}

var agentCallback = {
	onconnect : function(socket) {
		SASocket = socket;
		alert("레시피 전송 준비 완료");
		// createHTML("startConnection");
		SASocket.setDataReceiveListener(onreceive); // set receiveListener
		SASocket.sendData(CHANNELID, ""); // send
		SASocket.setSocketStatusListener(function(reason) {
			console.log("Service connection lost, Reason : [" + reason + "]");
			disconnect();
		});
	},
	onerror : onerror
};

var peerAgentFindCallback = {
	onpeeragentfound : function(peerAgent) {
		try {
			if (peerAgent.appName == ProviderAppName) {
				SAAgent.setServiceConnectionListener(agentCallback);
				SAAgent.requestServiceConnection(peerAgent);
			} else {
				alert("Not expected app!! : " + peerAgent.appName);
			}
		} catch (err) {
			console
					.log("exception [" + err.name + "] msg[" + err.message
							+ "]");
		}
	},
	onerror : onerror
}

function onsuccess(agents) {
	try {
		if (agents.length > 0) {
			SAAgent = agents[0];

			SAAgent.setPeerAgentFindListener(peerAgentFindCallback);
			SAAgent.findPeerAgents();
		} else {
			alert("Not found SAAgent!!");
		}
	} catch (err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}

function connect() {
	if (SASocket) {
		alert('Already connected!');
		return false;
	}
	try {
		webapis.sa.requestSAAgent(onsuccess, function(err) {
			console.log("err [" + err.name + "] msg[" + err.message + "]");
		});
	} catch (err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}

function disconnect() {
	try {
		if (SASocket != null) {
			SASocket.close();
			SASocket = null;
			alert("closeConnection");
		}
	} catch (err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}

function addSection() {

	var str = localStorage.getItem("fudingobj"
			+ localStorage.getItem("fuddingCnt"));
	datastr = str.split(",", 5);

	var section = document.createElement("DIV");
	section.innerHTML = '<section style="text-align:center" ><h3>요리이름: '
			+ datastr[0] + '<br>재료: ' + datastr[1] + '<br>요리시간: ' + datastr[2]
			+ '<br>요리단계: ' + datastr[3] + '<br></h3></section>';

	sectionWrap.appendChild(section.firstChild);
	sectionChanger.refresh();
	sectionChanger.setActiveSection(sectionWrap.children.length - 1);

}

function onreceive(channelId, data) {
	// createHTML(data);

	foodstr = data.split(":", 5);
	if (localStorage.getItem("fuddingCnt") === null) {
		localStorage.setItem("fuddingCnt", 1);
		localStorage.setItem("foodName1", foodstr[0]);

		var fstr = [];
		for ( var i = 0; i < 5; i++)
			fstr[i] = foodstr[i];

		localStorage.setItem("fudingobj" + localStorage.getItem("fuddingCnt"),

		fstr.valueOf());
		alert(fstr.valueOf());

	} else {
		var fct = parseInt(localStorage.getItem("fuddingCnt"), 10) + 1;
		localStorage.setItem("fuddingCnt", fct);
		localStorage.setItem("foodName" + localStorage.getItem("fuddingCnt"),
				foodstr[0]);

		var fstr = [];
		for ( var i = 0; i < 5; i++)
			fstr[i] = foodstr[i];

		localStorage.setItem("fudingobj" + localStorage.getItem("fuddingCnt"),
				fstr.valueOf());

	}

	addSection();
}

function fetch() {
	try {
		SASocket.setDataReceiveListener(onreceive);
		SASocket.sendData(CHANNELID, "Hello Accessory!");
	} catch (err) {
		console.log("exception [" + err.name + "] msg[" + err.message + "]");
	}
}

(function() {
	// add eventListener for tizenhwkey
	document.addEventListener('tizenhwkey', function(e) {
		if (e.keyName == "back")
			tizen.application.getCurrentApplication().exit();
	});

	function removeSection() {

		localStorage.clear();

		var curSection = sectionChanger.getActiveSectionIndex();
		if (sectionWrap.children.length > 1) {
			sectionWrap.removeChild(sectionWrap.children[curSection]);
		}
		sectionChanger.refresh();

	}

	function testSection() {
		var curSection = sectionChanger.getActiveSectionIndex();
		localStorage.setItem("currentIndex", curSection);
//		alert("cursection=" + curSection);
//		document.querySelector("#addSectionchangerPage").style.display = "none";
//		document.querySelector("#recipePage").style.display = "block";
		var recipeContent = document.getElementById("recipe");
		var fuddingstr = localStorage.getItem("fudingobj" + curSection);
		var fuddingstr2 = fuddingstr.split(",", 5);
		var stepCnt = fuddingstr2[3];
		var step = fuddingstr2[4];
		var stepStr = step.split("단계", stepCnt);
		for (var i=0;i<stepCnt;i++)
		alert(stepStr[i]);
		var stepContent = createElement("DIV");
//		for ( var i = 0; i < stepCnt; i++) {
//			stepContent.innerHTML = '<h3>' + i + '단계<br>' + stepStr[i]
//					+ '</h3><br>'
//
//		}
//		recipeContent.appendChild(stepContent.firstChild)

	}

	page.addEventListener("pagebeforeshow", function() {
		// make SectionChanger object
		sectionChanger = new tau.SectionChanger(changer, {
			circular : false,
			orientation : "horizontal"
		});

		removeBtn.addEventListener("click", removeSection);
		testBtn.addEventListener("click", testSection);
		fbutton.addEventListener("click", function() {
			if (!SASocket)
				connect();
		})
	});

	page.addEventListener("pagehide", function() {
		// release object
		sectionChanger.destroy();

		removeBtn.removeEventListener("click", removeSection);
		testBtn.removeEventListener("click", testSection);

	});

})();
