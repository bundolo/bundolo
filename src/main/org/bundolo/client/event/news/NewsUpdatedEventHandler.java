package org.bundolo.client.event.news;

import com.google.gwt.event.shared.EventHandler;

public interface NewsUpdatedEventHandler extends EventHandler{
  void onNewsUpdated(NewsUpdatedEvent event);
}
