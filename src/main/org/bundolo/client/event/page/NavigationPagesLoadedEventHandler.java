package org.bundolo.client.event.page;

import com.google.gwt.event.shared.EventHandler;

public interface NavigationPagesLoadedEventHandler extends EventHandler {
  void onNavigationPagesLoaded(NavigationPagesLoadedEvent event);
}
