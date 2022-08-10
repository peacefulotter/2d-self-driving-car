package com.peacefulotter.selfdrivingcar.ui;

import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultGenetic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class Spinners extends GridPane
{
    private record SpinnerParams<T>(T min, T max, T origin, T margin) {}

    private static final GeneticParams params = DefaultGenetic.GENETIC.getParams();

    private static final SpinnerParams<Integer> POPULATION_PARAMS = new SpinnerParams<>(25, 5000, params.population, 25);
    private static final SpinnerParams<Double> CROSS_RATE_PARAMS = new SpinnerParams<>(0.01d, 1d, params.crossoverRate, 0.01d );
    private static final SpinnerParams<Double> MUT_STRENGTH_PARAMS = new SpinnerParams<>(0.01d, 5d, params.mutationStrength, 0.1d );
    private static final SpinnerParams<Double> MUT_RATE_PARAMS = new SpinnerParams<>(0.001d, 0.1d, params.mutationRate, 0.001d );

    private int i = 0; // keeps track on which row to put each spinner

    public Spinners()
    {
        setVgap(10);
        addIntSpinner("Population", POPULATION_PARAMS, params::setPopulation );
        addDoubleSpinner("Crossover rate", CROSS_RATE_PARAMS, params::setCrossoverRate );
        addDoubleSpinner( "Mutation Strength", MUT_STRENGTH_PARAMS, params::setMutationStrength );
        addDoubleSpinner("Mutation rate", MUT_RATE_PARAMS, params::setMutationRate );
    }


    private <T extends Number> void addSpinner( String text, Spinner<T> spinner, Consumer<T> func )
    {
        Label label = new Label( text );
        label.setPadding( new Insets(5, 10, 0, 0 ) );

        spinner.valueProperty().addListener( (e, o, n) -> func.accept(n) );
        spinner.setPrefWidth( 100 );

        BorderPane pane = new BorderPane();
        pane.setLeft( label );
        pane.setRight( spinner );
        addRow( i++, pane );
    }

    private void addIntSpinner(String text, SpinnerParams<Integer> params, Consumer<Integer> func )
    {
        Spinner<Integer> spinner = new Spinner<>( params.min, params.max, params.origin, params.margin );
        addSpinner( text, spinner, func );
    }

    private void addDoubleSpinner(String text, SpinnerParams<Double> params, Consumer<Double> func )
    {
        Spinner<Double> spinner = new Spinner<>( params.min, params.max, params.origin, params.margin );
        addSpinner( text, spinner, func );
    }

    public GeneticParams getParams() { return params; }
}
