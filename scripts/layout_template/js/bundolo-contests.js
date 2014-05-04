$(document).ready(function() {
	$('body').on('click','.main .contests table>tbody>tr', function(e) {
		displayDummyContest();
	});
});

function display_contests() {
	$.get('templates/contests.html', function(template) {
	    var rendered = Mustache.render(template, {"contests": [
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." },
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." },
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." },
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." },
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." },
                             			 			    { "title": "aaa0", "date" : "04.05.2014.", "author": "some_user", "expiration_date" : "04.05.2014." }
                                			 			  ]
});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content contests');
	    displayContent(contentElement, rendered);
	  });
}

function addContest() {
	$('#modal').addClass("edit-contest");
	$('#editor_label').html('Add contest');
	$('#modal').modal('show');
}

function saveContest(title, expiration_date, content) {
	//TODO validation
	displayContest('dummy_user', '04.05.2014.', expiration_date, title, content);
	$('#modal').modal('hide');
}

function displayContest(author, date, expiration_date, title, content) {
	if (!$('.main>.jumbotron>.contests').length) {
		//TODO callback might be needed, to wait until contests are shown before showing contest
		display_contests();
	}
	$.get('templates/contest.html', function(template) {
	    var rendered = Mustache.render(template, {"author": author, "title": title, "date": date, "content": content, "expiration_date": expiration_date});
	    var contentElement = $('.main>.jumbotron>.contests>.contest');
	    displayContent(contentElement, rendered);
	  });
}
function displayDummyContest() {
	displayContest('kiloster', '04.05.2014.', '04.05.2014.', 'Razorback sucker', '<p>Combtooth blenny houndshark clown triggerfish paperbone,\
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