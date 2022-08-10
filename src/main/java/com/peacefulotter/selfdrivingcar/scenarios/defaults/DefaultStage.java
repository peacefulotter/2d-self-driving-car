package com.peacefulotter.selfdrivingcar.scenarios.defaults;

import com.peacefulotter.selfdrivingcar.game.GameLoop;
import com.peacefulotter.selfdrivingcar.game.Map;
import com.peacefulotter.selfdrivingcar.game.MapParams;
import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DefaultStage {

    public static final int CIRCUIT_WIDTH = 794;
    public static final int CIRCUIT_HEIGHT = 599;

    public static Map createMap()
    {
        return new Map( CIRCUIT_WIDTH, CIRCUIT_HEIGHT, MapParams.DEFAULT);
    }

    public static void launch(Stage stage, Parent root, Circuit circuit )
    {
        stage.setResizable(false);
        stage.setScene( new Scene( root ) );
        stage.show();

        new GameLoop(circuit).start();
    }
}
