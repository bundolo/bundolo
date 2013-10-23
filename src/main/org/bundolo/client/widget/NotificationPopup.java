package org.bundolo.client.widget;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.notification.DisplayNotificationEventHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class NotificationPopup extends DecoratedPopupPanel {
	
	private static final Logger logger = Logger.getLogger(NotificationPopup.class.getName());
	
	private NotificationPopup popup;
	
	protected final int SHOW_TIMER_DELAY = 25; //ms 
    private Timer showTimer;

    protected final int HIDE_TIMER_DELAY = 25; //ms
    private Timer hideTimer;
    
    protected final int WAIT_TIMER_DELAY = 10000; //ms
    private Timer waitTimer;

    private static final BigDecimal DELTA = BigDecimal.valueOf(1, 1);
	
	public NotificationPopup(HandlerManager eventBus) {
		super(true, false);
		popup = this;
		this.showTimer = new Timer() {
            @Override
            public void run() {
                String opacityStr = popup.getElement().getStyle().getOpacity();
                boolean increased = false;
                if (!isBlank(opacityStr)) {
                    try {
                        BigDecimal opacity = new BigDecimal(opacityStr);
                        if (opacity.compareTo(new BigDecimal(1)) < 0) {
                             popup.getElement().getStyle().setOpacity(opacity.add(DELTA).doubleValue());
                             increased = true;
                         }
                     } catch (NumberFormatException nfe) {
 // fallback to showing
                     }
                 }
                 if (!increased) {
                     popup.getElement().getStyle().setOpacity(1);
                     cancel();
                     waitTimer.schedule(WAIT_TIMER_DELAY);
                 }
             }
         };
         this.hideTimer = new Timer() {
             @Override
             public void run() {
                 String opacityStr = popup.getElement().getStyle().getOpacity();
                 boolean decreased = false;
                 if (!isBlank(opacityStr)) {
                     try {
                         BigDecimal opacity = new BigDecimal(opacityStr);
                         if (opacity.compareTo(DELTA) > 0) {
                            popup.getElement().getStyle().setOpacity(opacity.subtract(DELTA).doubleValue());
                            decreased = true;
                        }
                    } catch (NumberFormatException nfe) {
 // fallback to hiding
                    }
                }
                if (!decreased) {
                    popup.hide();
                    cancel();
                }
            }
        };
        
        this.waitTimer = new Timer() {
        	@Override
            public void run() {
        		popup.hidePopup();
        	}
        };		
		
		eventBus.addHandler(DisplayNotificationEvent.TYPE, new DisplayNotificationEventHandler() {
			public void onDisplayNotification(DisplayNotificationEvent event) {
				logger.log(Level.FINE, "onDisplayNotification: " + event.getNotificationToDisplay());
				popup.setWidget(new HTML(event.getNotificationToDisplay()));
				//popup.center();
				popup.showPopup();
			}
		});
	}
	
	public void showPopup(){
        popup.getElement().getStyle().setOpacity(0);
        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = Window.getClientWidth() - offsetWidth - 10;
		        int top = Window.getClientHeight() - offsetHeight - 10;
		        popup.setPopupPosition(left, top);				
			}});
        //popup.show();
        showTimer.scheduleRepeating(SHOW_TIMER_DELAY);
    }

    public void hidePopup(){
        showTimer.cancel();
        hideTimer.scheduleRepeating(HIDE_TIMER_DELAY);
    }

	public static boolean isBlank(final String str) {
	    return (str == null) || (str.trim().length() == 0);
	}

}
