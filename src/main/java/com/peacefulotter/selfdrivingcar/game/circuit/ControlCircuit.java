package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.car.ControlCar;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import com.peacefulotter.selfdrivingcar.utils.CarInput;
import com.peacefulotter.selfdrivingcar.utils.Loader;

import java.util.ArrayList;
import java.util.List;

public class ControlCircuit extends Circuit<Car>
{
    private static final boolean SAVING = false;
    private static final int SAVE_THRESHOLD = 2000;

    private static final List<Matrix2d> positions = new ArrayList<>();
    private static final List<Vector2d> controls = new ArrayList<>();

    private static final ControlCar car = new ControlCar( 5, true );

    public ControlCircuit( Map map )
    {
        super( map );

        addCarToCircuit( car );
        map.setOnKeyPressed( keyEvent -> CarInput.handleKeyPressed( car, keyEvent ) );
        map.setOnKeyReleased( keyEvent -> CarInput.handleKeyReleased( car, keyEvent ) );
    }

    // FIXME: move this to a proper recording system like RecordCar
    // FIXME: need to differentiate between Recording for plotting the best path and recording to train the NN later..
    // ... update(deltaTime, 1, cars.size())
    /*if ( SAVING )
    {
        Matrix2d x = car.simulate();
        saveCarState(x, y);
    }*/

    /*private void saveCarState( Matrix2d x, Vector2d y )
    {
        positions.add( x );
        controls.add( y.copy() );

        if ( positions.size() > SAVE_THRESHOLD )
        {
            Loader.saveDrivingData( positions, controls );
            System.out.println("SAVED");
            positions.clear();
            controls.clear();
        }
    }*/
}
