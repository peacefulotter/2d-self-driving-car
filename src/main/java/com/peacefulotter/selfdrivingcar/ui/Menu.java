package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage.CIRCUIT_WIDTH;


public class Menu extends GridPane
{
    private final BorderPane wrapper = new BorderPane();

    public Menu( Map map, GeneticCircuit circuit, Spinners spinners )
    {
        ColumnConstraints mapCol = new ColumnConstraints();
        mapCol.setPrefWidth( CIRCUIT_WIDTH );
        ColumnConstraints uiCol = new ColumnConstraints();
        getColumnConstraints().addAll(mapCol, uiCol);

        BottomPanel bottomPanel = new BottomPanel( circuit, map );
        RightPanel rightPanel = new RightPanel( circuit );

        wrapper.setTop(spinners);
        wrapper.setCenter( rightPanel );
        wrapper.setBottom( bottomPanel );

        addRow( 0, map, wrapper );
    }

}
