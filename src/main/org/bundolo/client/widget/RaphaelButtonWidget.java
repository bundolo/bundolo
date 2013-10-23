package org.bundolo.client.widget;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.raphael.IconPathType;
import org.bundolo.client.view.page.PageView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiConstructor;
import com.hydro4ge.raphaelgwt.client.Attr;
import com.hydro4ge.raphaelgwt.client.DragCallback;
import com.hydro4ge.raphaelgwt.client.IconPaths;
import com.hydro4ge.raphaelgwt.client.PathBuilder;
import com.hydro4ge.raphaelgwt.client.Raphael;
import com.hydro4ge.raphaelgwt.client.Raphael.Circle;
import com.hydro4ge.raphaelgwt.client.Raphael.Path;

public class RaphaelButtonWidget extends Raphael implements HasClickHandlers {
	
	private Logger logger = Logger.getLogger(RaphaelButtonWidget.class.getName());
	
	private static final double INITIAL_ICON_SCALE = 1.4;
	private static final double ICON_ZOOM_IN_SCALE = 1.7;
	private static final double INITIAL_MODIFIER_SCALE = 0.9;
	private static final double MODIFIER_ZOOM_IN_SCALE = 1.2;
	private static final double INITIAL_RADIUS = 16;
	private static final String INITIAL_STROKE = "#aaa";
	private final static int centerOffset = 27;
	private final static int canvasWidth = centerOffset * 2;
	private final static int canvasHeight = canvasWidth;
	
//	private Circle circle;
	private Path circlePath;
	private Path iconPath;
	private Path modifierPath;
	
	private JSONObject initialAttrs;
	
	private String icon;
	private String label;
	private String modifier;
	
	@UiConstructor
	public RaphaelButtonWidget(String icon, String label, String modifier) {
		super(canvasWidth, canvasHeight);
		this.icon = icon;
		this.label = label;
		this.modifier = modifier;
		draw();
	}
	
//	@Override
//	public void onLoad() {
//	    super.onLoad();
//		circle = new Circle(centerOffset, centerOffset, INITIAL_RADIUS);
//		PathBuilder iconPathBuilder = new PathBuilder();
//		iconPathBuilder.append(IconPathType.getByName(icon).getIconPath());
//		iconPath = new Path(iconPathBuilder);
//		initialAttrs = new JSONObject();
//		initialAttrs.put("fill", new JSONString("#000"));
//		initialAttrs.put("stroke-width", new JSONString("1"));
//		initialAttrs.put("stroke-linejoin", new JSONString("round"));
//		initialAttrs.put("stroke", new JSONString(INITIAL_STROKE));
//		initialAttrs.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
//		circle.attr(initialAttrs);
//		iconPath.attr(initialAttrs);
//		iconPath.translate(10, 10);
//		
//		if (Utils.hasText(modifier)) {
//	    	PathBuilder modifierPathBuilder = new PathBuilder();
//	    	modifierPathBuilder.append(IconPathType.getByName(modifier).getIconPath());
//	    	modifierPath = new Path(modifierPathBuilder);
//	    	initialAttrs.put("scale", new JSONNumber(INITIAL_MODIFIER_SCALE));
//	    	modifierPath.attr(initialAttrs);
//	    	modifierPath.translate(24, 24);
//	    }
//		
//		iconPath.addDomHandler(new MouseOverHandler() {
//			@Override
//			public void onMouseOver(MouseOverEvent event) {
//				JSONObject jSONObject = new JSONObject();
//				jSONObject.put("stroke", new JSONString("#ffffff"));
//				jSONObject.put("scale", new JSONNumber(ICON_ZOOM_IN_SCALE));
//				iconPath.animate(jSONObject, 100);
//				circle.animate(jSONObject, 100);
//				if (modifierPath != null) {
//					jSONObject.put("scale", new JSONNumber(MODIFIER_ZOOM_IN_SCALE));
//					modifierPath.animate(jSONObject, 100);
//				}
//			}
//		}, MouseOverEvent.getType());
//	    
//	    iconPath.addDomHandler(new MouseOutHandler() {
//
//			@Override
//			public void onMouseOut(MouseOutEvent event) {
//				JSONObject jSONObject = new JSONObject();
//				jSONObject.put("stroke", new JSONString(INITIAL_STROKE));
//				jSONObject.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
//				iconPath.animate(jSONObject, 100);
//				circle.animate(jSONObject, 100);
//				if (modifierPath != null) {
//					jSONObject.put("scale", new JSONNumber(INITIAL_MODIFIER_SCALE));
//					modifierPath.animate(jSONObject, 100);
//				}
//			}
//		}, MouseOutEvent.getType());
//	    setTitle(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.valueOf(label)));
//	}
	
	private void draw() {
//		circle = new Circle(0, 0, INITIAL_RADIUS);
		PathBuilder circlePathBuilder = new PathBuilder();
		circlePathBuilder.append("M"+centerOffset+","+(centerOffset-INITIAL_RADIUS)+"A"+INITIAL_RADIUS+","+INITIAL_RADIUS+",0,1,1,"+(centerOffset-0.1)+","+(centerOffset-INITIAL_RADIUS)+"z");
		circlePath = new Path(circlePathBuilder);
		
		PathBuilder iconPathBuilder = new PathBuilder();
		iconPathBuilder.append(IconPathType.getByName(icon).getIconPath());
		iconPath = new Path(iconPathBuilder);
		initialAttrs = new JSONObject();
		initialAttrs.put("fill", new JSONString("#000"));
		initialAttrs.put("stroke-width", new JSONString("1"));
		initialAttrs.put("stroke-linejoin", new JSONString("round"));
		initialAttrs.put("stroke", new JSONString(INITIAL_STROKE));
		initialAttrs.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
//		initialAttrs.put("transform", new JSONString("t" + centerOffset + "," + centerOffset));
//		circle.attr(initialAttrs);
//		circle.translate(centerOffset, centerOffset);
		
		circlePath.attr(initialAttrs);
//		circlePath.translate(centerOffset, centerOffset);
//		initialAttrs.put("transform", new JSONString("T10,10"));
		iconPath.attr(initialAttrs);
		iconPath.translate(10, 10);
		
		if (Utils.hasText(modifier)) {
	    	PathBuilder modifierPathBuilder = new PathBuilder();
	    	modifierPathBuilder.append(IconPathType.getByName(modifier).getIconPath());
	    	modifierPath = new Path(modifierPathBuilder);
	    	initialAttrs.put("scale", new JSONNumber(INITIAL_MODIFIER_SCALE));
//	    	initialAttrs.put("transform", new JSONString("T24,24"));
	    	modifierPath.attr(initialAttrs);
	    	modifierPath.translate(24, 24);
	    }
		
		iconPath.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				JSONObject jSONObject = new JSONObject();
				jSONObject.put("stroke", new JSONString("#ffffff"));
				jSONObject.put("scale", new JSONNumber(ICON_ZOOM_IN_SCALE));
				iconPath.animate(jSONObject, 100);
				circlePath.animate(jSONObject, 100);
				if (modifierPath != null) {
					jSONObject.put("scale", new JSONNumber(MODIFIER_ZOOM_IN_SCALE));
					modifierPath.animate(jSONObject, 100);
				}
			}
		}, MouseOverEvent.getType());
	    
	    iconPath.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				JSONObject jSONObject = new JSONObject();
				jSONObject.put("stroke", new JSONString(INITIAL_STROKE));
				jSONObject.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
				iconPath.animate(jSONObject, 100);
				circlePath.animate(jSONObject, 100);
				if (modifierPath != null) {
					jSONObject.put("scale", new JSONNumber(INITIAL_MODIFIER_SCALE));
					modifierPath.animate(jSONObject, 100);
				}
			}
		}, MouseOutEvent.getType());
	    setTitle(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.valueOf(label)));
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

}