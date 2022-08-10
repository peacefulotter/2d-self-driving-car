package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.RecordCircuit;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class RecordPanel extends Pane
{
    public RecordPanel( RecordCircuit circuit )
    {
        // RECORD BUTTON
        Button recordBtn = new Button("Record parents");
        Button saveRecordedBtn = new Button( "Save Recorded parent");
        recordBtn.setOnMouseClicked( event -> circuit.recordParentGeneration() );
        saveRecordedBtn.setOnMouseClicked( event -> circuit.saveRecordedParents() );
    }
}
