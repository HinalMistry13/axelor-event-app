package com.axelor.event.service;

import com.axelor.event.db.Event;

public interface EventService {

	public boolean checkCapacity(Event event);
	public Event setEventSummary(Event event);
}
