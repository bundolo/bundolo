$(document).ready(function() {
	$('body').on('click','.main .authors table>tbody>tr', function(e) {
		displayDummyAuthor();
	});
});

function display_authors() {
	$.get('templates/authors.html', function(template) {
	    var rendered = Mustache.render(template, {"authors": [
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" },
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" },
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" },
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" },
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" },
                             			 			    { "date" : "04.05.2014.", "author": "some_user", "description" : "bbb0" }	
                                			 			  ]
});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content authors');
	    displayContent(contentElement, rendered);
	  });
}

function addAuthor() {
	$('#modal').addClass("edit-author");
	$('#modal').modal('show');
}

function saveAuthor(author, description) {
	//TODO validation
	displayAuthor('dummy_user', date, description);
	$('#modal').modal('hide');
}

function displayAuthor(author, date, description) {
	$.get('templates/author.html', function(template) {
	    var rendered = Mustache.render(template, {"author": author, "date": date, "description": description});
	    var contentElement = $('.main>.jumbotron>.authors>.author');
	    displayContent(contentElement, rendered);
	  });
}
function displayDummyAuthor() {
	displayAuthor('kiloster', '04.05.2014.', 'Razorback sucker');
}