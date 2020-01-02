package application.models;

import java.util.List;

/**
 * The model ECGRecord. It contains all information of a ECG Record.
 */
public class ECGRecord {

	private List<ECGEvent> events;

	public List<ECGEvent> getEvents() {
		return events;
	}

	public void setEvents(List<ECGEvent> events) {
		this.events = events;
	}
}
