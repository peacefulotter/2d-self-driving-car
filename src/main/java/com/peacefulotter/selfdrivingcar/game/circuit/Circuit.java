package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.ml.IACar;

import java.util.ArrayList;
import java.util.List;

public class Circuit
{
    protected final Map map;

    protected List<IACar> cars;

    public Circuit( Map map )
    {
        this.map = map;
        this.cars = new ArrayList<>();
    }

    public void addCarToCircuit( IACar car )
    {
        cars.add( car );
        map.addCarToMap( car );
    }

    public void setCars( List<IACar> newCars )
    {
        // clear the map and add the new cars
        map.remove( 0, cars.size() );
        for ( IACar car: newCars )
            map.addCarToMap( car );
        cars = newCars;
    }

    public void update( double deltaTime ) { update( deltaTime, 0, cars.size() ); }

    protected void update( double deltaTime, int from, int to )
    {
        for ( int i = from; i < to; i++ )
        {
            IACar car = cars.get( i );
            if ( car.isDead() && !car.isReset() )
            {
                car.partialReset();
                continue;
            }

            // get the output from the NN
            Matrix2d output = car.simulate();
            double throttle = output.getAt( 0, 0 );
            double turn = output.getAt( 0, 1 );

            // and apply it to the car
            car.accelerate( throttle );
            car.turn( turn );
            car.update( deltaTime );
        }
    }

    public void render()
    {
        map.render( cars );
    }
}
