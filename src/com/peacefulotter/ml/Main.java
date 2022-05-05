package com.peacefulotter.ml;

import com.peacefulotter.ml.game.*;
import com.peacefulotter.ml.utils.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import static com.peacefulotter.ml.game.MultiThreadCircuit.THREADS;

public class Main extends Application
{
    private static final int CIRCUIT_WIDTH = 1080;
    private static final int CIRCUIT_HEIGHT = 815;
    private static final int MIN_CANVAS_WIDTH = CIRCUIT_WIDTH;
    private static final int MIN_CANVAS_HEIGHT = CIRCUIT_HEIGHT + 120;

    private static final int ORIGINAL_POPULATION = 1000;
    private static final int MAX_POPULATION = 5000;
    private static final int MIN_POPULATION = 200;
    private static final int POPULATION_MARGIN = 25;

    private static final double CROSSOVER_RATE = 0.1d;
    private static final double MUTATION_STRENGTH = 2d;
    private static final double MUTATION_RATE = 0.030d;

    private static final Spinner<Double> crossoverSpinner = new Spinner<>(0.01d, 1d, CROSSOVER_RATE, 0.01d);
    private static final Spinner<Double> mutIntensitySpinner = new Spinner<>(0.1d, 5d, MUTATION_STRENGTH, 0.1d);
    private static final Spinner<Double> muteRateSpinner = new Spinner<>(0.001d, 0.100d, MUTATION_RATE, 0.001d);

    private static final Label FPS_LABEL = new Label( "0 fps" );
    private static final Label populationProportionLabel = new Label( ORIGINAL_POPULATION + " / " + 0 );

    private static final String GENETIC_STYLE = "-fx-padding: 0 5 0 15";
    private static final String INFO_STYLE = "-fx-label-padding: 0 15 0 15";

    private Circuit circuit;
    private Map map;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start( Stage window ) throws Exception
    {
        System.out.println("Working on " + THREADS + " threads");

        Canvas canvas = new Canvas( CIRCUIT_WIDTH, CIRCUIT_HEIGHT );
        map = new Map(canvas);
        /* Replace by either:
            new Circuit(...) --> runs on a single thread (be careful with the ORIGINAL_POPULATION value)
            new MultiThreadCircuit(...) --> runs on multiple threads
            new ControlCircuit(...) --> be able to control a car on the circuit
         */
        this.circuit = new MultiThreadCircuit( map, ORIGINAL_POPULATION,
                crossoverSpinner.getValueFactory(),
                mutIntensitySpinner.getValueFactory(),
                muteRateSpinner.getValueFactory());
        GameLoop loop = new GameLoop(circuit);

        BorderPane wrapper = new BorderPane();
        wrapper.setTop( map );
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
        Spinner<Integer> populationSpinner = new Spinner<>(MIN_POPULATION, MAX_POPULATION, ORIGINAL_POPULATION, POPULATION_MARGIN);

        Button nextGenButton = new Button( "Next Gen" );
        Button toggleRenderDeadCarButton = new Button( "Toggle render dead car" );
        Button testMapButton = new Button( "Test Map" );
        Button saveModelButton = new Button( "Save Model" );

        Label genLabel = new Label( "Generation 1" );
        Label selectedParentsLabel = new Label( "0 Parents selected" );
        Label averageSpeed = new Label( "Average Speed: 0" );

        // Listeners and Bindings
        nextGenButton.setOnMouseClicked( event -> {
            circuit.nextGeneration(populationSpinner.getValue());
            genLabel.setText( "Generation " + (circuit.getGeneration() + 1) );
        } );
        toggleRenderDeadCarButton.setOnMouseClicked( event -> map.toggleRenderDeadCars() );
        testMapButton.setOnMouseClicked( event -> {
            map.setTestMap();
            circuit.testGeneration();
        } );
        saveModelButton.setOnMouseClicked( event -> circuit.saveSelectedCar() );
        circuit.getSelectedParents().addListener( (elt, o, n) ->
                selectedParentsLabel.setText( n + " Parents selected" ) );
        circuit.getAverageSpeed().addListener( (elt, o, n) ->
                averageSpeed.setText( "Average Speed: " + String.format("%1$,.2f", n.doubleValue() * 100) ) );

        // Genetic Top Box
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle( "-fx-padding: 0 0 15 0" );
        top.getChildren().addAll(
                populationLabel, populationSpinner,
                crossoverLabel, crossoverSpinner,
                mutStrengthLabel, mutIntensitySpinner,
                mutRateLabel, muteRateSpinner );
        for ( int i = 0; i < top.getChildren().size(); i += 2 )
            top.getChildren().get( i ).setStyle( GENETIC_STYLE );

        // Buttons and Information Bottom Box
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.getChildren().addAll(
                nextGenButton, toggleRenderDeadCarButton, genLabel,
                selectedParentsLabel, populationProportionLabel,
                FPS_LABEL, averageSpeed, testMapButton, saveModelButton );
        for ( Node node: bottom.getChildren() )
            node.setStyle( INFO_STYLE );

        pane.setTop( top );
        pane.setBottom( bottom );

        return pane;
    }

    public static void setFPS(double fps) {
        FPS_LABEL.setText( (int) fps + " fps" );
    }

    public static void setPopulationProportion( int alive, int dead )
    {
        populationProportionLabel.setText( alive + " / " + dead );
    }
}
