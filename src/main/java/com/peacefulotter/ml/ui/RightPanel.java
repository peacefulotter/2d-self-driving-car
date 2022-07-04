package com.peacefulotter.ml.ui;

import com.peacefulotter.ml.game.circuit.Circuit;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class RightPanel extends BorderPane
{
    public RightPanel( Circuit circuit, Spinners spinners )
    {
        Button nextGenButton = new Button( "Next Gen" );
        nextGenButton.setOnMouseClicked( event -> circuit.nextGeneration( spinners.getPopulation() ) );

        setBottom( nextGenButton );
    }
}
