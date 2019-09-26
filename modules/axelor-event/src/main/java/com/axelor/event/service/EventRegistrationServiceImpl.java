package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;

public class EventRegistrationServiceImpl implements EventRegistrationService{

	@Override
	public boolean checkRegistartionDate(EventRegistration eventRegistration) {
		Event event = eventRegistration.getEvent();
		LocalDate regDate = eventRegistration.getRegDate(); 
		if(event != null && event.getRegOpenDate() != null && event.getRegCloseDate() != null && regDate !=null) {
			if(regDate.compareTo(event.getRegOpenDate()) > 0 && regDate.compareTo(event.getRegCloseDate()) < 0)
				return true;
		}		
		return false;
	}

	@Override
	public BigDecimal getRegistrationAmount(EventRegistration eventRegistration, Event event) {
		BigDecimal amount = BigDecimal.ZERO;
		long daysBetween = ChronoUnit.DAYS.between(eventRegistration.getRegDate(),event.getRegCloseDate());
		List<Discount> discounts = event.getDiscountList();
		for(Discount dis : discounts) {
			if(dis.getBeforeDays() == daysBetween)
				amount = event.getEventFees().subtract(dis.getDiscountAmount());
		}
		if(amount== BigDecimal.ZERO)
			amount = event.getEventFees();
		return amount;
	}
	
	@Override
	public void importEventRegistrations(MetaFile dataFile,Integer id) {
		try {
			File configXmlFile = this.getConfigXmlFile();
			File dataCsvFile = this.getDataCsvFile(dataFile);

			CSVImporter importer = new CSVImporter(configXmlFile.getAbsolutePath(),"/home/axelor/hinal/Event/axelor-event-app/modules/axelor-event/src/main/resources/import-configs/input/");
			
			final Map<String, Object> context = new HashMap<>();
	        context.put("event",id);
	        importer.setContext(context);
			importer.addListener(new Listener() {
				@Override
				public void imported(Integer total, Integer success) {
					// TODO Auto-generated method stub
					System.out.println("Total Records : " + total);
					System.out.println("Success Records : " + success);
				}
				@Override
				public void imported(Model bean) {
				}
				@Override
				public void handle(Model bean, Exception e) {			
				}
			});
			importer.run();
			this.deleteTempFiles(configXmlFile, dataCsvFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getConfigXmlFile() {

		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/import-configs/event-registration-config.xml");
			if (bindFileInputStream != null) {
				FileOutputStream outputStream = new FileOutputStream(configFile);
				IOUtils.copy(bindFileInputStream, outputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	private File getDataCsvFile(MetaFile dataFile) {

		File csvFile = null;
		try {
			File tempDir = Files.createTempDir();
			csvFile = new File(tempDir, "event_registration.csv");
			Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvFile;
	}

	private void deleteTempFiles(File configXmlFile, File dataCsvFile) {
		try {
			if (configXmlFile.isDirectory() && dataCsvFile.isDirectory()) {
				FileUtils.deleteDirectory(configXmlFile);
				FileUtils.deleteDirectory(dataCsvFile);
			} else {
				configXmlFile.delete();
				dataCsvFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
