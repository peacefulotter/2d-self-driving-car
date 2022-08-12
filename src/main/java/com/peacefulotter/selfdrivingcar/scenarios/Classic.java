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

        System.out.println(map.getChildren().size());

        Spinners spinners = new Spinners();
        System.out.println(map.getChildren().size());

        GeneticParams params = spinners.getParams();

        System.out.println(map.getChildren().size());
        Genetic genetic = new Genetic( params );
        System.out.println(map.getChildren().size());

        GeneticCircuit circuit = new MultiThreadCircuit( map, genetic );
        System.out.println(map.getChildren().size());

        GridPane menu = new Menu( map, circuit, spinners );
        System.out.println(map.getChildren().size());

        DefaultStage.launch( stage, menu, circuit );
    }
}
