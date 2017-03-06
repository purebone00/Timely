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
	console.log($(status).text());
	if($(status).text() == 1) {
		$(status).addClass('btn btn-success');
		$(status).text("Submitted");
	} else {
		$(status).addClass('btn btn-danger');
		$(status).text("Not Submitted");
	}
		
}

window.onload = function() {
	statusClass();
};
		