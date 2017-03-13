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
		