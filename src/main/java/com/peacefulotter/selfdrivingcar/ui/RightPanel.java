package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class RightPanel extends BorderPane
{
    public RightPanel( GeneticCircuit circuit )
    {
        Button nextGenButton = new Button( "Next Gen" );
        nextGenButton.setOnMouseClicked( event -> circuit.nextGeneration() );

        setBottom( nextGenButton );
    }
}
