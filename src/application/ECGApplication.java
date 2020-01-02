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
	ChartHandler aVRGraphHandler = new ChartHandler();
	ChartHandler aVLGraphHandler = new ChartHandler();
	ChartHandler aVFGraphHandler = new ChartHandler();
	ChartHandler iGraphHandler = new ChartHandler();
	ChartHandler iiGraphHandler = new ChartHandler();
	ChartHandler iiiGraphHandler = new ChartHandler();

	/**
	 * The GUI components
	 */
	BorderPane borderPane;
	VBox container;
	HBox fileContainer;
	HBox group1Container;
	HBox group2Container;
	HBox group3Container;
	VBox aVFChartContainer;
	VBox aVRChartContainer;
	VBox aVLChartContainer;
	VBox iChartContainer;
	VBox iiChartContainer;
	VBox iiiChartContainer;
	TextField filePathField;
	Button chooseFileButton;
	LineChart<Number, Number> aVRLineChart;
	LineChart<Number, Number> aVLLineChart;
	LineChart<Number, Number> aVFLineChart;
	LineChart<Number, Number> iLineChart;
	LineChart<Number, Number> iiLineChart;
	LineChart<Number, Number> iiiLineChart;
	Label aVFChartLabel;
	Label aVRChartLabel;
	Label aVLChartLabel;
	Label iChartLabel;
	Label iiChartLabel;
	Label iiiChartLabel;

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

			aVRLineChart = aVRGraphHandler.init();
			aVFLineChart = aVFGraphHandler.init();
			aVLLineChart = aVLGraphHandler.init();
			iLineChart = iGraphHandler.init();
			iiLineChart = iiGraphHandler.init();
			iiiLineChart = iiiGraphHandler.init();

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
					ExecutorService taskExecutor = Executors.newCachedThreadPool();

					ChartInfo aVFGraph = new ECGCoordinateBuilder().build(record, "aVF");
					ChartInfo aVRGraph = new ECGCoordinateBuilder().build(record, "aVR");
					ChartInfo aVLGraph = new ECGCoordinateBuilder().build(record, "aVL");
					ChartInfo iGraph = new ECGCoordinateBuilder().build(record, "I");
					ChartInfo iiGraph = new ECGCoordinateBuilder().build(record, "II");
					ChartInfo iiiGraph = new ECGCoordinateBuilder().build(record, "III");

					// Submitting the tasks
					taskExecutor.submit(() -> aVFGraphHandler.start(aVFGraph));
					taskExecutor.submit(() -> aVRGraphHandler.start(aVRGraph));
					taskExecutor.submit(() -> aVLGraphHandler.start(aVLGraph));
					taskExecutor.submit(() -> iGraphHandler.start(iGraph));
					taskExecutor.submit(() -> iiGraphHandler.start(iiGraph));
					taskExecutor.submit(() -> iiiGraphHandler.start(iiiGraph));
				} catch (IOException ioException) {
					showErrorMessage("Error reading the file.");
				}
			});

			aVFChartLabel = new Label("aVF");
			aVFChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			aVFChartLabel.setAlignment(Pos.CENTER);

			aVRChartLabel = new Label("aVR");
			aVRChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			aVRChartLabel.setAlignment(Pos.CENTER);

			aVLChartLabel = new Label("aVL");
			aVLChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			aVLChartLabel.setAlignment(Pos.CENTER);

			iChartLabel = new Label("I");
			iChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			iChartLabel.setAlignment(Pos.CENTER);

			iiChartLabel = new Label("II");
			iiChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			iiChartLabel.setAlignment(Pos.CENTER);

			iiiChartLabel = new Label("III");
			iiiChartLabel.setPrefSize(systemDimension.getWidth() * 0.5, systemDimension.getHeight() * 0.05);
			iiiChartLabel.setAlignment(Pos.CENTER);

			aVFChartContainer = new VBox(aVFLineChart, aVFChartLabel);
			aVRChartContainer = new VBox(aVRLineChart, aVRChartLabel);
			aVLChartContainer = new VBox(aVLLineChart, aVLChartLabel);
			iChartContainer = new VBox(iLineChart, iChartLabel);
			iiChartContainer = new VBox(iiLineChart, iiChartLabel);
			iiiChartContainer = new VBox(iiiLineChart, iiiChartLabel);

			fileContainer = new HBox(filePathField, chooseFileButton);
			HBox.setMargin(filePathField, new Insets(2, 2, 2, 2));
			HBox.setMargin(chooseFileButton, new Insets(2, 2, 2, 0));

			group1Container = new HBox(aVRChartContainer, aVFChartContainer);
			HBox.setMargin(aVRChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(aVFChartContainer, new Insets(2, 2, 2, 0));

			group2Container = new HBox(aVLChartContainer, iChartContainer);
			HBox.setMargin(aVLChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(iChartContainer, new Insets(2, 2, 2, 0));

			group3Container = new HBox(iiChartContainer, iiiChartContainer);
			HBox.setMargin(iiChartContainer, new Insets(2, 2, 2, 2));
			HBox.setMargin(iiiChartContainer, new Insets(2, 2, 2, 0));

			container = new VBox(fileContainer, group1Container, group2Container, group3Container);

			borderPane = new BorderPane(container);
			Scene scene = new Scene(borderPane, 600, 530);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ECG Visualization");
			primaryStage.show();
		} catch (Exception e) {
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
