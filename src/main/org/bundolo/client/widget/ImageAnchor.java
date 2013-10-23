package org.bundolo.client.widget;

import com.google.gwt.user.client.ui.Image; 
import com.google.gwt.resources.client.ImageResource; 
import com.google.gwt.user.client.ui.Anchor; 
import com.google.gwt.user.client.DOM;

public class ImageAnchor extends Anchor {

    public ImageAnchor() { 
    }

    public void setResource(ImageResource imageResource) { 
        Image img = new Image(imageResource); 
        img.setStyleName("navbarimg"); 
        DOM.insertBefore(getElement(), img.getElement(), DOM 
                .getFirstChild(getElement())); 
    } 
}