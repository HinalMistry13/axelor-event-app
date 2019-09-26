package com.axelor.event.csv;

import java.util.Map;

import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventRegistrationService;
import com.google.inject.Inject;

public class ImportEventRegistration {

	@Inject
	EventRegistrationService eventRegistrationService;
	
	public Object importRegistrationData(Object bean, Map<String, Object> values){
		assert bean instanceof EventRegistration;
		EventRegistration registration = (EventRegistration) bean;
		registration.setAmount(eventRegistrationService.getRegistrationAmount(registration, registration.getEvent()));
		return registration;
	}
}
