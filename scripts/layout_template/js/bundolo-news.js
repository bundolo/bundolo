$(document).ready(function() {
	$('body').on('click','.main .news table>tbody>tr', function(e) {
		displayDummySingleNews();
	});
});

function display_news() {
	$.get('templates/news.html', function(template) {
	    var rendered = Mustache.render(template, {"news": [
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "content" : "bbb0" },
                             			 			    { "title": "aaa1", "date" : "14.05.2014.", "author": "some_user", "content" : "bbb1" },
                                 			 			{ "title": "aaa2", "date" : "24.05.2014.", "author": "some_user", "content" : "bbb2" },
                                 			 			{ "title": "aaa3", "date" : "05.05.2014.", "author": "some_user", "content" : "bbb3" },
                                 			 			{ "title": "aaa4", "date" : "06.05.2014.", "author": "some_user", "content" : "bbb4" },
                                 			 			{ "title": "aaa5", "date" : "07.05.2014.", "author": "some_user", "content" : "bbb5" },
                                 			 			{ "title": "aaa6", "date" : "08.05.2014.", "author": "some_user", "content" : "bbb6" }
                                			 			  ]
});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content news');
	    displayContent(contentElement, rendered);
	  });
}

function addNews() {
	$('#modal').addClass("edit-news");
	$('#modal').modal('show');
}

function saveNews(title, content) {
	//TODO validation
	displayNews('dummy_user', date, title, content);
	$('#modal').modal('hide');
}

function displaySingleNews(author, date, title, content) {
	$.get('templates/single_news.html', function(template) {
	    var rendered = Mustache.render(template, {"author": author, "title": title, "date": date, "content": content});
	    var contentElement = $('.main>.jumbotron>.news>.single_news');
	    //contentElement.attr('class', 'content news');
	    displayContent(contentElement, rendered);
	  });
}
function displayDummySingleNews() {
	displaySingleNews('kiloster', '04.05.2014.', 'Razorback sucker', '<p>Combtooth blenny houndshark clown triggerfish paperbone,\
			"European eel tilapia sea snail tilapia waryfish," Bitterling\
			crocodile shark. Flagblenny Hammerjaw stonecat freshwater herring\
			false brotula false moray; kanyu Atlantic eel blue triggerfish\
			weeverfish Rainbowfish leaffish. Rudderfish alligatorfish,\
			Billfish gray reef shark Razorback sucker flounder quillback;\
			clownfish medusafish Atlantic trout? Gouramie bichir frilled shark\
			dragonet spiny dogfish cuckoo wrasse. kanyu Atlantic eel blue triggerfish\
			weeverfish Rainbowfish leaffish. Rudderfish alligatorfish,\
			Billfish gray reef shark Razorback sucker flounder quillback;\
			clownfish medusafish Atlantic trout? Gouramie bichir frilled shark\
			dragonet spiny dogfish cuckoo wrasse.</p>\
		<p>Combtooth blenny houndshark clown triggerfish paperbone,\
			"European eel tilapia sea snail tilapia waryfish," Bitterling\
			crocodile shark. Flagblenny Hammerjaw stonecat freshwater herring\
			false brotula false moray; kanyu Atlantic eel blue triggerfish\
			weeverfish Rainbowfish leaffish. Rudderfish alligatorfish,\
			Billfish gray reef shark Razorback sucker flounder quillback;\
			clownfish medusafish Atlantic trout? Gouramie bichir frilled shark\
			dragonet spiny dogfish cuckoo wrasse. kanyu Atlantic eel blue triggerfish\
			weeverfish Rainbowfish leaffish. Rudderfish alligatorfish,\
			Billfish gray reef shark Razorback sucker flounder quillback;\
			clownfish medusafish Atlantic trout? Gouramie bichir frilled shark\
			dragonet spiny dogfish cuckoo wrasse.</p>');
}