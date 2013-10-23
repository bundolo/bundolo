package org.bundolo.client.event.notification;

import com.google.gwt.event.shared.GwtEvent;

public class DisplayNotificationEvent extends GwtEvent<DisplayNotificationEventHandler>{
	public static Type<DisplayNotificationEventHandler> TYPE = new Type<DisplayNotificationEventHandler>();
	private final String notificationToDisplay;

	public DisplayNotificationEvent(String notificationToDisplay) {
		this.notificationToDisplay = notificationToDisplay;
	}

	public String getNotificationToDisplay() {
		return notificationToDisplay;
	}

	@Override
	public Type<DisplayNotificationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DisplayNotificationEventHandler handler) {
		handler.onDisplayNotification(this);
	}
}