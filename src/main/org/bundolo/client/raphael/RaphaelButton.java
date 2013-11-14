package org.bundolo.client.raphael;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.view.page.PageView;
import org.bundolo.shared.model.PageDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.hydro4ge.raphaelgwt.client.DragCallback;
import com.hydro4ge.raphaelgwt.client.PathBuilder;
import com.hydro4ge.raphaelgwt.client.Raphael.Circle;
import com.hydro4ge.raphaelgwt.client.Raphael.Path;

public class RaphaelButton {

    private final Logger logger = Logger.getLogger(RaphaelButton.class
	    .getName());

    private static final double INITIAL_ICON_SCALE = 1.4;
    private static final double ICON_ZOOM_IN_SCALE = 1.7;
    private static final double INITIAL_RADIUS = 16;
    private static final String INITIAL_STROKE = "#aaa";
    private final String iconPathString; // might not be necessary
    private double positionX;
    private double positionY;
    private RaphaelConnection connection1;
    private RaphaelConnection connection2;
    private final int centerOffset = 26;

    private final Circle circle;
    private final Path iconPath;

    private final JSONObject initialAttrs;

    public RaphaelButton(String iconPathString, final int positionX,
	    int positionY, final PageDTO pageDTO) {
	this.iconPathString = iconPathString;
	this.positionX = positionX;
	this.positionY = positionY;
	circle = LocalStorage.getInstance().getNavigation().new Circle(
		centerOffset, centerOffset, INITIAL_RADIUS);
	PathBuilder pathBuilder = new PathBuilder();
	pathBuilder.append(iconPathString);
	iconPath = LocalStorage.getInstance().getNavigation().new Path(
		pathBuilder);
	initialAttrs = new JSONObject();
	initialAttrs.put("fill", new JSONString("#000"));
	initialAttrs.put("stroke-width", new JSONString("1"));
	initialAttrs.put("stroke-linejoin", new JSONString("round"));
	initialAttrs.put("stroke", new JSONString(INITIAL_STROKE));
	initialAttrs.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
	circle.attr(initialAttrs);
	circle.translate(positionX, positionY);
	iconPath.attr(initialAttrs);
	iconPath.translate(positionX + 10, positionY + 10);

	ClickHandler clickHandler = new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		((PageView.Presenter) LocalStorage.getInstance().getPresenter(
			PresenterName.page.name()))
			.onNavigatePageButtonClicked(pageDTO);
	    }
	};
	circle.addDomHandler(clickHandler, ClickEvent.getType());
	iconPath.addDomHandler(clickHandler, ClickEvent.getType());

	MouseOverHandler mouseOverHandler = new MouseOverHandler() {
	    @Override
	    public void onMouseOver(MouseOverEvent event) {
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("stroke", new JSONString("#ffffff"));
		jSONObject.put("scale", new JSONNumber(ICON_ZOOM_IN_SCALE));
		iconPath.animate(jSONObject, 100);
		circle.animate(jSONObject, 100);
	    }
	};
	circle.addDomHandler(mouseOverHandler, MouseOverEvent.getType());
	iconPath.addDomHandler(mouseOverHandler, MouseOverEvent.getType());

	MouseOutHandler mouseOutHandler = new MouseOutHandler() {
	    @Override
	    public void onMouseOut(MouseOutEvent event) {
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("stroke", new JSONString(INITIAL_STROKE));
		jSONObject.put("scale", new JSONNumber(INITIAL_ICON_SCALE));
		iconPath.animate(jSONObject, 100);
		circle.animate(jSONObject, 100);
	    }
	};
	circle.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
	iconPath.addDomHandler(mouseOutHandler, MouseOutEvent.getType());

	class ButtonDragCallback implements DragCallback {

	    private final RaphaelButton button;
	    // private double x;
	    // private double y;
	    private double dx;
	    private double dy;

	    ButtonDragCallback(RaphaelButton button) {
		this.button = button;
	    }

	    @Override
	    public void onStart(double x, double y) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onMove(double dx, double dy, double x, double y) {
		// this.x = x;
		// this.y = y;
		this.dx = dx;
		this.dy = dy;
	    }

	    @Override
	    public void onEnd() {
		// refuse drag if it is outside of the canvas
		int canvasWidth = LocalStorage.getInstance().getNavigation()
			.getOffsetWidth();
		int canvasHeight = LocalStorage.getInstance().getNavigation()
			.getOffsetHeight();
		// logger.log(Level.SEVERE, "canvasWidth: " + canvasWidth +
		// ", canvasHeight: " + canvasHeight);
		if ((button.positionX + dx < 0)
			|| (button.positionX + dx + 2 * getZoomRadius() > canvasWidth)
			|| (button.positionY + dy < 0)
			|| (button.positionY + dy + 2 * getZoomRadius() > canvasHeight)) {
		    return;
		}
		// refuse drag if buttons would overlap
		if (((button.connection1 == null) || (!button.connection1
			.buttonsTooClose(dx, dy, 1)))
			&& ((button.connection2 == null) || (!button.connection2
				.buttonsTooClose(dx, dy, -1)))) {
		    button.positionX += dx;
		    button.positionY += dy;
		    button.circle.translate(dx, dy);
		    button.iconPath.translate(dx, dy);

		    // logger.log(Level.SEVERE, "onEnd: " +
		    // button.getPositionXWithOffset() + ", " +
		    // button.getPositionYWithOffset());
		    if (button.connection1 != null) {
			button.connection1.redrawConnection();
		    }
		    if (button.connection2 != null) {
			button.connection2.redrawConnection();
		    }
		} else {
		    // TODO drag it as close as possible
		}
	    }
	}
	iconPath.drag(new ButtonDragCallback(this));
	// circle.drag(new ButtonDragCallback(this));
    }

    // public RapahaelButton getNeighbour1() {
    // return neighbour1;
    // }

    public void setNeighbour1(RaphaelButton neighbour1) {
	connection1 = new RaphaelConnection(this, neighbour1);
	neighbour1.setNeighbour2(this);
    }

    // public RapahaelButton getNeighbour2() {
    // return neighbour2;
    // }

    public void setNeighbour2(RaphaelButton neighbour2) {
	connection2 = neighbour2.connection1;
    }

    public double getPositionX() {
	return positionX;
    }

    public double getPositionY() {
	return positionY;
    }

    public void setPositionX(double positionX) {
	this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
	this.positionY = positionY;
    }

    public double getPositionXWithOffset() {
	return positionX + centerOffset;
    }

    public double getPositionYWithOffset() {
	return positionY + centerOffset;
    }

    public static Double getRadius() {
	return INITIAL_RADIUS * INITIAL_ICON_SCALE; // TODO consider zooming
    }

    public static Double getZoomRadius() {
	return INITIAL_RADIUS * ICON_ZOOM_IN_SCALE; // TODO consider zooming
    }

}