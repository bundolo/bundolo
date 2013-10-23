package org.bundolo.client.event.news;

import com.google.gwt.event.shared.EventHandler;

public interface AddNewsEventHandler extends EventHandler {
  void onAddNews(AddNewsEvent event);
}
