package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class GenerationPanel extends BorderPane
{
    private static final Label label = new Label( "Generation 1" );
    private static final Button button = new Button( "Next Gen" );

    public GenerationPanel( GeneticCircuit circuit )
    {
        label.setPadding( new Insets(5, 0, 0, 0 ) );
        button.setOnMouseClicked( event -> circuit.nextGeneration() );

        setLeft( label );
        setRight( button );
    }

    public static void setGen( int generation )
    {
        label.setText( "Generation " + generation );
    }
}
