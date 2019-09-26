package com.axelor.event.service;

import java.math.BigDecimal;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.meta.db.MetaFile;

public interface EventRegistrationService {
	
	public boolean checkRegistartionDate(EventRegistration eventRegistration);
	public BigDecimal getRegistrationAmount(EventRegistration eventRegistration,Event event);
	public void importEventRegistrations(MetaFile dataFile,Integer id);
}
