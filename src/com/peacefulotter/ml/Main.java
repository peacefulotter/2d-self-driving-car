package com.peacefulotter.ml;

import com.peacefulotter.ml.game.*;
import com.peacefulotter.ml.game.circuit.Circuit;
import com.peacefulotter.ml.game.circuit.ControlCircuit;
import com.peacefulotter.ml.game.circuit.MultiThreadCircuit;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.ui.BottomPanel;
import com.peacefulotter.ml.ui.RightPanel;
import com.peacefulotter.ml.ui.Spinners;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application
{
    private static final int CIRCUIT_WIDTH = 794;
    private static final int CIRCUIT_HEIGHT = 599;
    private static final int MIN_CANVAS_WIDTH = CIRCUIT_WIDTH + 400;
    private static final int MIN_CANVAS_HEIGHT = CIRCUIT_HEIGHT + 120;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start( Stage window )
    {
        Spinners spinners = new Spinners();
        Map map = new Map( CIRCUIT_WIDTH, CIRCUIT_HEIGHT, MapParams.DEFAULT);

        /* Replace by either:
            new Circuit(map, spinners) --> runs on a single thread (be careful with the ORIGINAL_POPULATION value)
            new MultiThreadCircuit(map, spinners) --> runs on multiple threads
            new ControlCircuit( map ) --> be able to control a car on the circuit
         */
        Circuit circuit = new MultiThreadCircuit(map, spinners);
        GameLoop loop = new GameLoop(circuit);

        GridPane root = new GridPane();
        ColumnConstraints mapCol = new ColumnConstraints();
        mapCol.setPrefWidth( CIRCUIT_WIDTH );
        ColumnConstraints uiCol = new ColumnConstraints();
        root.getColumnConstraints().addAll(mapCol, uiCol);

        BottomPanel bottomPanel = new BottomPanel( circuit, map );
        RightPanel rightPanel = new RightPanel( circuit, spinners );
        BorderPane wrapper = new BorderPane();
        wrapper.setTop( spinners );
        wrapper.setCenter( rightPanel );
        wrapper.setBottom( bottomPanel );

        root.addRow( 0, map, wrapper );

        window.setResizable(false);
        window.setScene( new Scene( root ) );
        window.show();

        loop.start();
    }
}
