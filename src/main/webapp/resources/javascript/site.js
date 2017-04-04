$(function() {
	$('#show-sidebar').click(function() {
		$('.menu.sidebar').sidebar('toggle');
	});

	$('#hide-sidebar').click(function() {
		$('#show-sidebar').show();
		$('.menu.sidebar').sidebar('toggle');
	});
});

function statusClass() {
	var status = document.getElementsByClassName("status");
	
	$(status).each(function(){
	    if($(this).text() == 1) {
	    	$(this).addClass('btn btn-success');
			$(this).html('<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>');
	    } else {
	    	$(this).addClass('btn btn-danger');
			$(this).html('<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>');
	    }
	    	
	 });
}

function removeShowingEntriesOnTimesheetsPage() {
	var showEntries = document.getElementsByClassName("dataTables_info");
	window.location.pathname+window.location.search
	if(window.location.pathname+window.location.search === "/Timely/faces/timesheet.xhtml")
	showEntries[0].style.display = "none";
}

function projectNumberChange() {
	var projNo = document.getElementsByClassName("ProjectNo");
	$(projNo).each(function(){
	    if($(this).text() == 0)
	    	$(this).text(" ");
	 });
}

function redIfNotificate(notificationCount) {
	var iconAwesome = document.getElementsByClassName("fa fa-exclamation fa-2x");
	var icon = iconAwesome[0];
	
	if(parseInt(notificationCount.textContent) != 0) {
		icon.style.color = "red";
		icon.className += " animated infinite jello";
	}
}

function addToNotifications() {
	var nId = document.getElementById("dropdown");
	var firstChild = nId.getElementsByTagName("ul")[0];
	var notificationCount = document.getElementById("dtLdropdown");
	redIfNotificate(notificationCount);
	if(parseInt(notificationCount.textContent) != 0) {
		addTSANotification(firstChild, notificationCount);
	}
}

function addTSANotification(firstChild, notificationCount) {
	var newNotification = document.createElement("li");
	var notificationMessage = "You have " + notificationCount.textContent + " timesheets to approve."; 
	newNotification.style.cursor = "pointer";
	newNotification.innerHTML = "<a href=\"/Timely/faces/timesheetapprover.xhtml\" role=\"menuitem\" tabindex=\"-1\">"+ notificationMessage +"</a>"
	firstChild.appendChild(newNotification);
}

$(document).ready(function() {
    $(".dropdown-toggle").dropdown();
    removeShowingEntriesOnTimesheetsPage();
});


window.onload = function() {
	statusClass();
	projectNumberChange();
	addToNotifications();

};
		