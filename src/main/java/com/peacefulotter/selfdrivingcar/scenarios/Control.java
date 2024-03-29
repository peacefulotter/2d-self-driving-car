package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.ControlCircuit;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Control extends Application {
    @Override
    public void start(Stage stage) {
        Map map = DefaultStage.createMap();
        ControlCircuit circuit = new ControlCircuit( map );
        DefaultStage.launch(stage, map, circuit);
    }
}
