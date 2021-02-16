package com.peacefulotter.ml;

import com.peacefulotter.ml.game.Circuit;
import com.peacefulotter.ml.game.GameLoop;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class Main extends Application
{
    private static final int CIRCUIT_WIDTH = 1080;
    private static final int CIRCUIT_HEIGHT = 815;
    private static final int MIN_CANVAS_WIDTH = CIRCUIT_WIDTH;
    private static final int MIN_CANVAS_HEIGHT = CIRCUIT_HEIGHT + 120;

    private static final int ORIGINAL_POPULATION = 600;
    private static final double CROSSOVER_RATE = 0.1d;
    private static final double MUTATION_STRENGTH = 2d;
    private static final double MUTATION_RATE = 0.030d;

    private static final Spinner<Double> crossoverSpinner = new Spinner<>(0.01d, 1d, CROSSOVER_RATE, 0.01d);
    private static final Spinner<Double> mutIntensitySpinner = new Spinner<>(0.1d, 5d, MUTATION_STRENGTH, 0.1d);
    private static final Spinner<Double> muteRateSpinner = new Spinner<>(0.001d, 0.100d, MUTATION_RATE, 0.001d);

    private static final Label FPS_LABEL = new Label( "0 fps" );

    private Circuit circuit;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start( Stage window ) throws Exception
    {
        Canvas canvas = new Canvas( CIRCUIT_WIDTH, CIRCUIT_HEIGHT );
        this.circuit = new Circuit(
                canvas, ORIGINAL_POPULATION,
                crossoverSpinner.getValueFactory(),
                mutIntensitySpinner.getValueFactory(),
                muteRateSpinner.getValueFactory());
        GameLoop loop = new GameLoop(circuit);

        BorderPane wrapper = new BorderPane();
        wrapper.setTop( circuit );
        wrapper.setBottom( buildBottomTab() );
        window.setMinWidth( MIN_CANVAS_WIDTH );
        window.setMinHeight( MIN_CANVAS_HEIGHT );
        window.setScene( new Scene( wrapper ) );
        window.show();

        loop.start();
    }

    private BorderPane buildBottomTab() {
        BorderPane pane = new BorderPane();
        pane.setStyle( "-fx-spacing: 4; -fx-padding: 4;" );

        Label populationLabel = new Label( "Population" );
        Label crossoverLabel = new Label( "Crossover rate" );
        Label mutStrengthLabel = new Label( "Mutation Strength" );
        Label mutRateLabel = new Label( "Mutation rate" );
        Spinner<Integer> populationSpinner = new Spinner<>(5, 1000, ORIGINAL_POPULATION, 5);

        Button nextGenButton = new Button( "Next Gen" );
        Label genLabel = new Label( "Generation 1" );
        Label averageSpeed = new Label( "Average Speed: 0" );

        populationLabel.setStyle( "-fx-padding: 0 5 0 15" );
        crossoverLabel.setStyle( "-fx-padding: 0 5 0 15" );
        mutStrengthLabel.setStyle( "-fx-padding: 0 5 0 15" );
        mutRateLabel.setStyle( "-fx-padding: 0 5 0 15" );
        genLabel.setStyle( "-fx-label-padding: 0 15 0 15" );
        averageSpeed.setStyle( "-fx-label-padding: 0 15 0 15" );
        FPS_LABEL.setStyle( "-fx-label-padding: 0 15 0 15" );

        nextGenButton.setOnMouseClicked( event -> {
            circuit.nextGeneration(populationSpinner.getValue());
            genLabel.setText( "Generation " + circuit.getGeneration() );
        } );
        circuit.getAverageSpeed().addListener( (elt, o, n) ->
                averageSpeed.setText( "Average Speed: " + String.format("%1$,.2f", n.doubleValue() * 100) ) );

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle( "-fx-padding: 0 0 15 0" );
        top.getChildren().addAll(
                populationLabel, populationSpinner,
                crossoverLabel, crossoverSpinner,
                mutStrengthLabel, mutIntensitySpinner,
                mutRateLabel, muteRateSpinner );
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.getChildren().addAll( nextGenButton, genLabel, averageSpeed, FPS_LABEL );

        pane.setTop( top );
        pane.setBottom( bottom );
        return pane;
    }

    public static void setFPS(double fps) {
        FPS_LABEL.setText( (int) fps + " fps" );
    }
}
