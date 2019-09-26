package com.axelor.event.csv;

import java.util.Map;

import com.axelor.event.db.Event;
import com.axelor.event.service.EventService;
import com.google.inject.Inject;

public class ImportEvent {

	@Inject
	EventService eventService;	
	
	public Object importEvent(Object bean, Map<String, Object> values){
		assert bean instanceof Event;
		Event event = (Event) bean;
		event = eventService.setEventSummary(event);
		return event;
	}
}
