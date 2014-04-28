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
  $('#edit_content').summernote({
	  toolbar: [
	    ['style', ['style']],
	    ['font', ['bold', 'italic', 'underline', 'strike', 'clear']],
	    ['fontname', ['fontname']],
	    ['color', ['color']],
	    ['fontsize', ['fontsize']],
	    ['height', ['height']],
	    ['para', ['ul', 'ol', 'paragraph']],	    
	    ['insert', ['picture', 'link']], // no insert buttons
	    //['table', ['table']], // no table button
	    //['help', ['help']] //no help button
	  ]
	});
  	var modalDialog = $('#modal');
  	modalDialog.on('hidden.bs.modal', function(e) {
  		modalDialog.removeClass("edit-comment");
  		modalDialog.removeClass("edit-text");
	});
  	modalDialog.find('.btn-primary').click(function(e) {
  		if (modalDialog.hasClass('edit-comment')) {
  			saveComment($('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-text')) {
  			saveText($('#edit_title').val(), $('#edit_description').val(), $('#edit_content').code());
  		}
        return false;
    });
});

function displayContent(parentElement, html) {
	parentElement.html(html);
	addContextMenu(parentElement);
}