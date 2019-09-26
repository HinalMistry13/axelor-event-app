package com.axelor.event.repo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.axelor.event.db.Event;
import com.axelor.event.db.repo.EventRepository;

public class EventsRepository extends EventRepository{

	@Override
	public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
		if (!context.containsKey("json-enhance")) {
			return json;
		}
		try {
			Long id = (Long) json.get("id");
			Event event = find(id);
			DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");  
			json.put("startDt", dateFormat.format(event.getStartDate()));
			json.put("endDt", dateFormat.format(event.getEndDate()));
		} catch (Exception e) {
		}
		return json;
	}
}
