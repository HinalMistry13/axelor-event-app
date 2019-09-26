package com.axelor.event.web;

import java.math.BigDecimal;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {

	@Inject EventRegistrationService eventRegistrationService;
	
	public void getEventRegistrationAmount(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		Event event = request.getContext().getParent().asType(Event.class);
		BigDecimal amount = eventRegistrationService.getRegistrationAmount(eventRegistration, event);
		response.setValue("amount", amount);
	}
	
}
