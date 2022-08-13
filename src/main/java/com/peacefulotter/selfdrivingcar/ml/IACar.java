package com.peacefulotter.selfdrivingcar.ml;

import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.car.CarColor;
import com.peacefulotter.selfdrivingcar.ml.activation.Activations;
import com.peacefulotter.selfdrivingcar.ml.loss.Loss;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.utils.Loader;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IACar extends Car
{
    private static final int ARROWS = 7;
    private static final boolean DRAW_ARROWS = false;

    // Neural Network specifications (hyperparameters)
    private static final int INPUT_DETAILS_LENGTH = 2;
    public static final int[] DIMENSIONS = {ARROWS + INPUT_DETAILS_LENGTH, 32, 48, 16, 2};
    // ReLUs + HyperTan always
    // TODO: generic Activations
    private static final Activations[] ACTIVATIONS = {
            Activations.ReLU, Activations.ReLU, Activations.ReLU, Activations.HyperTan
    };

    private NeuralNetwork nn;
    private boolean isParent, isCrossed;

    public IACar()
    {
        this( DRAW_ARROWS );
    }

    public IACar( boolean drawArrows )
    {
        this( drawArrows, DIMENSIONS, ACTIVATIONS );
    }

    public IACar( boolean drawArrows, int[] dimensions, Activations[] activations )
    {
        super( dimensions[0] - INPUT_DETAILS_LENGTH, drawArrows );
        this.nn = new NeuralNetwork( dimensions, activations );
    }

    public IACar( NeuralNetwork nn )
    {
        super( nn.getDimensions()[0] - INPUT_DETAILS_LENGTH, DRAW_ARROWS );
        this.nn = nn;
    }

    @Override
    public void resetCar() {
        super.resetCar();
        isParent = false;
        isCrossed = false;
    }

    @Override
    public void update(double deltaTime)
    {
        super.update(deltaTime);

        if ( !alive && !isParent && !isCrossed && !selected)
        {
            setColor( CarColor.DEAD_COLOR );
        }
    }

    public IACar setParent()
    {
        isParent = true;
        setColor( CarColor.PARENT_COLOR );
        return this;
    }

    public IACar setCrossedParent()
    {
        isCrossed = true;
        setColor( CarColor.CROSSED_PARENT );
        return this;
    }

    /**
     * Simulates the NeuralNetwork
     * @return the prediction of the neural network
     */
    public Matrix2d simulate()
    {
        Matrix2d data = new Matrix2d( 1, nn.getDimensions()[0] );
        // arrows length
        List<Double> arrowLengths = arrows.getLengths();
        int nbArrows = arrowLengths.size();
        for (int i = 0; i < nbArrows; i++)
            data.setAt( 0, i, arrowLengths.get(i) );
        // additional data
        data.setAt( 0, nbArrows, speed );
        data.setAt( 0, nbArrows + 1, acceleration );

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

    public NeuralNetwork getCopyNN() { return new NeuralNetwork( nn ); }
}
