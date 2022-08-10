package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.ui.Menu;
import com.peacefulotter.selfdrivingcar.ui.Spinners;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Classic extends Application
{
    @Override
    public void start(Stage stage)
    {
        Map map = DefaultStage.createMap();

        Spinners spinners = new Spinners();
        GeneticParams params = spinners.getParams();
        Genetic genetic = new Genetic( params );

        GeneticCircuit circuit = new MultiThreadCircuit( map, genetic );

        GridPane menu = new Menu( map, circuit, spinners );

        DefaultStage.launch( stage, menu, circuit );
    }
}
