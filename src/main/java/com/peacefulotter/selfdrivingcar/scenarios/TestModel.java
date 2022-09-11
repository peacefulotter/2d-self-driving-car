package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.ml.NeuralNetwork;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultGenetic;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestModel extends Application {

    @Override
    public void start(Stage stage)
    {
        Map map = DefaultStage.createMap();
        GeneticCircuit circuit = new MultiThreadCircuit(map, DefaultGenetic.GENETIC );
        DefaultStage.launch( stage, map, circuit );
    }
}


