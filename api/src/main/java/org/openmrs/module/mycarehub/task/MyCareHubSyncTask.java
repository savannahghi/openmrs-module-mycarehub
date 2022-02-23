package org.openmrs.module.mycarehub.task;

import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class MyCareHubSyncTask extends AbstractTask {
	
	private final DataSynchronizer processor;
	
	public MyCareHubSyncTask() {
		this.processor = new DataSynchronizer();
	}
	
	/**
	 * @see org.openmrs.scheduler.Task#execute()
	 */
	@Override
	public void execute() {
		Context.openSession();
		processor.processQueueData();
		Context.closeSession();
	}
}
