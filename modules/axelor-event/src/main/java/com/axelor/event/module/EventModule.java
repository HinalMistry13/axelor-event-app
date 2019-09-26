package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.repo.EventsRepository;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventRegistrationServiceImpl;
import com.axelor.event.service.EventServicImpl;
import com.axelor.event.service.EventService;

public class EventModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(EventRepository.class).to(EventsRepository.class);
		bind(EventService.class).to(EventServicImpl.class);
		bind(EventRegistrationService.class).to(EventRegistrationServiceImpl.class);
	}
}
