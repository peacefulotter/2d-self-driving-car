package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.ui.BottomPanel;
import com.peacefulotter.selfdrivingcar.ui.RightPanel;
import com.peacefulotter.selfdrivingcar.ui.Spinners;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage.CIRCUIT_WIDTH;

public class Classic extends Application
{


    @Override
    public void start(Stage stage)
    {
        Map map = DefaultStage.createMap();

        Spinners spinners = new Spinners();
        GeneticParams params = spinners.getParams();
        Genetic genetic = new Genetic( params );

        Circuit circuit = new MultiThreadCircuit( map, genetic );

        GridPane root = new GridPane();
        ColumnConstraints mapCol = new ColumnConstraints();
        mapCol.setPrefWidth( CIRCUIT_WIDTH );
        ColumnConstraints uiCol = new ColumnConstraints();
        root.getColumnConstraints().addAll(mapCol, uiCol);

        BottomPanel bottomPanel = new BottomPanel( circuit, map );
        RightPanel rightPanel = new RightPanel( circuit );

        BorderPane wrapper = new BorderPane();
        wrapper.setTop( spinners );
        wrapper.setCenter( rightPanel );
        wrapper.setBottom( bottomPanel );

        root.addRow( 0, map, wrapper );

        DefaultStage.launch( stage, root, circuit );
    }
}
