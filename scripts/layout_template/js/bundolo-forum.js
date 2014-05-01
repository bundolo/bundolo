$(document).ready(function() {
	$('.main a[data-toggle="tab"]').on('show.bs.tab', function(e) {
		//TODO fix this
		$('.main p').addClass("hidden");
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
function displayForum() {
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
	    displayContent($(".main>.jumbotron>.content"), rendered);
	    $('.main table>tbody>tr').click(function() {
			displayDummyTopic();
		});
	  });
}
function displayTopic(title) {
	$.get('templates/topic.html', function(template) {
	    var rendered = Mustache.render(template, {"title": title});
	    displayContent($(".main>.jumbotron>.content"), rendered);
	  });
}
function displayDummyTopic() {
	displayTopic("ukinimo goste i probudimo admina");
}