var homeHtml = "";
$(document).ready(function() {
  $('[data-toggle=offcanvas]').click(function() {
    $('.row-offcanvas').toggleClass('active');
  });
  $('[title]').hover(
	function() {
		$('.status_bar>div').text($(this).attr('title'));
	}, function() {
		$('.status_bar>div').text('');
	}
  );
  var mainContent = $(".main>.jumbotron>.content");
  homeHtml = mainContent.html();
  addContextMenu(mainContent);
});

function displayContent(parentElement, html) {
	parentElement.html(html);
	addContextMenu(parentElement);
}

function displayHomeText() {
	displayContent($(".main>.jumbotron>.content"), homeHtml);
}
function displayDummyText() {
	displayContent($(".main>.jumbotron>.content"), '<h1>text title</h1>\
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