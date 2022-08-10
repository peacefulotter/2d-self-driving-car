package com.peacefulotter.selfdrivingcar.scenarios;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.game.circuit.MultiThreadCircuit;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.map.Maps;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultGenetic;
import com.peacefulotter.selfdrivingcar.scenarios.defaults.DefaultStage;
import com.peacefulotter.selfdrivingcar.ui.Menu;
import com.peacefulotter.selfdrivingcar.ui.Spinners;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class ModelParent extends Application
{
    @Override
    public void start( Stage stage )
    {
        Loader loader = new Loader();
        String[] models = new String[] { "3505", "5224", "3149", "best" };
        List<IACar> trainedNNs = Arrays.stream(models)
                .map(m -> loader.importModel("model_" + m))
                .map(IACar::new)
                .toList();

        Map map = DefaultStage.createMap(Maps.TEST);
        Genetic genetic = DefaultGenetic.GENETIC;
        GeneticCircuit circuit = new MultiThreadCircuit( map, genetic );

        circuit.nextGeneration( trainedNNs );

        GridPane root = new Menu( map, circuit, new Spinners() );

        DefaultStage.launch(stage, root, circuit);
    }
}
