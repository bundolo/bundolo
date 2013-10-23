package org.bundolo.client.event.page;

import com.google.gwt.event.shared.EventHandler;

public interface PageUpdatedEventHandler extends EventHandler{
  void onPageUpdated(PageUpdatedEvent event);
}
