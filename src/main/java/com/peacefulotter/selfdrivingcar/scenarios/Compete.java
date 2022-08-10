package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.ControlCircuit;
import com.peacefulotter.selfdrivingcar.game.map.MapParams;
import com.peacefulotter.selfdrivingcar.game.map.Maps;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.NeuralNetwork;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.application.Application;
import javafx.stage.Stage;

public class Compete extends Application
{
    @Override
    public void start( Stage stage )
    {
        Map map = DefaultStage.createMap(Maps.TEST);
        Circuit circuit = new ControlCircuit( map );

        NeuralNetwork trainedNN = new Loader().importModel("model_best" );
        IACar trainedCar = new IACar( trainedNN );
        circuit.addCarToCircuit( trainedCar );

        DefaultStage.launch(stage, map, circuit);
    }
}
