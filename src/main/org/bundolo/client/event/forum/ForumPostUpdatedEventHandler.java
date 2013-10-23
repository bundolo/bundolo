package org.bundolo.client.event.forum;

import com.google.gwt.event.shared.EventHandler;

public interface ForumPostUpdatedEventHandler extends EventHandler{
  void onForumPostUpdated(ForumPostUpdatedEvent event);
}
