package org.bundolo.client.event.page;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class NavigationPagesLoadedEvent extends GwtEvent<NavigationPagesLoadedEventHandler> {
	public static Type<NavigationPagesLoadedEventHandler> TYPE = new Type<NavigationPagesLoadedEventHandler>();
	private final List<Object> navigationPages;

	public NavigationPagesLoadedEvent(List<Object> navigationPages) {
		this.navigationPages = navigationPages;
	}

	public List<Object> getNavigationPages() {
		return navigationPages;
	}

	@Override
	public Type<NavigationPagesLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NavigationPagesLoadedEventHandler handler) {
		handler.onNavigationPagesLoaded(this);
	}
}
