package org.bundolo.client.event.serial;

import com.google.gwt.event.shared.EventHandler;

public interface EpisodeUpdatedEventHandler extends EventHandler{
  void onEpisodeUpdated(EpisodeUpdatedEvent event);
}
