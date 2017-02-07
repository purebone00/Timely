$(function() {
			$('#show-sidebar').click(function() {
				$('.menu.sidebar').sidebar('toggle');
			});

			$('#hide-sidebar').click(function() {
				$('#show-sidebar').show();
				$('.menu.sidebar').sidebar('toggle');
			});
		});
		