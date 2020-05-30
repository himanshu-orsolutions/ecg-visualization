package application.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.models.Amplitude;
import application.models.ECGEvent;
import application.models.ECGRecord;

/**
 * The utility class ECGReportParser. It holds implementation to parse the ECG
 * report.
 */
public class ECGReportParser {

	private ECGReportParser() {
		// The utility class
	}

	/**
	 * The ECG event pattern
	 */
	private static final Pattern ECG_EVENT_PATTERN = Pattern.compile(
			"^\\[\\{id\\=(\\d+)\\,title\\=([\\w ]+)\\,start_time\\=(\\-?\\d+(\\.\\d+)?)\\,end_time\\=(\\-?\\d+(\\.\\d+)?)\\,assigned_duration\\=(\\-?\\d+(\\.\\d+)?)\\,calculated_duration\\=(\\-?\\d+(\\.\\d+)?)\\,amplitude\\=\\{I\\=(\\-?\\d+(\\.\\d+)?)\\,II\\=(\\-?\\d+(\\.\\d+)?)\\,III\\=(\\-?\\d+(\\.\\d+)?)\\,aVR\\=(\\-?\\d+(\\.\\d+)?)\\,aVL\\=(\\-?\\d+(\\.\\d+)?)\\,aVF\\=(\\-?\\d+(\\.\\d+)?)\\,V1\\=(\\-?\\d+(\\.\\d+)?)\\,V2\\=(\\-?\\d+(\\.\\d+)?)\\,V3\\=(\\-?\\d+(\\.\\d+)?)\\,V4\\=(\\-?\\d+(\\.\\d+)?)\\,V5\\=(\\-?\\d+(\\.\\d+)?)\\,V6\\=(\\-?\\d+(\\.\\d+)?)\\}\\}\\]$");

	/**
	 * Parses the ECG event from the event string
	 * 
	 * @param event The event string
	 * @return The ECG event
	 */
	private static ECGEvent parseECGEvent(String event) {

		event = event.replace("~", "-");
		Matcher eventMatcher = ECG_EVENT_PATTERN.matcher(event);
		if (eventMatcher.find()) {
			ECGEvent ecgEvent = new ECGEvent();
			ecgEvent.setId(Long.parseLong(eventMatcher.group(1)));
			ecgEvent.setTitle(eventMatcher.group(2));
			ecgEvent.setStartTime(Double.parseDouble(eventMatcher.group(3)));
			ecgEvent.setEndTime(Double.parseDouble(eventMatcher.group(5)));
			ecgEvent.setAssignedDuration(Double.parseDouble(eventMatcher.group(7)));
			ecgEvent.setCalculatedDuration(Double.parseDouble(eventMatcher.group(9)));

			Amplitude amplitude = new Amplitude();
			amplitude.setI(Double.parseDouble(eventMatcher.group(11)));
			amplitude.setIi(Double.parseDouble(eventMatcher.group(13)));
			amplitude.setIii(Double.parseDouble(eventMatcher.group(15)));
			amplitude.setaVR(Double.parseDouble(eventMatcher.group(17)));
			amplitude.setaVL(Double.parseDouble(eventMatcher.group(19)));
			amplitude.setaVF(Double.parseDouble(eventMatcher.group(21)));
			amplitude.setV1(Double.parseDouble(eventMatcher.group(23)));
			amplitude.setV2(Double.parseDouble(eventMatcher.group(25)));
			amplitude.setV3(Double.parseDouble(eventMatcher.group(27)));
			amplitude.setV4(Double.parseDouble(eventMatcher.group(29)));
			amplitude.setV5(Double.parseDouble(eventMatcher.group(31)));
			amplitude.setV6(Double.parseDouble(eventMatcher.group(33)));
			ecgEvent.setAmplitude(amplitude);

			return ecgEvent;
		}
		return null;
	}

	/**
	 * Parses the ECG record present at the specified location
	 * 
	 * @param recordPath The path of record
	 * @return The list of ECG events
	 */
	public static ECGRecord parseECGRecord(Path recordPath) throws IOException {

		ECGRecord ecgRecord = new ECGRecord();

		if (recordPath.toFile().exists()) {
			List<String> lines = Files.readAllLines(recordPath);
			List<ECGEvent> events = new ArrayList<>();
			lines.forEach(line -> {
				ECGEvent event = parseECGEvent(line);
				if (event != null) {
					events.add(event);
				}
			});

			ecgRecord.setEvents(events);
			return ecgRecord;
		}
		return ecgRecord;
	}
}
