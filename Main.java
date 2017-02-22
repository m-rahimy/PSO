package ir.mrgkrahimy.pso_implementation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

import static ir.mrgkrahimy.pso_implementation.MatrixHelper.calcVectorLength;


public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {

        showBase(primaryStage);
        PSO pso = new PSO(5, -10, 10, 50, 300, 0.4f, 1.5f, 1.75f);/*
        pso.compute();
        showResultAsChart(primaryStage, pso);*/

        //disp(pso.getAll());
    }

    private void showBase(Stage primaryStage) {
        GridPane parent = new GridPane();
        parent.setPadding(new Insets(10));
        parent.setVgap(10);
        parent.setHgap(10);
        parent.addRow(0);
        final int[] i = {1};

        TextField nVarTextField = new TextField("5"), varMinTextField = new TextField("-10"),
                varMaxTextField = new TextField("10"),
                nPopTextField = new TextField("50"), maxIterationTextField = new TextField("300"),
                wTextField = new TextField("0.4"),
                c1TextField = new TextField("1.5"), c2TextField = new TextField("1.75");

        Button generateButton = new Button("Generate PSO");
        generateButton.setOnAction(event -> {
            PSO pso = new PSO(
                    Integer.parseInt(nVarTextField.getText()),
                    Integer.parseInt(varMinTextField.getText()),
                    Integer.parseInt(varMaxTextField.getText()),
                    Integer.parseInt(nPopTextField.getText()),
                    Integer.parseInt(maxIterationTextField.getText()),
                    Float.parseFloat(wTextField.getText()),
                    Float.parseFloat(c1TextField.getText()),
                    Float.parseFloat(c2TextField.getText())
            );
            new Thread(() -> {
                long startTime = System.nanoTime();
                pso.compute();
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage stage = new Stage();
                        stage.setTitle("Results");
                        GridPane gridPane = new GridPane();
                        gridPane.setPadding(new Insets(10));
                        gridPane.setVgap(10);
                        gridPane.setHgap(10);
                        int r = 0;
                        gridPane.addRow(r);

                        Button showResultAsChartButton = new Button("show ResultAs Chart");
                        showResultAsChartButton.setOnAction(event1 -> showResultAsChart(primaryStage, pso));
                        TextArea bestCostsTextArea = new TextArea(Arrays.toString(pso.getBestCosts()));
                        bestCostsTextArea.setWrapText(true);
                        Particle[] particles = pso.getAll().get(0);
                        float minLen = (float) Math.pow(2, 32);
                        Particle particle = null;
                        for (Particle p : particles) {
                            float l = calcVectorLength(p.position);
                            if (l < minLen) {
                                minLen = l;
                                particle = new Particle(p);
                            }
                        }
                        // show results
                        gridPane.addRow(r++,
                                new Label("Execution Time: "),
                                new Label(
                                        String.valueOf(duration) + " ns = " +
                                                String.valueOf(duration / 1000000) + " ms "));
                        gridPane.addRow(r++, new Label("Best Costs in each Iteration: "), bestCostsTextArea);
                        gridPane.addRow(r++,
                                new Label("The Particle with Maximized Chance: "),
                                new Label(Arrays.toString(particle.position)));
                        gridPane.addRow(r++);
                        gridPane.addRow(r++, new Label(), showResultAsChartButton);


                        stage.setScene(new Scene(gridPane));
                        stage.show();
                    }
                });
            }).start();

        });

        parent.addRow(i[0]++, new Label("nVar"), nVarTextField);
        parent.addRow(i[0]++, new Label("varMin"), varMinTextField);
        parent.addRow(i[0]++, new Label("varMax"), varMaxTextField);
        parent.addRow(i[0]++, new Label("nPop"), nPopTextField);
        parent.addRow(i[0]++, new Label("maxIteration"), maxIterationTextField);
        parent.addRow(i[0]++, new Label("w"), wTextField);
        parent.addRow(i[0]++, new Label("c1"), c1TextField);
        parent.addRow(i[0]++, new Label("c2"), c2TextField);

        parent.addRow(i[0]++);
        parent.addRow(i[0]++, new Label(), generateButton);

        Stage stage = new Stage();
        stage.setTitle("PSO algorithm");
        stage.setScene(new Scene(parent));
        stage.show();

    }

    private void showProcessAsChart(PSO pso) {

        List<Particle[]> particles = pso.getAll();
        int l = particles.get(0)[0].position.length;
        //final int l=2;
        for (int i = 0; i < l; i++) {
            Stage stage = new Stage();
            stage.setWidth(516);
            stage.setHeight(439);
            final int[] iter = {1};
            final int j = i;
            Timeline tl = new Timeline();
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                    actionEvent -> {
                        float sc = 1;//iter[0];//(float) (2 * (Math.pow(iter[0], 2)));
                        NumberAxis NxAxis = new NumberAxis("Particle Position (dimension " + j + ")",
                                pso.getVarMin() / sc, pso.getVarMax() / sc, 0.5 / sc);
                        NumberAxis NyAxis = new NumberAxis("Particle Position (dimension " + (j + 1 >= l ? 0 : j + 1) + ")",
                                pso.getVarMin() / sc, pso.getVarMax() / sc, 0.5 / sc);

                        ScatterChart<Number, Number> scatterChart =
                                new ScatterChart<>(NxAxis, NyAxis);
                        scatterChart.setTitle("iteration: " + iter[0]);
                        Series series = new Series();
                        series.setName("Particle Position");
                        for (Particle p : particles.get(iter[0])) {
                            series.getData().add(
                                    new XYChart.Data<>(
                                            p.position[j],
                                            p.position[j + 1 >= l ? 0 : j + 1]));
                        }

                        iter[0]++;
                        scatterChart.setTitle("iteration: " + iter[0]);
                        series.getData().clear();
                        for (Particle p : particles.get(iter[0])) {
                            series.getData().add(
                                    new XYChart.Data<>(
                                            p.position[j],
                                            p.position[j + 1 >= l ? 0 : j + 1]));
                        }
                        scatterChart.getData().add(series);

                        stage.setScene(new Scene(new BorderPane(scatterChart)));

                    }));

            tl.setCycleCount(particles.size());
            tl.play();

            System.out.println("i is " + i);

            int margin = 1;

            stage.setX(margin + stage.getWidth() * i);
            if (i <= 2) {
                stage.setY(margin);
            } else {
                stage.setX(margin + stage.getWidth() * (i % 3));
                stage.setY(margin + stage.getHeight());
            }
            if (i > 2) {
                stage.setY(margin + stage.getHeight());
            }

            stage.show();

        }

    }

    private void disp(List<Particle[]> particleList) {
        for (Particle[] particles : particleList) {
            for (Particle particle : particles) {
                System.out.println(particle.toString());
            }
        }
    }

    private void showResultAsChart(Stage primaryStage, PSO pso) {

        List<Particle[]> particles = pso.getAll();

        primaryStage.setTitle("Chart");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iteration");
        yAxis.setLabel("Best Cost");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Best Cost of PSO in " + particles.size() + " Iterations");
        XYChart.Series series = new Series();
        series.setName("Best Cost at The Specific Iteration");
        float[] bestCosts = pso.getBestCosts();
        for (int i = 1; i < pso.getMaxIteration(); i++) {
            series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
        }

        final double SCALE_DELTA = 1.1;
        lineChart.setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

            lineChart.setScaleX(lineChart.getScaleX() * scaleFactor);
            lineChart.setScaleY(lineChart.getScaleY() * scaleFactor);
        });

        lineChart.setOnMousePressed(event -> {
            if (event.getClickCount() == 2) {
                lineChart.setScaleX(1.0);
                lineChart.setScaleY(1.0);
            }
        });

        xAxis.setAutoRanging(false);


        BorderPane parent = new BorderPane();
        parent.setCenter(lineChart);
        Button showProcessButton = new Button("Show Process");
        showProcessButton.setId("a");
        showProcessButton.setOnAction(event -> showProcessAsChart(pso));

        Button _1st_Div = new Button("First Part");
        _1st_Div.setOnAction(event -> {
            series.getData().clear();
            xAxis.setLowerBound(1);
            xAxis.setUpperBound(25);
            for (int i = 1; i < 25; i++) {
                series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
            }
        });

        Button _2nd_Div = new Button("Second Part");
        _2nd_Div.setOnAction(event -> {
            series.getData().clear();
            xAxis.setLowerBound(25);
            xAxis.setUpperBound(50);
            for (int i = 25; i < 50; i++) {
                series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
            }
        });

        Button _3rd_Div = new Button("Third Part");
        _3rd_Div.setOnAction(event -> {
            series.getData().clear();
            xAxis.setLowerBound(50);
            xAxis.setUpperBound(100);
            for (int i = 50; i < 100; i++) {
                series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
            }
        });

        Button last_Div = new Button("Last Part");
        last_Div.setOnAction(event -> {
            series.getData().clear();
            xAxis.setLowerBound(pso.getMaxIteration() - 50);
            xAxis.setUpperBound(pso.getMaxIteration());
            for (int i = pso.getMaxIteration() - 50; i < pso.getMaxIteration(); i++) {
                series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
            }
        });

        Slider lowerBoundSlider = new Slider(0, pso.getMaxIteration(), 0);
        lowerBoundSlider.setShowTickLabels(true);
        lowerBoundSlider.setShowTickMarks(true);

        Slider upperBoundSlider = new Slider(0, pso.getMaxIteration(), pso.getMaxIteration());
        upperBoundSlider.setShowTickLabels(true);
        upperBoundSlider.setShowTickMarks(true);

        lowerBoundSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < (int) upperBoundSlider.getValue()) {
                    series.getData().clear();
                    xAxis.setLowerBound(newValue.intValue());
                    xAxis.setUpperBound(upperBoundSlider.getValue());
                    for (int i = (int)xAxis.getLowerBound(); i < xAxis.getUpperBound(); i++) {
                        series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
                    }
                }else {
                    lowerBoundSlider.setValue(upperBoundSlider.getValue()-2);
                }
            }
        });

        upperBoundSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue()> (int)lowerBoundSlider.getValue()){
                    series.getData().clear();
                    xAxis.setLowerBound(lowerBoundSlider.getValue());
                    xAxis.setUpperBound(newValue.intValue());
                    for (int i=(int)xAxis.getLowerBound();i<xAxis.getUpperBound();i++){
                        series.getData().add(new XYChart.Data<>(i, bestCosts[i]));
                    }
                }else {
                    upperBoundSlider.setValue(lowerBoundSlider.getValue()+2);
                }
            }
        });


        GridPane bottomGridPane = new GridPane();
        bottomGridPane.setAlignment(Pos.CENTER);
        bottomGridPane.setHgap(10);
        bottomGridPane.setVgap(20);
        bottomGridPane.setPadding(new Insets(20));
        int cols=0;
        int rows=1;
        bottomGridPane.addColumn(cols++, showProcessButton);
        bottomGridPane.addColumn(cols++, _1st_Div);
        bottomGridPane.addColumn(cols++, _2nd_Div);
        bottomGridPane.addColumn(cols++, _3rd_Div);
        bottomGridPane.addColumn(cols++, last_Div);

        bottomGridPane.add(new Label("lower: "), 0, rows, 1, 1);
        bottomGridPane.add(lowerBoundSlider, 1, rows++, 2, 1);
        bottomGridPane.add(new Label("upper: "), 0, rows, 1, 1);
        bottomGridPane.add(upperBoundSlider, 1, rows++, 2,1);

        parent.setBottom(bottomGridPane);
        Scene scene = new Scene(parent, 800, 600);
        lineChart.getData().add(series);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
