package org.openmrs.module.mycarehub.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.util.HandlerUtil;

import java.util.Iterator;
import java.util.List;

/**
 */
public class DataSynchronizer {
	
	private final Log log = LogFactory.getLog(DataSynchronizer.class);
	
	private static Boolean isRunning = false;
	
	public void processQueueData() {
		if (!isRunning) {
			synchronizeAllData();
		} else {
			log.info("Data synchronizer aborting (another synchronizer already running)!");
		}
	}
	
	private void synchronizeAllData() {
		try {
			isRunning = true;
			log.info("Firing up the Synchronizer processor ...");
		}
		finally {
			isRunning = false;
			log.info("Stopping up queue data processor ...");
		}
	}
}
