$(document).ready(function() {
	$('body').on('show.bs.tab', '.main .connections a[data-toggle="tab"]', function(e) {
		$('.main .connection').html('');
	});
	$('body').on('click','.main .connections table>tbody>tr', function(e) {
		displayDummyConnection();
	});
});

function addConnection() {
	$('#modal').addClass("edit-connection");
	$('#editor_label').html('Add connection');
	$('#modal').modal('show');
}

function saveConnection(title, content) {
	//TODO validation
	displayConnection(title, content);
	$('#modal').modal('hide');
}
function display_connections() {
	$.get('templates/connections.html', function(template) {
	    var rendered = Mustache.render(template, {
			  "groups": [
			 			    { "title": "Literature", "id" : "literature" },
			 			    { "title": "Culture", "id" : "culture" },
			 			    { "title": "Alternative comics", "id" : "comics" },
			 			    { "title": "Online magazines", "id" : "magazines" },
			 			    { "title": "Underground culture", "id" : "underground" }
			 			  ]
			 			});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content connections');
	    displayContent(contentElement, rendered);
	  });
}
function displayConnection(title, content) {
	if (!$('.main>.jumbotron>.connections').length) {
		//TODO callback might be needed, to wait until connections are shown before showing connection
		display_connections();
	}
	$.get('templates/connection.html', function(template) {
	    var rendered = Mustache.render(template, {"title": title, "content": content});
	    $(".main .connections .connection").html(rendered);
	  });
}
function displayDummyConnection() {
	displayConnection("bundolo", "http://www.bundolo.org");
}