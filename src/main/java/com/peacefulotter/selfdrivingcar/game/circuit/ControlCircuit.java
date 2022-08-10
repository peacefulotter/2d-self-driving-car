package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import com.peacefulotter.selfdrivingcar.utils.CarInput;
import com.peacefulotter.selfdrivingcar.utils.Loader;

import java.util.ArrayList;
import java.util.List;

public class ControlCircuit extends Circuit
{
    private static final boolean SAVING = false;
    private static final int SAVE_THRESHOLD = 2000;

    private static final List<Matrix2d> positions = new ArrayList<>();
    private static final List<Vector2d> controls = new ArrayList<>();

    private static final IACar car = new IACar( 9, true );

    public ControlCircuit( Map map )
    {
        super( map );

        addCarToCircuit( car );
        map.setOnKeyPressed( keyEvent -> CarInput.handleKeyPressed( car, keyEvent ) );
        map.setOnKeyReleased( keyEvent -> CarInput.handleKeyReleased( car, keyEvent ) );
    }

    @Override
    public void update( double deltaTime )
    {
        // update all cars except the first one which is the controlled car
        super.update( deltaTime, 1, cars.size() );

        Matrix2d x = car.simulate();
        Vector2d y = CarInput.getVector();

        car.accelerate( y.getX() );
        car.turn( y.getY() );
        car.update( deltaTime );

        if ( SAVING )
            saveCarState(x, y);
    }

    private void saveCarState( Matrix2d x, Vector2d y )
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
    }
}
