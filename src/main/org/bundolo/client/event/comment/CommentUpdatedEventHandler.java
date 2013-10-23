package org.bundolo.client.event.comment;

import com.google.gwt.event.shared.EventHandler;

public interface CommentUpdatedEventHandler extends EventHandler{
  void onCommentUpdated(CommentUpdatedEvent event);
}
