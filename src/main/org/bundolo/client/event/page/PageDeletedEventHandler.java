package org.bundolo.client.event.page;

import com.google.gwt.event.shared.EventHandler;

public interface PageDeletedEventHandler extends EventHandler {
  void onPageDeleted(PageDeletedEvent event);
}
