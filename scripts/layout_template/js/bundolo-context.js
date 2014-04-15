$(document).ready(function() {
	var item = $('<span class="fa-stack fa-2x comments-button">\
			<i class="fa fa-circle fa-stack-2x"></i>\
			<i class="fa fa-comment-o fa-stack-1x fa-inverse"></i>\
			</span>');
	item.click(function(e) {
		setContextMenuPostion(e, $('.context-menu'));				        
        return false;
    });
	$('.content').append(item);
    $(document).click(function(event) { 
        if($(event.target).parents().index($('.context-menu')) == -1) {
            if($('.context-menu').is(":visible")) {
                $('.context-menu').hide()
            }
        }        
    })
    $('.comments-button').hover(
    	function() {
    		$(this).parent().addClass("hover");
    		if ($(this).parent().parent().css("overflow")=="hidden") {
    			$(this).parent().parent().addClass("show-overflow");
    		}
    		//alert("in: "+$(this).parent().attr("class"));
    	}, function() {
    		$(this).parent().removeClass("hover");
    		$(this).parent().parent().removeClass("show-overflow");
    		//alert("out: "+$(this).parent().attr("class"));
    	}
    );
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