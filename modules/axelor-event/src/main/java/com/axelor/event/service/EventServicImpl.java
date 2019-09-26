package com.axelor.event.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventServicImpl implements EventService{

	@Override
	public boolean checkCapacity(Event event) {
		if(event.getTotalEntry() < event.getCapacity())
			return true;
		return false;
	}

	@Override
	public Event setEventSummary(Event event) {
		int totalEntry = event.getEventRegistrationList().size();
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<EventRegistration> registrations = event.getEventRegistrationList();
		for(EventRegistration reg : registrations) {
			totalAmount = totalAmount.add(reg.getAmount());
		}
		event.setTotalDiscount(event.getEventFees().multiply(new BigDecimal(totalEntry)).subtract(totalAmount));
		event.setTotalEntry(totalEntry);
		event.setAmountCollected(totalAmount);
		return event;
	}
}
