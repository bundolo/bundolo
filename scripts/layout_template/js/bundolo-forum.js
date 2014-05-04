$(document).ready(function() {
	$('body').on('show.bs.tab', '.main .forum a[data-toggle="tab"]', function(e) {
		$('.main .topic').html('');
	});
	$('body').on('click','.main .forum table>tbody>tr', function(e) {
		displayDummyTopic();
	});
});

function addPost() {
	$('#modal').addClass("edit-post");
	$('#editor_label').html('Add post');
	$('#modal').modal('show');
}

function savePost(content) {
	//TODO validation
	displayPost(textTitle, textDescription, textContent);
	$('#modal').modal('hide');
}
function display_forum() {
	$.get('templates/forum.html', function(template) {
	    var rendered = Mustache.render(template, {
			  "categories": [
			 			    { "title": "Literature", "id" : "literature" },
			 			    { "title": "Bundolo", "id" : "bundolo" },
			 			    { "title": "Various", "id" : "various" },
			 			    { "title": "Suggestions", "id" : "suggestions" },
			 			    { "title": "Archive", "id" : "archive" }
			 			  ]
			 			});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content forum');
	    displayContent(contentElement, rendered);
	  });
}
function displayTopic(title) {
	if (!$('.main>.jumbotron>.forum').length) {
		//TODO callback might be needed, to wait until forum is shown before showing topic
		display_forum();
	}
	$.get('templates/topic.html', function(template) {
	    var rendered = Mustache.render(template, {"title": title});
	    $(".main .forum .topic").html(rendered);
	  });
}
function addTopic() {
	$('#modal').addClass("edit-topic");
	$('#editor_label').html('Add topic');
	$('#modal').modal('show');
}
function saveTopic(title, content) {
	//TODO validation
	displayTopic(title);
	$('#modal').modal('hide');
}
function displayDummyTopic() {
	displayTopic("ukinimo goste i probudimo admina");
}