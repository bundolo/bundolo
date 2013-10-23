package org.bundolo.client.event.comment;

import com.google.gwt.event.shared.EventHandler;

public interface AddCommentEventHandler extends EventHandler {
  void onAddComment(AddCommentEvent event);
}
