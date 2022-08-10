package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage.CIRCUIT_WIDTH;


public class Menu extends GridPane
{
    private final GridPane wrapper = new GridPane();

    public Menu( Map map, GeneticCircuit circuit, Spinners spinners )
    {
        ColumnConstraints mapCol = new ColumnConstraints();
        mapCol.setPrefWidth( CIRCUIT_WIDTH );
        ColumnConstraints uiCol = new ColumnConstraints();
        getColumnConstraints().addAll(mapCol, uiCol);

        GenerationPanel genPanel = new GenerationPanel( circuit );
        GridPane bottomPanel = new BottomPanel( circuit, map );

        wrapper.setStyle( "-fx-padding: 20px;" );
        wrapper.setVgap(10);

        wrapper.addRow( 0, spinners );
        wrapper.addRow( 1, genPanel );
        wrapper.addRow( 2, bottomPanel );

        addRow( 0, map, wrapper );
    }

}
