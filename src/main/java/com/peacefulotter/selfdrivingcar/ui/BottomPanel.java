package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.GameLoop;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BottomPanel extends GridPane
{

    public BottomPanel( GeneticCircuit circuit, Map map )
    {
        setVgap(10);

        Label fpsLabel = new Label( "0 FPS" );
        Label popProportionLabel = new Label( "0 / 0" );
        Label selectedParentsLabel = new Label( "0 Parents selected" );
        Label averageSpeed = new Label( "Average Speed: 0" );

        Button toggleRenderDeadCarButton = new Button( "Toggle render dead car" );
        Button testMapButton = new Button( "Test Map" );
        Button saveModelButton = new Button( "Save Model" );

        // Listeners and Bindings
        GameLoop.frames.addListener( (e, o, n) -> fpsLabel.setText( n + " FPS" ) );
        toggleRenderDeadCarButton.setOnMouseClicked( event -> map.toggleRenderDeadCars() );
        testMapButton.setOnMouseClicked( e -> circuit.testGeneration() );
        saveModelButton.setOnMouseClicked( event -> circuit.saveSelectedCar() );

        circuit.getPopProportion().addListener( (e, o, n) -> popProportionLabel.setText( n ) );
        circuit.getSelectedParents().addListener( (e, o, n) -> selectedParentsLabel.setText( n + " Parents selected" ) );
        circuit.getAverageSpeed().addListener( (e, o, n) -> averageSpeed.setText( "Average Speed: " + String.format("%1$,.2f", n.doubleValue() * 100) ) );

        // Buttons and Information Bottom Box
        addRow( 0, selectedParentsLabel);
        addRow( 1, popProportionLabel);
        addRow( 2, fpsLabel);
        addRow( 3, averageSpeed);
        addRow( 4, toggleRenderDeadCarButton);
        addRow( 5, testMapButton);
        addRow( 6, saveModelButton);
    }
}
