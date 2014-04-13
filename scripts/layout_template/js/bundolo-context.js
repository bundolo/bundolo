$(document).ready(function() {
	$('.context-button').click(function(e) {
		setContextMenuPostion(e, $('.context-menu'));				        
        return false;
    });
    $(document).click(function(event) { 
        if($(event.target).parents().index($('.context-menu')) == -1) {
            if($('.context-menu').is(":visible")) {
                $('.context-menu').hide()
            }
        }        
    })
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