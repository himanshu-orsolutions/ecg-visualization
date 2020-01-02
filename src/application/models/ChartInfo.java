package application.models;

import java.util.List;

/**
 * The model ChartInfo. It holds all information of a line chart.
 */
public class ChartInfo {

	private List<Coordinate> coordinates;

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
