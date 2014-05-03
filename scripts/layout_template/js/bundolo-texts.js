$(document).ready(function() {
	$('body').on('click','.main .texts table>tbody>tr', function(e) {
		displayDummyText();
	});
});

function display_texts() {
	$.get('templates/texts.html', function(template) {
	    var rendered = Mustache.render(template, {});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content texts');
	    displayContent(contentElement, rendered);
	  });
}

function addText() {
	$('#modal').addClass("edit-text");
	$('#modal').modal('show');
}

function saveText(title, description, content) {
	//TODO validation
	displayText('dummy_user', title, description, content);
	$('#modal').modal('hide');
}

function displayText(author, title, description, content) {
	$.get('templates/text.html', function(template) {
	    var rendered = Mustache.render(template, {"author": author, "title": title, "description": description, "content": content});
	    var contentElement = $('.main>.jumbotron>.content');
	    contentElement.attr('class', 'content text');
	    displayContent(contentElement, rendered);
	  });
}
function displayDummyText() {
	displayText('kiloster', 'Razorback sucker', 'dragonet spiny dogfish cuckoo wrasse', '<p>Combtooth blenny houndshark clown triggerfish paperbone,\
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