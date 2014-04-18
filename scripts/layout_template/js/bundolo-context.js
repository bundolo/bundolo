var accordionCounter = 0;
var commentCounter = 0;
var dummyCommentCounter = 0;
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
        if($(event.target).parents().index(contextMenu) == -1) {
            if(contextMenu.is(":visible")) {
            	contextMenu.hide()
            }
        }        
    })
	var commentsRoot = contextMenu.find("div>div");
	var rootCommentButton = addCommentButton(commentsRoot, commentsRoot);
	rootCommentButton.click(function(e) {
		addComment(commentsRoot, 'comment'+commentCounter++);
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
	commentsButton.hover(
		function() {
			$('.status_bar>div').text($(this).attr('title'));
			$(this).parent().addClass("hover");
    		if ($(this).parent().parent().css("overflow")=="hidden") {
    			$(this).parent().parent().addClass("show-overflow");
    		}
		}, function() {
			$('.status_bar>div').text('');
			$(this).parent().removeClass("hover");
    		$(this).parent().parent().removeClass("show-overflow");
		}
	);
	parentElement.append(commentsButton);
	
}

function addComment(parentElement, commentText) {
	var comment = $('<div class="panel panel-default comment" id="comment'+commentCounter+'"></div>');
	var commentContent = $('<div class="panel-heading"><span>'+commentText+'</span></div><div id="collapse1" class="panel-collapse"><div class="panel-body"><div class="panel-group" id="accordion1"><div><div></div></div></div></div></div>');
	var commentCommentButton = addCommentButton(comment);
	comment.append(commentContent);
	parentElement.append(comment);
	commentCommentButton.click(function(e) {
		addComment(comment.find(">.panel-collapse>div>div>div>div"), 'comment'+commentCounter+++" "+Math.random() + " "+Math.random());
        return false;
    });
	if(dummyCommentCounter-- >0) {
	//TODO remove this after testing is done
		commentCommentButton.click();
	}
}

function addCommentButton(parentElement) {
	var commentButton = $('<span title="Add comment" class="fa-stack fa-2x pull-right comment-button" id="comment-button'+commentCounter+'">\
			<i class="fa fa-circle fa-stack-2x"></i>\
			<i class="fa fa-plus fa-stack-1x fa-inverse"></i>\
			</span>');

	commentButton.hover(
			function() {
				$('.status_bar>div').text($(this).attr('title'));
				$(this).parent().addClass("hover");
	    		if ($(this).parent().parent().css("overflow")=="hidden") {
	    			$(this).parent().parent().addClass("show-overflow");
	    		}
			}, function() {
				$('.status_bar>div').text('');
				$(this).parent().removeClass("hover");
	    		$(this).parent().parent().removeClass("show-overflow");
			}
		);
	parentElement.append(commentButton);
	return commentButton;
}