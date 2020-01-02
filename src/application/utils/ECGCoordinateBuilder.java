package application.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.models.Coordinate;
import application.models.ECGEvent;
import application.models.ECGRecord;
import application.models.ChartInfo;

public class ECGCoordinateBuilder {

	/**
	 * The current X position
	 */
	private double currentX = 0d;

	/**
	 * Gets the line co-ordinates between two points
	 * 
	 * @param start The start
	 * @param end   The end
	 * @return The co-ordinates
	 */
	private List<Coordinate> getLineCoordinates(Coordinate start, Coordinate end) {

		List<Coordinate> coordinates = new ArrayList<>();
		double x1 = start.getX();
		double y1 = start.getY();
		double x2 = end.getX();
		double y2 = end.getY();

		if (x1 >= currentX) {
			double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2));
			double alpha = (y2 - y1) / length;

			for (double i = x1; i <= x2; i++) {
				coordinates.add(new Coordinate(i, y1 + alpha));
				y1 += alpha;
			}
			currentX = x2;
		}

		return coordinates;
	}

	/**
	 * Gets the two parameters(a and b) required in the equation of curve i.e. y =
	 * ax^2 + bx
	 * 
	 * @param start The start point
	 * @param end   The end point
	 * @return The curve parameters
	 */
	private List<Double> getEquationParams(Coordinate start, Coordinate end) {

		if (start.getX() == 0d) {
			start.setX(1d);
		}

		double y1 = start.getY();
		double y2 = end.getY();

		double coef1a = Math.pow(start.getX(), 2);
		double coef2a = Math.pow(end.getX(), 2);
		double coef1b = start.getX();
		double coef2b = end.getX();
		double multiplier = coef1a / coef2a;

		double updatedY2 = y2 * multiplier;
		double updatedCoef2b = coef2b * multiplier;
		double b = (y1 - updatedY2) / (coef1b - updatedCoef2b);
		double a = (y1 - (b * coef1b)) / coef1a;

		return Arrays.asList(a, b);
	}

	/**
	 * Gets the curve co-ordinates between two points
	 * 
	 * @param start The start point
	 * @param end   The end point
	 * @return The co-ordinates
	 */
	private List<Coordinate> getCurveCoordinates(Coordinate start, Coordinate end) {

		List<Coordinate> coordinates = new ArrayList<>();
		double x1 = start.getX();
		double x2 = end.getX();
		double diff = x2 - x1;
		double count = 0;
		List<Double> curveParams = getEquationParams(new Coordinate(0, start.getY()), new Coordinate(diff, end.getY()));

		if (x1 >= currentX) {
			for (double i = x1; count < diff; i++, count++) {
				coordinates.add(new Coordinate(i,
						((curveParams.get(0) * Math.pow(count, 2)) + (curveParams.get(1) * count)) / diff));
			}
			currentX = x2;
		}

		return coordinates;
	}

	/**
	 * Builds the graph parameters
	 * 
	 * @param record The ECG record
	 * @return The graph
	 */
	public ChartInfo build(ECGRecord record, String type) {

		List<Coordinate> coordinates = new ArrayList<>();
		List<ECGEvent> events = record.getEvents();

		if (!events.isEmpty()) {
			double startX = events.get(0).getStartTime();
			for (ECGEvent event : events) {
				double diff = event.getEndTime() - event.getStartTime();
				double value = 0d;

				switch (type) {
				case "I":
					value = event.getAmplitude().getI();
					break;
				case "aVR":
					value = event.getAmplitude().getaVR();
					break;
				case "v1":
					value = event.getAmplitude().getV1();
					break;
				case "v4":
					value = event.getAmplitude().getV4();
					break;
				case "II":
					value = event.getAmplitude().getIi();
					break;
				case "aVL":
					value = event.getAmplitude().getaVL();
					break;
				case "v2":
					value = event.getAmplitude().getV2();
					break;
				case "v5":
					value = event.getAmplitude().getV5();
					break;
				case "III":
					value = event.getAmplitude().getIii();
					break;
				case "aVF":
					value = event.getAmplitude().getaVF();
					break;
				case "v3":
					value = event.getAmplitude().getV3();
					break;
				case "v6":
					value = event.getAmplitude().getV6();
					break;
				}

				if (diff < 30) { // Steep plot
					coordinates.addAll(getLineCoordinates(new Coordinate(event.getStartTime() - startX, 0d),
							new Coordinate(
									event.getStartTime() - startX + ((event.getEndTime() - event.getStartTime()) / 2),
									value)));
					coordinates
							.addAll(getLineCoordinates(
									new Coordinate(event.getStartTime() - startX
											+ ((event.getEndTime() - event.getStartTime()) / 2), value),
									new Coordinate(event.getEndTime() - startX, 0d)));
				} else { // Curve plot
					coordinates.addAll(getCurveCoordinates(new Coordinate(event.getStartTime() - startX, 0d),
							new Coordinate(
									event.getStartTime() - startX + ((event.getEndTime() - event.getStartTime()) / 2),
									value)));
					coordinates
							.addAll(getCurveCoordinates(
									new Coordinate(event.getStartTime() - startX
											+ ((event.getEndTime() - event.getStartTime()) / 2), value),
									new Coordinate(event.getEndTime() - startX, 0d)));
				}
			}
		}
		ChartInfo graph = new ChartInfo();
		graph.setCoordinates(coordinates);
		return graph;
	}
}
