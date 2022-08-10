package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultGenetic;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Compete extends Application
{
    @Override
    public void start( Stage stage )
    {
        IACar trainedNN = new IACar(
            new Loader().importModel("model_best" )
        );

        Map map = DefaultStage.createMap();
        Genetic genetic = DefaultGenetic.GENETIC;
        Circuit circuit = new MultiThreadCircuit( map, genetic );

        DefaultStage.launch(stage, map, circuit);
    }
}
