package application.models;

/**
 * The model ECGEvent. It holds information of an ECG event.
 */
public class ECGEvent {

	private Long id;
	private String title;
	private Double startTime;
	private Double endTime;
	private Double assignedDuration;
	private Double calculatedDuration;
	private Amplitude amplitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getStartTime() {
		return startTime;
	}

	public void setStartTime(Double startTime) {
		this.startTime = startTime;
	}

	public Double getEndTime() {
		return endTime;
	}

	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}

	public Double getAssignedDuration() {
		return assignedDuration;
	}

	public void setAssignedDuration(Double assignedDuration) {
		this.assignedDuration = assignedDuration;
	}

	public Double getCalculatedDuration() {
		return calculatedDuration;
	}

	public void setCalculatedDuration(Double calculatedDuration) {
		this.calculatedDuration = calculatedDuration;
	}

	public Amplitude getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(Amplitude amplitude) {
		this.amplitude = amplitude;
	}
}
