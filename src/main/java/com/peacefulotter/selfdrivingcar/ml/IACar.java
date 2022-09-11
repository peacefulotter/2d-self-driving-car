package com.peacefulotter.selfdrivingcar.ml;

import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.car.CarColor;
import com.peacefulotter.selfdrivingcar.ml.activation.Activations;
import com.peacefulotter.selfdrivingcar.ml.loss.Loss;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IACar extends Car
{
    private static final int ARROWS = 5;
    private static final int FUTURE_ARROWS = 4; // for both ways
    private static final boolean DRAW_ARROWS = false;

    // Neural Network specifications (hyperparameters)
    private static final int INPUT_DETAILS_SIZE = 2;
    private static final int INPUT_SIZE = ARROWS + FUTURE_ARROWS * 2 + INPUT_DETAILS_SIZE;
    private static final int OUTPUT_SIZE = 2;
    private static final int HIDDEN_LAYER_SIZE = (int) Math.ceil( (2 / 3d) * INPUT_SIZE + 2 );
    public static final int[] DIMENSIONS = {INPUT_SIZE, HIDDEN_LAYER_SIZE, OUTPUT_SIZE};
    // always ReLU(s) + HyperTan
    // TODO: generic Activations
    private static final Activations[] ACTIVATIONS = {
            Activations.ReLU, Activations.HyperTan
    };

    private NeuralNetwork nn;
    private boolean isParent, isCrossed;

    public int nbArrows, nbFutureArrows;

    public IACar()
    {
        this( DRAW_ARROWS );
    }

    public IACar( boolean drawArrows )
    {
        this( drawArrows, ARROWS, FUTURE_ARROWS );
    }

    public IACar( boolean drawArrows, int nbArrows, int nbFutureArrows )
    {
        this( drawArrows, nbArrows, nbFutureArrows, DIMENSIONS, ACTIVATIONS );
    }

    public IACar( boolean drawArrows, int nbArrows, int nbFutureArrows, int[] dimensions, Activations[] activations )
    {
        super( nbArrows, nbFutureArrows, drawArrows );
        this.nbArrows = nbArrows;
        this.nbFutureArrows = nbFutureArrows;
        this.nn = new NeuralNetwork( dimensions, activations );
    }

    public IACar( int nbArrows, int nbFutureArrows, NeuralNetwork nn )
    {
        this( DRAW_ARROWS, nbArrows, nbFutureArrows );
        this.nn = nn;
    }

    @Override
    public void resetCar() {
        super.resetCar();
        isParent = false;
        isCrossed = false;
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

    @Override
    public void update( double deltaTime )
    {
        // get the output from the NN
        Matrix2d output = simulate();
        double acc = output.getAt( 0, 0 );
        double angle = output.getAt( 0, 1 );

        accelerate( acc );
        turn( angle );
        super.update( deltaTime );
    }

    /**
     * Simulates the NeuralNetwork
     * @return the prediction of the neural network
     */
    public Matrix2d simulate()
    {
        Matrix2d data = new Matrix2d( 1, nn.getDimensions()[0] );
        // arrows length
        List<Double> arrowLengths = getArrowLengths();
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
