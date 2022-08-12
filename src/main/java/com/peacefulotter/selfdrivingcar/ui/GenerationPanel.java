package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GenerationPanel extends BorderPane
{
    private static final Label label = new Label( "Generation 1" );
    private static final Button button = new Button( "Next Gen" );

    public GenerationPanel( GeneticCircuit circuit )
    {
        label.setPadding( new Insets(5, 0, 0, 0 ) );
        circuit.getGenerationProperty().addListener( (e, o, n) -> label.setText( "Generation " + n ));
        button.setOnMouseClicked( event -> circuit.nextGeneration() );

        setLeft( label );
        setRight( button );
    }
}
