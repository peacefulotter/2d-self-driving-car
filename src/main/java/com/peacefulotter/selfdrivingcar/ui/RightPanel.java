package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class RightPanel extends BorderPane
{
    public RightPanel( Circuit circuit, GeneticParams params )
    {
        Button nextGenButton = new Button( "Next Gen" );
        nextGenButton.setOnMouseClicked( event -> circuit.nextGeneration( params.getPopulation() ) );

        setBottom( nextGenButton );
    }
}
