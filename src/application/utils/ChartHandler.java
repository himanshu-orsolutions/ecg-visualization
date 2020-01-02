package application.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.models.Coordinate;
import application.models.ChartInfo;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;

/**
 * The ChartHandler. It holds implementation which operates on a line chart.
 */
public class ChartHandler {

	private double currentTimeSeries = 0;
	private static final int MAX_DATA_POINTS = 1000;
	private Series<Number, Number> series;
	private final ConcurrentLinkedQueue<Coordinate> dataQueue = new ConcurrentLinkedQueue<>();
	private NumberAxis xAxis;
	private NumberAxis yAxis;

	/**
	 * Initializes the graph handler
	 */
	public LineChart<Number, Number> init() {

		xAxis = new NumberAxis(0, MAX_DATA_POINTS, 1);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);
		yAxis = new NumberAxis(-10, 10, 1);
		yAxis.setAutoRanging(false);

		LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis) {
			@Override
			protected void dataItemAdded(final Series<Number, Number> series, final int itemIndex,
					final Data<Number, Number> item) {
			}
		};

		chart.setHorizontalGridLinesVisible(false);
		chart.setVerticalGridLinesVisible(false);
		chart.setVerticalZeroLineVisible(false);
		chart.setHorizontalZeroLineVisible(false);
		chart.setLegendVisible(false);
		chart.setAlternativeRowFillVisible(false);
		chart.setAlternativeColumnFillVisible(false);
		chart.setAnimated(false);

		series = new LineChart.Series<>();
		chart.getData().add(series);
		Dimension systemDimension = Toolkit.getDefaultToolkit().getScreenSize();

		chart.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.33);

		// Setting background image on line chart
		chart.getStylesheets().add("application/application.css");

		return chart;
	}

	/**
	 * Starts the view build
	 */
	public void start(ChartInfo graph) {

		ExecutorService taskExecutor = Executors.newCachedThreadPool();
		taskExecutor.execute(() -> {
			try {
				List<Coordinate> coordinates = graph.getCoordinates();
				for (Coordinate coordinate : coordinates) {
					dataQueue.add(coordinate);
					Thread.sleep(10);
				}
			} catch (final InterruptedException ex) {
			}
		});
		prepareTimeline();
	}

	/**
	 * Prepares the time line
	 */
	private void prepareTimeline() {

		new AnimationTimer() {
			@Override
			public void handle(final long now) {
				addDataToSeries();
			}
		}.start();
	}

	/**
	 * Adds new data to the series
	 */
	private void addDataToSeries() {

		for (int i = 0; i < 20; i++) {
			if (dataQueue.isEmpty()) {
				break;
			}

			final Coordinate datapoint = dataQueue.remove();
			currentTimeSeries = datapoint.getX();
			series.getData().add(new LineChart.Data<>(datapoint.getX(), datapoint.getY()));
		}

		// Shifting the time window
		if (series.getData().size() > (MAX_DATA_POINTS * 10)) {
			series.getData().remove(0, series.getData().size() - (MAX_DATA_POINTS * 10));
		}
		xAxis.setLowerBound(currentTimeSeries - (MAX_DATA_POINTS));
		xAxis.setUpperBound(currentTimeSeries - 1);
	}
}
