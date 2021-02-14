package com.peacefulotter.ml.ia;

import com.peacefulotter.ml.ia.activation.ActivationFunc;
import com.peacefulotter.ml.ia.loss.LossFunc;
import com.peacefulotter.ml.maths.Matrix2d;

import java.util.*;

public class NeuralNetwork
{
    private final int layers;

    private final HashMap<Integer, Matrix2d> w;
    private final HashMap<Integer, Matrix2d> b;
    private final HashMap<Integer, ActivationFunc> activations;

    public NeuralNetwork( int[] dimensions, ActivationFunc[] activations )
    {
        this.layers = dimensions.length;

        this.w = new HashMap<>();
        this.b = new HashMap<>();
        this.activations = new HashMap<>();

        for (int i = 1; i < this.layers; i++)
        {
            this.w.put(i, Matrix2d.genRandom(dimensions[i - 1], dimensions[i]).div((float)Math.sqrt(dimensions[i-1])));
            this.b.put(i, new Matrix2d(1, dimensions[i]));
            this.activations.put(i + 1, activations[i - 1] );
        }
    }

    public Matrix2d predict(Matrix2d x)
    {
        HashMap<String, HashMap<Integer, Matrix2d>> f = forward( x );
        HashMap<Integer, Matrix2d> a = f.get( "a" );
        return a.get( layers );
    }

    public HashMap<String, HashMap<Integer, Matrix2d>> forward( Matrix2d x ) {
        HashMap<Integer, Matrix2d> z = new HashMap<>();
        HashMap<Integer, Matrix2d> a = new HashMap<>();
        a.put( 1, x );

        for (int i = 1; i < layers; i++)
        {
            z.put( i + 1, a.get( i ).mul( w.get( i ) ).plus( b.get( i ) ) );
            a.put( i + 1, activations.get( i + 1 ).forward(z.get(i + 1)));
        }
        return new HashMap<>()
        {{
            put( "z", z );
            put( "a", a );
        }};
    }

    private HashMap<String, Matrix2d> insertParam(Matrix2d dw, Matrix2d delta) {
        HashMap<String, Matrix2d> param = new HashMap<>();
        param.put( "dw", dw );
        param.put( "delta", delta );
        return param;
    }

    public HashMap<Integer, HashMap<String, Matrix2d>> back_prop( LossFunc loss, HashMap<Integer, Matrix2d> z, HashMap<Integer, Matrix2d> a, Matrix2d y) {
        Matrix2d pred = a.get( layers );
        Matrix2d delta = loss.gradient(pred, y).mul(activations.get(layers).gradient(pred));
        Matrix2d dw = a.get( layers - 1 ).transpose().dot( delta );

        HashMap<Integer, HashMap<String, Matrix2d>> deltaParams = new HashMap<>();
        deltaParams.put( layers - 1, insertParam( dw, delta ) );

        for (int i = layers - 1; i >= 2; i--)
        {
            delta = delta.dot(w.get(i).transpose()).mul(activations.get(i).gradient(z.get(i)));
            dw = a.get( i - 1 ).transpose().dot( delta );
            deltaParams.put( i - 1, insertParam( dw, delta ) );
        }

        return deltaParams;
    }

    private void updateWeights(int i, double lr, HashMap<String, Matrix2d> params ) {
        w.get( i ).sub( params.get( "dw" ).mul( lr ) );
        b.get( i ).sub( params.get( "delta" ).mul( lr ) );
    }

    public Matrix2d trainOnce( Matrix2d x, Matrix2d y, LossFunc criterion, double lr )
    {
        HashMap<String, HashMap<Integer, Matrix2d>> f = forward( x );
        HashMap<Integer, Matrix2d> z = f.get( "z" );
        HashMap<Integer, Matrix2d> a = f.get( "a" );

        HashMap<Integer, HashMap<String, Matrix2d>> newParams = back_prop( criterion, z, a, y );
        for ( Integer k: newParams.keySet() )
        {
            updateWeights( k, lr, newParams.get(k) );
        }

        return a.get( layers );
    }

    private int[] generateRandomIndices(int length) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < length; i++ )
            l.add( i );
        Collections.shuffle( l );
        int[] res = new int[length];
        for (int i = 0; i < length; i++)
            res[i] = l.get( i );
        return res;
    }


    public void train(Matrix2d x, Matrix2d y, LossFunc criterion, double lr, int epochs, int batchSize, int printPeriod )
    {
        assert x.rows == y.rows;

        for ( int epoch = 0; epoch < epochs; epoch++ )
        {
            int[] indices = generateRandomIndices(x.rows);
            Matrix2d shuffleX = x.shuffleRows(indices);
            Matrix2d shuffleY = y.shuffleRows(indices);

            for ( int i = 0; i < x.rows / batchSize; i++ )
            {
                int k = i * batchSize;
                int l = k + batchSize;
                Matrix2d trainX = shuffleX.selectRows( k, l );
                Matrix2d trainY = shuffleY.selectRows( k, l );
                trainOnce( trainX, trainY, criterion, lr );
            }

            if ( (epoch + 1) % printPeriod == 0 )
            {
                Matrix2d pred = predict(x);
                double loss = criterion.loss(pred, y);
                System.out.println("Loss at epoch" + epoch + "/" + epochs + ": " + loss);
            }
        }
    }

    public Matrix2d getW( int i )
    {
        return w.get( i );
    }

    public Matrix2d getB( int i )
    {
        return b.get( i );
    }

    public void setW( int i, Matrix2d val )
    {
        w.put( i, val );
    }

    public void setB( int i, Matrix2d val )
    {
        b.put( i, val );
    }
}
