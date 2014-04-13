$(document).ready(function() {
	$(".tablesorter").tablesorter({
		theme : 'bootstrap',
		headerTemplate : '{content} {icon}',
		widgets : [ 'zebra', 'columns', 'uitheme', 'filter' ],
		sortList : [ [ 0, 0 ], [ 1, 0 ] ],
		filter_cssFilter  : 'tablesorter-filter',
        filter_startsWith : false,
        filter_ignoreCase : true
	});
	$('#sidebarAccordion').on('show.bs.collapse', function(e) {
		$(e.target).parent('.panel-default')
				.addClass('active');
		if (!$('.row-offcanvas.active').length) {
			$('.row-offcanvas').addClass('active');
		}
	});
	$('#sidebarAccordion').on('shown.bs.collapse', function(e) {
		$(e.target).parent('.panel-default')
				.addClass('active');
		if (!$('.row-offcanvas.active').length) {
			$('.row-offcanvas').addClass('active');
		}
	});
	$('#sidebarAccordion').on('hidden.bs.collapse', function(e) {
		$(this).find('.panel-default').not(
				$(e.target)).removeClass('active');
		
		if (!$('.panel-default.active').length) {
			$('.row-offcanvas').removeClass('active');
		}
		
	});
	$('.sidebar input[type="search"]').focus(function(event) {
		preventSidebarToggle($(this), event);
	});
	$('.sidebar input[type="search"]').click(function(event) {
		preventSidebarToggle($(this), event);
	});
	$('.sidebar .table>tbody>tr').click(function() {
    	displayDummyText();
    });
    $('.sidebar .table>tbody>tr').hover(
		function() {
			$(this).find('td').addClass( "hover" );
		}, function() {
			$(this).find('td').removeClass( "hover" );
		}
   );
});

function preventSidebarToggle(element, event) {
	var thisAccordion = element.closest('.panel');
	if (thisAccordion.hasClass('active')) {
		event.stopPropagation();
	}
}