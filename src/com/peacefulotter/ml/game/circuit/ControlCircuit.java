package com.peacefulotter.ml.game.circuit;

import com.peacefulotter.ml.game.Map;
import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import com.peacefulotter.ml.utils.Input;
import com.peacefulotter.ml.utils.Loader;

import java.util.ArrayList;
import java.util.List;

public class ControlCircuit extends Circuit
{
    private static final boolean SAVING = false;
    private static final int SAVE_THRESHOLD = 2000;

    private static final List<Matrix2d> positions = new ArrayList<>();
    private static final List<Vector2d> controls = new ArrayList<>();

    private static final IACar car = new IACar( 5, true );


    public ControlCircuit( Map map )
    {
        super( map, 0, null );

        map.addCarToMap( car );
        map.setOnKeyPressed( keyEvent -> Input.handleKeyPressed( car, keyEvent ) );
        map.setOnKeyReleased( keyEvent -> Input.handleKeyReleased( car, keyEvent ) );
    }

    @Override
    protected void update( float deltaTime, int from, int to )
    {
        Matrix2d x = car.simulate();
        Vector2d y = Input.getVector();

        car.accelerate( y.getX() );
        car.turn( y.getY() );
        car.update( deltaTime );

        if ( SAVING )
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

    @Override
    public void render()
    {
        map.render( List.of( car ) );
    }
}
