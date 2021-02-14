package com.peacefulotter.ml;

import com.peacefulotter.ml.game.Circuit;
import com.peacefulotter.ml.game.GameLoop;
import javafx.application.Application;
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
    private static final int MIN_CANVAS_HEIGHT = CIRCUIT_HEIGHT + 100;

    private static final int ORIGINAL_POPULATION = 150;

    private Circuit circuit;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start( Stage window ) throws Exception
    {
        Canvas canvas = new Canvas( CIRCUIT_WIDTH, CIRCUIT_HEIGHT );
        this.circuit = new Circuit(canvas, ORIGINAL_POPULATION);
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

    private HBox buildBottomTab() {
        HBox box = new HBox();
        box.setStyle( "-fx-spacing: 4; -fx-padding: 4;" );

        Spinner<Integer> spinner = new Spinner<>(5, 650, ORIGINAL_POPULATION, 5);
        Button nextGenButton = new Button( "Next Gen" );
        Label genLabel = new Label( "Generation 1" );
        Label population = new Label( "Population " + ORIGINAL_POPULATION );
        Label averageSpeed = new Label( "Average Speed: 0" );

        nextGenButton.setOnMouseClicked( event -> {
            circuit.nextGeneration(spinner.getValue());
            genLabel.setText( "Generation " + circuit.getGeneration() );
        } );
        circuit.getAverageSpeed().addListener( (elt, o, n) ->
                averageSpeed.setText( "Average Speed: " + String.format("%1$,.2f", n.doubleValue() * 100) ) );

        box.getChildren().addAll( spinner, nextGenButton, genLabel, population, averageSpeed );
        return box;
    }
}
