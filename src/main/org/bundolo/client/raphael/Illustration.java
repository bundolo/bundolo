package org.bundolo.client.raphael;

import java.util.logging.Logger;

import com.hydro4ge.raphaelgwt.client.Raphael;

public class Illustration extends Raphael {

    private final Logger logger = Logger.getLogger(Illustration.class.getName());

    String illustrationText;

    public Illustration(int width, int height, String illustrationText) {
	super(width, height);
	this.illustrationText = illustrationText;
    }

    @Override
    public void onLoad() {
	super.onLoad();
	setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space", "preserve");
	final Text text = new Text(200, 100, illustrationText);
	text.attr("font-family", "monospace");
    }
}