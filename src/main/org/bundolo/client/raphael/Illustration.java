package org.bundolo.client.raphael;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.Bundolo;
import org.bundolo.client.LocalStorage;
import org.bundolo.client.resource.IconResource;
import org.bundolo.shared.model.PageDTO;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuItem;
import com.hydro4ge.raphaelgwt.client.IconPaths;
import com.hydro4ge.raphaelgwt.client.PathBuilder;
import com.hydro4ge.raphaelgwt.client.Raphael;
import com.hydro4ge.raphaelgwt.client.Raphael.Circle;
import com.hydro4ge.raphaelgwt.client.Raphael.Path;
import com.hydro4ge.raphaelgwt.client.Raphael.Text;

public class Illustration extends Raphael {
	
	private Logger logger = Logger.getLogger(Illustration.class.getName());
	
	String illustrationText;

	public Illustration(int width, int height, String illustrationText) {
		super(width, height);
		this.illustrationText = illustrationText;
	}
	
	@Override
	public void onLoad() {
	    super.onLoad();
	    setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:space","preserve");
	    final Text text = new Text(200, 100, illustrationText);
	    text.attr("font-family","monospace");
	}
}