package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.map.Map;

import java.util.ArrayList;
import java.util.List;

public class Circuit<CarT extends Car>
{
    protected final Map map;

    protected List<CarT> cars;

    public Circuit( Map map )
    {
        this.map = map;
        this.cars = new ArrayList<>();
    }

    public void addCarToCircuit( CarT car )
    {
        cars.add( car );
        map.addCarToMap( car );
    }

    public void setCars( List<CarT> newCars )
    {
        // clear the map and add the new cars
        map.remove( 0, cars.size() );
        for ( Car car: newCars )
            map.addCarToMap( car );
        cars = newCars;
    }

    public void update( double deltaTime ) { update( deltaTime, 0, cars.size() ); }

    protected void update( double deltaTime, int from, int to )
    {
        for ( int i = from; i < to; i++ )
        {
            Car car = cars.get( i );

            if ( car.isDead() )
                continue;

            car.update( deltaTime );
        }
    }

    public void render()
    {
        map.render( cars );
    }
}
