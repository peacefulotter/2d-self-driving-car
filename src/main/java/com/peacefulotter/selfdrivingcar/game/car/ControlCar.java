package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import com.peacefulotter.selfdrivingcar.utils.CarInput;

public class ControlCar extends Car
{
    public ControlCar( int nbArrows, boolean drawArrows )
    {
        super( nbArrows, 5, drawArrows );
    }

    @Override
    public void update( double deltaTime )
    {
        Vector2d y = CarInput.getVector();
        accelerate( y.getX() );
        turn( y.getY() );

        super.update( deltaTime );
    }
}
