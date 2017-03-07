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
	    	$(status).addClass('btn btn-success');
			$(status).text("Submitted");
	    } else {
	    	$(status).addClass('btn btn-danger');
			$(status).text("Not Submitted");
	    }
	    	
	 });
}

function projectNumberChange() {
	var projNo = document.getElementsByClassName("ProjectNo");
	$(projNo).each(function(){
	    if($(this).text() == 0)
	    	$(this).text(" ");
	 });
}





window.onload = function() {
	statusClass();
	projectNumberChange();

};
		