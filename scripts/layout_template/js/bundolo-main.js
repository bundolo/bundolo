var homeHtml = "";
$(document).ready(function() {
	$('[data-toggle=offcanvas]').click(function() {
		$('.row-offcanvas').toggleClass('active');
	});
	$('body').on('mouseenter', '[title]', function(e) {
		displayStatusBar($(this).attr('title'));
	});
	$('body').on('mouseleave', '.navbar-header [title]', function(e) {
		displayStatusBar('');
	});
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
  	/*$('#edit_date').datepicker({
	    format: "dd/mm/yyyy",
	    weekStart: 1
	});*/
  	$('#edit_date').datepicker({
	    format: "dd/mm/yyyy",
	    weekStart: 1
	}).on('show', function() {
		//this is a fix for datepicker not showing when it's opened from modal dialog
  		var modal = $('#edit_date').closest('.modal');
  		var datePicker = $('body').find('.datepicker');
  		if(!modal.length) {
  			$(datePicker).css('z-index', 'auto');
  			return;
  		}
  		var zIndexModal = $(modal).css('z-index');
  		$(datePicker).css('z-index', zIndexModal + 1);
  	});
  	
  	var modalDialog = $('#modal');
  	modalDialog.on('hidden.bs.modal', function(e) {
  		modalDialog.attr('class', 'modal fade');  		
  		//modalDialog.removeClass("edit-comment");
  		//modalDialog.removeClass("edit-text");
	});
  	modalDialog.on('shown.bs.modal', function(e) {
  		//TODO for comments editor should be focused, but for some other content, maybe some other field
  		$('.note-editable').focus();
	});
  	modalDialog.find('.btn-primary').click(function(e) {
  		if (modalDialog.hasClass('edit-comment')) {
  			saveComment($('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-text')) {
  			saveText($('#edit_title').val(), $('#edit_description').val(), $('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-serial')) {
			saveSerial($('#edit_title').val(), $('#edit_description').val());
  		} else if (modalDialog.hasClass('edit-episode')) {
			saveEpisode($('#edit_title').val(), $('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-topic')) {
			saveTopic($('#edit_title').val(), $('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-post')) {
			savePost($('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-news')) {
			saveNews($('#edit_title').val(), $('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-connection')) {
			saveConnection($('#edit_title').val(), $('#edit_content').code());
  		} else if (modalDialog.hasClass('edit-contest')) {
			saveContest($('#edit_title').val(), $('#edit_date').val(), $('#edit_content').code());
  		}
        return false;
    });
});

function displayContent(parentElement, html) {
	parentElement.html(html);
	addContextMenu(parentElement);
}

function sanitize(content) {
	//TODO make this more generic. strip all tags for some content, be selective for other
	return content.replace(/(<([^>]+)>)/ig,"");
}

function displayHome() {
	var contentElement = $('.main>.jumbotron>.content');
    contentElement.attr('class', 'content');
	displayContent(contentElement, homeHtml);
}