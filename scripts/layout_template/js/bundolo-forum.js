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
	$('#modal').modal('show');
}

function savePost(postContent) {
	//TODO validation
	displayText(textTitle, textDescription, textContent);
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
	$.get('templates/topic.html', function(template) {
	    var rendered = Mustache.render(template, {"title": title});
	    $(".main .forum .topic").html(rendered);
	  });
}
function displayDummyTopic() {
	displayTopic("ukinimo goste i probudimo admina");
}