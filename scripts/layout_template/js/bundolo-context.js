var commentParentElement;
//TODO these variables could probably be removed
var accordionCounter = 0;
var commentCounter = 0;
$(document).ready(function() {
	
});

function setContextMenuPostion(event, contextMenu) {
    var mousePosition = {};
    var menuPostion = {};
    var menuDimension = {};

    menuDimension.x = contextMenu.outerWidth();
    menuDimension.y = contextMenu.outerHeight();
    mousePosition.x = event.pageX;
    mousePosition.y = event.pageY;

    if (mousePosition.x + menuDimension.x > $(window).width() + $(window).scrollLeft()) {
        menuPostion.x = mousePosition.x - menuDimension.x;
    } else {
        menuPostion.x = mousePosition.x;
    }

    if (mousePosition.y + menuDimension.y > $(window).height() + $(window).scrollTop()) {
        menuPostion.y = mousePosition.y - menuDimension.y;
    } else {
        menuPostion.y = mousePosition.y;
    }

    contextMenu.css({
        display: "block",
        left: menuPostion.x,
        top: menuPostion.y
    });
    return menuPostion;
}

function addContextMenu(parentElement) {
	var contextMenu =  $('<div class="context-menu panel-group" id="accordion' + accordionCounter++ + '"><div><div></div></div></div>');
	//close context menu if it is open and there has been a click outside of it
	$(document).click(function(event) {
		var modalDialog = $('#modal');
		var targetParents = $(event.target).parents();
		if ((targetParents.index(modalDialog) == -1) && !modalDialog.hasClass('in') && (targetParents.index(contextMenu) == -1)) {
            if(contextMenu.is(":visible")) {
            	contextMenu.hide()
            }
        }        
    })
	var commentsRoot = contextMenu.find("div>div");
	var rootCommentButton = addCommentButton(commentsRoot, commentsRoot);
	rootCommentButton.click(function(e) {
		addComment(commentsRoot);
        return false;
    });
	$('body').append(contextMenu);
	var commentsButton = $('<span class="fa-stack fa-2x comments-button">\
			<i class="fa fa-circle fa-stack-2x"></i>\
			<i class="fa fa-comment-o fa-stack-1x fa-inverse"></i>\
			</span>');
	commentsButton.click(function(e) {
		setContextMenuPostion(e, contextMenu);				        
        return false;
    });
	commentsButton.attr("title", "Comments");
	parentElement.append(commentsButton);
	
}

function addComment(parentElement) {
	$('#modal').addClass("edit-comment");
	//TODO this can probably be nicer. we are supposed to pass variable to the click event of an element inside modal we are showing
	commentParentElement = parentElement;
	$('#modal').modal('show');
}

function saveComment(commentContent) {
	//TODO validation
	var comment = $('<div class="panel panel-default comment" id="comment'+commentCounter+++'"></div>');
	var commentContent = $('<div class="panel-heading"><span>'+commentContent+'</span></div><div id="collapse1" class="panel-collapse"><div class="panel-body"><div class="panel-group" id="accordion1"><div><div></div></div></div></div></div>');
	var commentCommentButton = addCommentButton(comment);
	comment.append(commentContent);
	commentParentElement.append(comment);
	commentCommentButton.click(function(e) {
		addComment(comment.find(">.panel-collapse>div>div>div>div"));
        return false;
    });
	$('#modal').modal('hide');
}

function addCommentButton(parentElement) {
	var commentButton = $('<span title="Add comment" class="fa-stack fa-2x pull-right comment-button" id="comment-button'+commentCounter+'">\
			<i class="fa fa-circle fa-stack-2x"></i>\
			<i class="fa fa-plus fa-stack-1x fa-inverse"></i>\
			</span>');
	parentElement.append(commentButton);
	return commentButton;
}

function displayComment(author, content, parentElement) {
	$.get('templates/comment.html', function(template) {
	    var rendered = Mustache.render(template, {"author": author, "content": content});
	    parentElement.append($(rendered));
	    //displayContent($(".main>.jumbotron>.content"), rendered);
	  });
}