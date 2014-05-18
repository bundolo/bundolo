$(document).ready(function() {
	displaySidebar();
});

function preventSidebarToggle(element, event) {
	var thisAccordion = element.closest('.panel');
	if (thisAccordion.hasClass('active')) {
		event.stopPropagation();
	}
}

function displaySidebar() {
	$.get('templates/sidebar.html', function(template) {
	    var rendered = Mustache.render(template, {
			  "collapsibles": [
			 			    { "title": "Texts", "id" : "texts", "icon" : "file-text-o", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Serials", "id" : "serials", "icon" : "book", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Authors", "id" : "authors", "icon" : "user", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Announcements", "id" : "announcements", "icon" : "bullhorn", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Forum", "id" : "topics", "icon" : "comments-o", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Contests", "id" : "contests", "icon" : "eye", "columns" : ["author", "title", "date", "last_activity"] },
			 			    { "title": "Connections", "id" : "connections", "icon" : "link", "columns" : ["author", "title", "date", "last_activity"] },
			 			  ]
			 			});
	    $(".sidebar").html(rendered);
	    //TODO assign event handlers if any
	    /*$(".tablesorter").tablesorter({
			theme : 'bootstrap',
			headerTemplate : '{content} {icon}',
			widgets : [ 'zebra', 'columns', 'uitheme', 'filter', 'scroller' ],
			sortList : [ [ 0, 0 ], [ 1, 0 ] ],
			filter_cssFilter  : 'tablesorter-filter',
	        filter_startsWith : false,
	        filter_ignoreCase : true,
	        widgetOptions : {
	            scroller_height : 150,
	            scroller_barWidth : 17,
	            scroller_jumpToHeader: true,
	            scroller_idPrefix : 's_'
	          }
		});*/
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
	  });
}

function displaySidebarAccordion(type) {
	var itemCounter = 0;
	$.get('templates/'+type+'.html', function(template) {
		var items = {"items": []};
		for ( var i = 0; i < 25; i++ ) {
			items.items.push(generateDummyItem(itemCounter, type));
			itemCounter++;
		}
		var rendered = Mustache.render(template, items);
		var tableBody = $('.sidebar #collapse_'+type+' tbody');
		tableBody.append(rendered);
		$('.sidebar #collapse_'+type+' .tablesorter').trigger("updateAll");
		tableBody.bind('scroll', function() {
	    	if($(this).scrollTop() + $(this).innerHeight() >= this.scrollHeight) {
    			var additional_items = {"items": []};
    			for ( var i = 0; i < 5; i++ ) {
    				additional_items.items.push(generateDummyItem(itemCounter, type));
    				itemCounter++;
    			}
    		    var rendered_rows = Mustache.render(template, additional_items);
    		    $(this).append(rendered_rows);
    		    $('.sidebar #collapse_'+type+' .tablesorter').trigger("updateAll");
	        }
	    });
	  });
}

function generateDummyItem(id, type) {
	switch(type) {
	case "texts":
	    return generateDummyText(id);
	    break;
	case "serials":
		return generateDummySerial(id);
	    break;
	case "authors":
		return generateDummyAuthor(id);
	    break;
	case "announcements":
		return generateDummyAnnouncement(id);
	    break;
	case "contests":
		return generateDummyContest(id);
	    break;
	case "connections":
		return generateDummyConnection(id);
	    break;
	case "topics":
		return generateDummyTopic(id);
	    break;
	}
}