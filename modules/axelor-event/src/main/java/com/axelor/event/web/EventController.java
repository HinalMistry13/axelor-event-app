package com.axelor.event.web;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventService;
import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventController {

	@Inject
	EventService eventService;
	
	@Inject
	EventRegistrationService eventRegistrationService;
	
	public void onEventSaveValidation(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		if(event.getEventRegistrationList()!=null) {
			if(!eventService.checkCapacity(event))
				response.setError(I18n.get(IExceptionMessage.REGISTRATION_EXCEEDS_CAPACITY));
			List<EventRegistration> registrations = event.getEventRegistrationList();
			for(EventRegistration reg : registrations) {
				if(!eventRegistrationService.checkRegistartionDate(reg))
					response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATA_INVALID));
			}
			event = eventService.setEventSummary(event);
		}
		response.setValue("capacity", event.getCapacity());
		response.setValue("reference",event.getReference());
		response.setValue("venue", event.getVenue());
		response.setValue("startDate", event.getStartDate());
		response.setValue("endDate", event.getEndDate());
		response.setValue("regOpenDate", event.getRegOpenDate());
		response.setValue("regCloseDate", event.getRegCloseDate());
		response.setValue("eventFees", event.getEventFees());
		response.setValue("discountList", event.getDiscountList());
		response.setValue("eventRegistrationList", event.getEventRegistrationList());
		response.setValue("description", event.getDescription());
		response.setValue("totalDiscount",event.getTotalDiscount());
		response.setValue("totalEntry", event.getTotalEntry());
		response.setValue("amountCollected", event.getAmountCollected());
	}
	
	public void getEventSummary(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		event = eventService.setEventSummary(event);
		response.setValue("totalDiscount",event.getTotalDiscount());
		response.setValue("totalEntry", event.getTotalEntry());
		response.setValue("amountCollected", event.getAmountCollected());
	}
	
	public void importEventRegistrations(ActionRequest request, ActionResponse response) {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
	    MetaFile dataFile = Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
	    Integer id = (Integer)request.getContext().get("_id");
	    try {
	      eventRegistrationService.importEventRegistrations(dataFile, id);
	      File readFile = MetaFiles.getPath("").toFile();
	      response.setNotify(FileUtils.readFileToString(readFile, StandardCharsets.UTF_8).replaceAll("(\r\n|\n\r|\r|\n)", "<br />"));
	    } catch (Exception e) {
	    }
	}
}
