package org.bundolo.client.event.contest;

import com.google.gwt.event.shared.EventHandler;

public interface ContestUpdatedEventHandler extends EventHandler{
  void onContestUpdated(ContestUpdatedEvent event);
}
