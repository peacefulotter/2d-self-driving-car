package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BottomPanel extends BorderPane
{
    private static final String GENETIC_STYLE = "-fx-padding: 0 5 0 15";
    private static final String INFO_STYLE = "-fx-label-padding: 0 15 0 15";

    private static final Label fpsLabel = new Label( "0 fps" );;
    private static final Label genLabel = new Label( "Generation 1" );
    private static final Label popProportionLabel = new Label(  0 + " / " + 0 );

    public BottomPanel(GeneticCircuit circuit, Map map )
    {
        // setStyle( "-fx-spacing: 4; -fx-padding: 4;" );

        Button toggleRenderDeadCarButton = new Button( "Toggle render dead car" );
        Button testMapButton = new Button( "Test Map" );
        Button saveModelButton = new Button( "Save Model" );

        Label selectedParentsLabel = new Label( "0 Parents selected" );
        Label averageSpeed = new Label( "Average Speed: 0" );

        // Listeners and Bindings
        toggleRenderDeadCarButton.setOnMouseClicked( event -> map.toggleRenderDeadCars() );
        testMapButton.setOnMouseClicked( event -> {
            circuit.testGeneration();
        } );
        saveModelButton.setOnMouseClicked( event -> circuit.saveSelectedCar() );
        circuit.getSelectedParents().addListener( (elt, o, n) ->
                selectedParentsLabel.setText( n + " Parents selected" ) );
        circuit.getAverageSpeed().addListener( (elt, o, n) ->
                averageSpeed.setText( "Average Speed: " + String.format("%1$,.2f", n.doubleValue() * 100) ) );

        // Genetic Top Box
        VBox top = new VBox();
        top.setAlignment( Pos.CENTER_LEFT);
        top.setStyle( "-fx-padding: 0 0 15 0" );

        for ( int i = 0; i < top.getChildren().size(); i += 2 )
            top.getChildren().get( i ).setStyle( GENETIC_STYLE );

        // Buttons and Information Bottom Box
        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.getChildren().addAll(
            toggleRenderDeadCarButton, genLabel,
            selectedParentsLabel, popProportionLabel,
            fpsLabel, averageSpeed, testMapButton, saveModelButton
        );
        for ( Node node: bottom.getChildren() )
            node.setStyle( INFO_STYLE );

        setTop( top );
        setBottom( bottom );
    }

    public static void setFPS(double fps)
    {
        fpsLabel.setText( (int) fps + " fps" );
    }

    public static void setGen( int generation )
    {
        genLabel.setText( "Generation " + generation );
    }

    public static void setPopProportion( int alive, int dead )
    {
        popProportionLabel.setText( alive + " / " + dead );
    }
}