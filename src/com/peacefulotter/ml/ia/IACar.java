package com.peacefulotter.ml.ia;

import com.peacefulotter.ml.game.Car;
import com.peacefulotter.ml.ia.activation.ActivationFunc;
import com.peacefulotter.ml.ia.activation.Activations;
import com.peacefulotter.ml.ia.loss.Loss;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.utils.Loader;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IACar extends Car
{
    public static final int[] DIMENSIONS = new int[] {7, 20, 2};
    private static final ActivationFunc[] ACTIVATIONS = new ActivationFunc[] {
            Activations.ReLU, Activations.HyperTan
    };
    private static final int ARROWS = 5;


    private NeuralNetwork nn;

    public IACar()
    {
        super(ARROWS, false);
        this.nn = new NeuralNetwork( DIMENSIONS, ACTIVATIONS );
    }

    public IACar( NeuralNetwork nn )
    {
        super(ARROWS,true);
        this.nn = nn;
    }

    public Matrix2d simulate()
    {
        Matrix2d data = new Matrix2d( 1, DIMENSIONS[0] );
        // arrows length
        int nbArrows = arrows.size();
        for (int i = 0; i < nbArrows; i++)
            data.setAt( 0, i, arrows.get( i ).getLength() );
        // additional data
        data.setAt( 0, nbArrows, speed );
        data.setAt( 0, nbArrows + 1, acceleration );
        // data.setAt( 0, nbArrows + 2, angle );
        // data.setAt( 0, nbArrows + 3, angleSpeed );

        return nn.predict( data.normalize() );
    }

    public void setNN( NeuralNetwork nn )
    {
        this.nn = nn;
    }

    public NeuralNetwork applyNNFunction( Function<Matrix2d, Matrix2d> func )
    {
        return nn.applyFunction( func );
    }

    public NeuralNetwork applyNNFunction( BiFunction<Matrix2d, Matrix2d, Matrix2d> func, IACar other )
    {
        return nn.applyFunction( func, other.nn );
    }

    public void trainIA()
    {
        HashMap<String, Matrix2d> hm = new Loader().loadDrivingData( DIMENSIONS[0], DIMENSIONS[DIMENSIONS.length - 1]);
        Matrix2d X = hm.get( "X" );
        Matrix2d Y = hm.get( "Y" );

        if ( X.cols != DIMENSIONS[ 0 ] ) throw new AssertionError();

        nn.train( X, Y, Loss.MSE, 1e-4, 250, 1, 10 );
        // nextGeneration( new ArrayList<>(1) { { add(0); } }, population );
    }
}
