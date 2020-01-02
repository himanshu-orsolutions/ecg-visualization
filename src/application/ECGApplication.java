package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.models.ECGRecord;
import application.models.ChartInfo;
import application.utils.ECGCoordinateBuilder;
import application.utils.ECGReportParser;
import application.utils.ChartHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The ECGApplication
 */
public class ECGApplication extends Application {

	/**
	 * The file chooser
	 */
	private static final FileChooser FILE_CHOOSER = new FileChooser();

	/**
	 * The chart handlers
	 */
	ChartHandler iGraphHandler = new ChartHandler();
	ChartHandler aVRGraphHandler = new ChartHandler();
	ChartHandler v1GraphHandler = new ChartHandler();
	ChartHandler v4GraphHandler = new ChartHandler();
	ChartHandler iiGraphHandler = new ChartHandler();
	ChartHandler aVLGraphHandler = new ChartHandler();
	ChartHandler v2GraphHandler = new ChartHandler();
	ChartHandler v5GraphHandler = new ChartHandler();
	ChartHandler iiiGraphHandler = new ChartHandler();
	ChartHandler aVFGraphHandler = new ChartHandler();
	ChartHandler v3GraphHandler = new ChartHandler();
	ChartHandler v6GraphHandler = new ChartHandler();

	/**
	 * The GUI components
	 */
	BorderPane borderPane;
	VBox container;
	HBox fileContainer;
	HBox group1Container;
	HBox group2Container;
	HBox group3Container;
	VBox iChartContainer;
	VBox aVRChartContainer;
	VBox v1ChartContainer;
	VBox v4ChartContainer;
	VBox iiChartContainer;
	VBox aVLChartContainer;
	VBox v2ChartContainer;
	VBox v5ChartContainer;
	VBox iiiChartContainer;
	VBox aVFChartContainer;
	VBox v3ChartContainer;
	VBox v6ChartContainer;
	TextField filePathField;
	Button chooseFileButton;
	LineChart<Number, Number> iLineChart;
	LineChart<Number, Number> aVRLineChart;
	LineChart<Number, Number> v1LineChart;
	LineChart<Number, Number> v4LineChart;
	LineChart<Number, Number> iiLineChart;
	LineChart<Number, Number> aVLLineChart;
	LineChart<Number, Number> v2LineChart;
	LineChart<Number, Number> v5LineChart;
	LineChart<Number, Number> iiiLineChart;
	LineChart<Number, Number> aVFLineChart;
	LineChart<Number, Number> v3LineChart;
	LineChart<Number, Number> v6LineChart;
	Label iChartLabel;
	Label aVRChartLabel;
	Label v1ChartLabel;
	Label v4ChartLabel;
	Label iiChartLabel;
	Label aVLChartLabel;
	Label v2ChartLabel;
	Label v5ChartLabel;
	Label iiiChartLabel;
	Label aVFChartLabel;
	Label v3ChartLabel;
	Label v6ChartLabel;

	/**
	 * Shows the error message on pop-up
	 * 
	 * @param errorMessage The error message
	 */
	private void showErrorMessage(String errorMessage) {

		new Alert(AlertType.ERROR, errorMessage, ButtonType.CLOSE).show();
	}

	/**
	 * Initiates the application view
	 */
	@Override
	public void start(Stage primaryStage) {

		try {
			Dimension systemDimension = Toolkit.getDefaultToolkit().getScreenSize();

			iLineChart = iGraphHandler.init();
			aVRLineChart = aVRGraphHandler.init();
			v1LineChart = v1GraphHandler.init();
			v4LineChart = v4GraphHandler.init();
			iiLineChart = iiGraphHandler.init();
			aVLLineChart = aVLGraphHandler.init();
			v2LineChart = v2GraphHandler.init();
			v5LineChart = v5GraphHandler.init();
			iiiLineChart = iiiGraphHandler.init();
			aVFLineChart = aVFGraphHandler.init();
			v3LineChart = v3GraphHandler.init();
			v6LineChart = v6GraphHandler.init();

			filePathField = new TextField();
			filePathField.setPrefSize(systemDimension.getWidth() * 0.9, 16);
			filePathField.setEditable(false);

			chooseFileButton = new Button("choose file");
			chooseFileButton.setPrefSize(systemDimension.getWidth() * 0.1, 16);

			chooseFileButton.setOnAction((event) -> {
				File selectedFile = FILE_CHOOSER.showOpenDialog(null);
				filePathField.setText(selectedFile.getPath());
				try {
					ECGRecord record = ECGReportParser.parseECGRecord(Paths.get(selectedFile.getPath()));
					chooseFileButton.setDisable(true);
					ExecutorService taskExecutor = Executors.newFixedThreadPool(12);

					ChartInfo iGraph = new ECGCoordinateBuilder().build(record, "I");
					ChartInfo aVRGraph = new ECGCoordinateBuilder().build(record, "aVR");
					ChartInfo v1Graph = new ECGCoordinateBuilder().build(record, "v1");
					ChartInfo v4Graph = new ECGCoordinateBuilder().build(record, "v4");
					ChartInfo iiGraph = new ECGCoordinateBuilder().build(record, "II");
					ChartInfo aVLGraph = new ECGCoordinateBuilder().build(record, "aVL");
					ChartInfo v2Graph = new ECGCoordinateBuilder().build(record, "v2");
					ChartInfo v5Graph = new ECGCoordinateBuilder().build(record, "v5");
					ChartInfo iiiGraph = new ECGCoordinateBuilder().build(record, "III");
					ChartInfo aVFGraph = new ECGCoordinateBuilder().build(record, "aVF");
					ChartInfo v3Graph = new ECGCoordinateBuilder().build(record, "v3");
					ChartInfo v6Graph = new ECGCoordinateBuilder().build(record, "v6");

					// Submitting the tasks
					taskExecutor.execute(() -> iGraphHandler.start(iGraph));
					taskExecutor.execute(() -> aVRGraphHandler.start(aVRGraph));
					taskExecutor.execute(() -> v1GraphHandler.start(v1Graph));
					taskExecutor.execute(() -> v4GraphHandler.start(v4Graph));
					taskExecutor.execute(() -> iiGraphHandler.start(iiGraph));
					taskExecutor.execute(() -> aVLGraphHandler.start(aVLGraph));
					taskExecutor.execute(() -> v2GraphHandler.start(v2Graph));
					taskExecutor.execute(() -> v5GraphHandler.start(v5Graph));
					taskExecutor.execute(() -> iiiGraphHandler.start(iiiGraph));
					taskExecutor.execute(() -> aVFGraphHandler.start(aVFGraph));
					taskExecutor.execute(() -> v3GraphHandler.start(v3Graph));
					taskExecutor.execute(() -> v6GraphHandler.start(v6Graph));
				} catch (IOException ioException) {
					showErrorMessage("Error reading the file.");
				}
			});

			iChartLabel = new Label("I");
			iChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			iChartLabel.setAlignment(Pos.CENTER);

			aVRChartLabel = new Label("aVR");
			aVRChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			aVRChartLabel.setAlignment(Pos.CENTER);

			v1ChartLabel = new Label("V1");
			v1ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v1ChartLabel.setAlignment(Pos.CENTER);

			v4ChartLabel = new Label("V4");
			v4ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v4ChartLabel.setAlignment(Pos.CENTER);

			iiChartLabel = new Label("II");
			iiChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			iiChartLabel.setAlignment(Pos.CENTER);

			aVLChartLabel = new Label("aVL");
			aVLChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			aVLChartLabel.setAlignment(Pos.CENTER);

			v2ChartLabel = new Label("V2");
			v2ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v2ChartLabel.setAlignment(Pos.CENTER);

			v5ChartLabel = new Label("V5");
			v5ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v5ChartLabel.setAlignment(Pos.CENTER);

			iiiChartLabel = new Label("III");
			iiiChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			iiiChartLabel.setAlignment(Pos.CENTER);

			aVFChartLabel = new Label("aVF");
			aVFChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			aVFChartLabel.setAlignment(Pos.CENTER);

			v3ChartLabel = new Label("V3");
			v3ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v3ChartLabel.setAlignment(Pos.CENTER);

			v6ChartLabel = new Label("V6");
			v6ChartLabel.setPrefSize(systemDimension.getWidth() * 0.25, systemDimension.getHeight() * 0.05);
			v6ChartLabel.setAlignment(Pos.CENTER);

			iChartContainer = new VBox(iLineChart, iChartLabel);
			aVRChartContainer = new VBox(aVRLineChart, aVRChartLabel);
			v1ChartContainer = new VBox(v1LineChart, v1ChartLabel);
			v4ChartContainer = new VBox(v4LineChart, v4ChartLabel);
			iiChartContainer = new VBox(iiLineChart, iiChartLabel);
			aVLChartContainer = new VBox(aVLLineChart, aVLChartLabel);
			v2ChartContainer = new VBox(v2LineChart, v2ChartLabel);
			v5ChartContainer = new VBox(v5LineChart, v5ChartLabel);
			iiiChartContainer = new VBox(iiiLineChart, iiiChartLabel);
			aVFChartContainer = new VBox(aVFLineChart, aVFChartLabel);
			v3ChartContainer = new VBox(v3LineChart, v3ChartLabel);
			v6ChartContainer = new VBox(v6LineChart, v6ChartLabel);

			fileContainer = new HBox(filePathField, chooseFileButton);
			HBox.setMargin(filePathField, new Insets(2, 2, 2, 2));
			HBox.setMargin(chooseFileButton, new Insets(2, 2, 2, 0));

			group1Container = new HBox(iChartContainer, aVRChartContainer, v1ChartContainer, v4ChartContainer);
			HBox.setMargin(iChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(aVRChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v1ChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v4ChartContainer, new Insets(2, 2, 2, 0));

			group2Container = new HBox(iiChartContainer, aVLChartContainer, v2ChartContainer, v5ChartContainer);
			HBox.setMargin(iiChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(aVLChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v2ChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v5ChartContainer, new Insets(2, 2, 2, 0));

			group3Container = new HBox(iiiChartContainer, aVFChartContainer, v3ChartContainer, v6ChartContainer);
			HBox.setMargin(iiiChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(aVFChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v3ChartContainer, new Insets(2, 2, 2, 0));
			HBox.setMargin(v6ChartContainer, new Insets(2, 2, 2, 0));

			container = new VBox(fileContainer, group1Container, group2Container, group3Container);

			borderPane = new BorderPane(container);
			Scene scene = new Scene(borderPane, 600, 530);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ECG Visualization");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage("Error initiating the views.");
		}
	}

	/**
	 * Execution starts from here
	 * 
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
