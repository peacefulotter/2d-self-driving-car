package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.game.circuit.ControlCircuit;
import com.peacefulotter.selfdrivingcar.game.map.Maps;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Compete extends Application
{
    private void addChallengerCar( Circuit circuit, String modelFilename, Color color )
    {
        IACar trainedCar = new Loader().importModel( modelFilename );
        circuit.addCarToCircuit( trainedCar );
        trainedCar.setColor( color );
    }

    @Override
    public void start( Stage stage )
    {
        Map map = DefaultStage.createMap(Maps.DEFAULT);
        ControlCircuit circuit = new ControlCircuit( map );

        addChallengerCar( circuit, "model_big_insane_crash", Color.YELLOW );
        addChallengerCar( circuit, "model_big_best", Color.RED );
        addChallengerCar( circuit, "model_big_future", Color.GREEN );

        DefaultStage.launch(stage, map, circuit);
    }
}
