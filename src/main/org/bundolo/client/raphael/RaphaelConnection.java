package org.bundolo.client.raphael;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.hydro4ge.raphaelgwt.client.PathBuilder;
import com.hydro4ge.raphaelgwt.client.Point;
import com.hydro4ge.raphaelgwt.client.Raphael.Path;

public class RaphaelConnection {
	
	private Logger logger = Logger.getLogger(RaphaelConnection.class.getName());
	
	private final RaphaelButton button1;
	private final RaphaelButton button2;
	private Path connectionPath;
	private static final double RADIUS_SECTION = 20;
	private static final double CORNER_RADIUS_IN = 20;
	private static final double CORNER_RADIUS_OUT = 40;
	private static final double CONNECTION_WIDTH = 10;	
	private static final double CORNER_LENGTH = 7 * CONNECTION_WIDTH;
	private final JSONObject initialAttrs;
	
	public RaphaelConnection(RaphaelButton button1, RaphaelButton button2) {
		this.button1 = button1;
		this.button2 = button2;
		initialAttrs = new JSONObject();
		initialAttrs.put("fill", new JSONString("#000"));
		initialAttrs.put("stroke", new JSONString("#666"));
		initialAttrs.put("stroke-width", new JSONString("3"));
//		initialAttrs.put("arrow-end", new JSONString("oval"));
		redrawConnection();
	}

	public void redrawConnection() {
		if (connectionPath == null) {
			PathBuilder pathBuilder = new PathBuilder();
			pathBuilder.append(connectionPathString());
			connectionPath = LocalStorage.getInstance().getNavigation().new Path(pathBuilder);
			connectionPath.attr(initialAttrs);
		} else {
			connectionPath.attr("path", connectionPathString());
		}
	}
	
	private String connectionPathString() {
		String result;
		double x1 = button1.getPositionXWithOffset();
		double y1 = button1.getPositionYWithOffset();
		double x2 = button2.getPositionXWithOffset();
		double y2 = button2.getPositionYWithOffset();
		double r = button1.getRadius();
		double factorX = (x1 > x2)?-1:1;
		double factorY = (y1 > y2)?-1:1;
		double arcFactor = - factorX*factorY * 0.5 + 0.5;
		
		double x11;
		double y11;
		double x12;
		double y12;
		double x21;
		double y21;
		double x22;
		double y22;
		double x13;
		double y13;
		double x23;
		double y23;
		if (Math.abs(y1 - y2) < 2 * button1.getZoomRadius()) {
			x11 = x1 + CONNECTION_WIDTH * factorX;
			y11 = y1 - r * factorY;
			x12 = x1 - CONNECTION_WIDTH * factorX;
			y12 = y1 - r * factorY;
			x21 = x2 - CONNECTION_WIDTH * factorX;
			y21 = y2 - r * factorY;
			x22 = x2 + CONNECTION_WIDTH * factorX;
			y22 = y2 - r * factorY;
			double factorY3 = (y1 > y11)?-1:1;
			y13 = factorY == 1 ? y2 + r * factorY3 + CORNER_LENGTH * factorY3: y1 + r * factorY3 + CORNER_LENGTH * factorY3;
			y23 = y13 + 2 * CONNECTION_WIDTH * factorY3;
			result = "M"+x11+","+y11+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+arcFactor+","+x12+","+y12+
					"L"+x12+","+(y23 + factorY * CORNER_RADIUS_OUT)+"Q,"+x12+","+y23+","+(x12 + factorX * CORNER_RADIUS_OUT)+","+y23+
					"L"+(x22 - factorX * CORNER_RADIUS_OUT)+","+y23+"Q,"+x22+","+y23+","+x22+","+(y23 + factorY * CORNER_RADIUS_OUT)+
					"L"+x22+","+y22+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+arcFactor+","+x21+","+y21;					
			if (Math.abs(x1 - x2) < 2 * CORNER_RADIUS_IN + r) {
				result += "L"+x21+","+y13+
						"L"+x11+","+y13;
//				result += "L"+x21+","+(y13 + factorY * CORNER_RADIUS_IN)+"Q,"+x21+","+y13+","+(x21 - factorX * Math.abs(x1 - x2)/2)+","+y13+
//						"L"+(x11 + factorX * Math.abs(x1 - x2)/2)+","+y13+"Q,"+x11+","+y13+","+x11+","+(y13 + factorY * CORNER_RADIUS_IN);
			} else {
				result += "L"+x21+","+(y13 + factorY * CORNER_RADIUS_IN)+"Q,"+x21+","+y13+","+(x21 - factorX * CORNER_RADIUS_IN)+","+y13+
				"L"+(x11 + factorX * CORNER_RADIUS_IN)+","+y13+"Q,"+x11+","+y13+","+x11+","+(y13 + factorY * CORNER_RADIUS_IN);
			}
			result += "z";
		} else if (Math.abs(x1 - x2) < 2 * button1.getZoomRadius()) {
			x11 = x1 - r * factorX;
			y11 = y1 + CONNECTION_WIDTH * factorY;
			x12 = x1 - r * factorX;
			y12 = y1 - CONNECTION_WIDTH * factorY;
			x21 = x2 - r * factorX;
			y21 = y2 - CONNECTION_WIDTH * factorY;
			x22 = x2 - r * factorX;
			y22 = y2 + CONNECTION_WIDTH * factorY;
			double factorX3 = (x1 > x11)?-1:1;
			x13 = factorX == 1 ? x2 + r * factorX3 + CORNER_LENGTH * factorX3 : x1 + r * factorX3 + CORNER_LENGTH * factorX3;
			x23 = x13 + 2 * CONNECTION_WIDTH * factorX3;
			result = "M"+x11+","+y11+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+(1-arcFactor)+","+x12+","+y12+
					"L"+(x23 + factorX * CORNER_RADIUS_OUT)+","+y12+"Q,"+x23+","+y12+","+x23+","+(y12 + factorY * CORNER_RADIUS_OUT)+
					"L"+x23+","+(y22 - factorY * CORNER_RADIUS_OUT)+"Q,"+x23+","+y22+","+(x23 + factorX * CORNER_RADIUS_OUT)+","+y22+
					"L"+x22+","+y22+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+(1-arcFactor)+","+x21+","+y21;
					if (Math.abs(y1 - y2) < 2 * CORNER_RADIUS_IN + r) {
						result += "L"+x13+","+y21+
								"L"+x13+","+y11;
					} else {
						result += "L"+(x13 + factorX * CORNER_RADIUS_IN)+","+y21+"Q,"+x13+","+y21+","+x13+","+(y21 - factorY * CORNER_RADIUS_IN)+
								"L"+x13+","+(y11 + factorY * CORNER_RADIUS_IN)+"Q,"+x13+","+y11+","+(x13 + factorX * CORNER_RADIUS_IN)+","+y11;
					}
					
			result += "z";
		} else {
			x11 = x1 + r * factorX;
			y11 = y1 + CONNECTION_WIDTH * factorY;
			x12 = x1 + r * factorX;
			y12 = y1 - CONNECTION_WIDTH * factorY;
			x21 = x2 - CONNECTION_WIDTH * factorX;
			y21 = y2 - r * factorY;
			x22 = x2 + CONNECTION_WIDTH * factorX;
			y22 = y2 - r * factorY;
			result = "M"+x11+","+y11+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+arcFactor+","+x12+","+y12+
					"L"+(x22 - factorX * CORNER_RADIUS_OUT)+","+y12+"Q,"+x22+","+y12+","+x22+","+(y12 + factorY * CORNER_RADIUS_OUT)+
					"L"+x22+","+y22+
					"A,"+RADIUS_SECTION+","+RADIUS_SECTION+",0,0,"+arcFactor+","+x21+","+y21+
					"L"+x21+","+(y11 + factorY * CORNER_RADIUS_IN)+"Q,"+x21+","+y11+","+(x21 - factorX * CORNER_RADIUS_IN)+","+y11+
					"z";
		}
//		double factorXY = (Math.abs(y1 - y2) > Math.abs(x1 - x2))?-1:1;
//		
//		double x11 = x1 + r * factorX;
//		double y11 = y1 + CONNECTION_WIDTH * factorY;
//		double x12 = x1 + r * factorX;
//		double y12 = y1 - CONNECTION_WIDTH * factorY;
//		double x21 = x2 - CONNECTION_WIDTH * factorX;
//		double y21 = y2 - r * factorY;
//		double x22 = x2 + CONNECTION_WIDTH * factorX;
//		double y22 = y2 - r * factorY;
				
		
		//arcFactor = 0;
//		logger.log(Level.SEVERE, "arcFactor: " + arcFactor);
		
//		logger.log(Level.SEVERE, "factorX: " + factorX + ", factorY: " + factorY + ", u1: " + u1 + ", v1: " + v1 + ", x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2);
//		return "M"+x1+","+y1+"L"+x2+","+y2;
		
//		return "M"+x11+","+y11+"L"+x12+","+y12+"L"+x22+","+y22+"L"+x21+","+y21+"z";
		return result;
	}

	public RaphaelButton getButton1() {
		return button1;
	}

	public RaphaelButton getButton2() {
		return button2;
	}
	
	public boolean buttonsTooClose(double dx, double dy, int factor) {
		return Math.pow(button1.getPositionXWithOffset() + factor * dx - button2.getPositionXWithOffset(), 2) + Math.pow(button1.getPositionYWithOffset() + factor * dy - button2.getPositionYWithOffset(), 2) < Math.pow(button1.getZoomRadius() + button2.getZoomRadius(), 2);
	}

}