package com.peacefulotter.selfdrivingcar;

import com.peacefulotter.selfdrivingcar.game.*;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import com.peacefulotter.selfdrivingcar.ui.BottomPanel;
import com.peacefulotter.selfdrivingcar.ui.RightPanel;
import com.peacefulotter.selfdrivingcar.ui.Spinners;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application
{
    private static final int CIRCUIT_WIDTH = 794;
    private static final int CIRCUIT_HEIGHT = 599;
//    private static final int MIN_CANVAS_WIDTH = CIRCUIT_WIDTH + 400;
//    private static final int MIN_CANVAS_HEIGHT = CIRCUIT_HEIGHT + 120;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start( Stage window )
    {
        Map map = new Map( CIRCUIT_WIDTH, CIRCUIT_HEIGHT, MapParams.DEFAULT);
        Spinners spinners = new Spinners();
        GeneticParams params = spinners.getParams();

        /* Replace by either:
            new Circuit(map, params) --> runs on a single thread (be careful with the ORIGINAL_POPULATION value)
            new MultiThreadCircuit(map, params) --> runs on multiple threads
            new ControlCircuit( map ) --> be able to control a car on the circuit
         */
        Circuit circuit = new MultiThreadCircuit( map, params );
        GameLoop loop = new GameLoop(circuit);

        GridPane root = new GridPane();
        ColumnConstraints mapCol = new ColumnConstraints();
        mapCol.setPrefWidth( CIRCUIT_WIDTH );
        ColumnConstraints uiCol = new ColumnConstraints();
        root.getColumnConstraints().addAll(mapCol, uiCol);

        BottomPanel bottomPanel = new BottomPanel( circuit, map );
        RightPanel rightPanel = new RightPanel( circuit, params );

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
