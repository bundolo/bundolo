$(document).ready(function() {
	$('.main a[data-toggle="tab"]').on('show.bs.tab', function(e) {
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
function displayTopic(textTitle, textDescription, textContent) {
	displayContent($(".main>.jumbotron>.content"), '<h1>'+textTitle+'</h1><h3>'+textDescription+'</h3>' + textContent);
}
function displayDummyTopic() {
	displayText('Razorback sucker', 'dragonet spiny dogfish cuckoo wrasse', '<p>Combtooth blenny houndshark clown triggerfish paperbone,\
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