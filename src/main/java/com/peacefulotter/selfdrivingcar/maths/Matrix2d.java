package com.peacefulotter.selfdrivingcar.maths;

import java.util.*;


public class Matrix2d
{
    private final double[][] m;
    public final int rows, cols;

    public Matrix2d( int rows, int cols)
    {
        this.m = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix2d( Matrix2d mat ) {
        this.m = Arrays.copyOf( mat.m, mat.rows );
        this.rows = mat.rows;
        this.cols = mat.cols;
    }

    public Matrix2d( double[][] m )
    {
        this.m = deepCopy( m );
        this.rows = m.length;
        this.cols = m[0].length;
    }

    public static double[][] deepCopy(double[][] original) {
        final double[][] res = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            res[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return res;
    }

    public static Matrix2d applyFunc( MatrixLambda func, int rows, int cols)
    {
        Matrix2d res = new Matrix2d( rows, cols );
        return res.applyFunc( func );
    }

    public Matrix2d applyFunc( MatrixLambda func )
    {
        Matrix2d res = new Matrix2d( rows, cols );
        for ( int i = 0; i < rows; i++ )
        {
            for ( int j = 0; j < cols; j++ )
            {
                res.m[i][j] = func.apply( res, i, j );
            }
        }
        return res;
    }


    public static Matrix2d genRandom( int rows, int cols ) {
        Random r = new Random();
        return Matrix2d.applyFunc( (mat, i, j) -> r.nextGaussian(), rows, cols);
    }

    public Matrix2d transpose()
    {
        return Matrix2d.applyFunc( (mat, i, j) -> m[j][i], cols, rows);
    }

    public double mean() {
        double[] res = new double[] { 0d };
        applyFunc( (m, i, j) -> {
            res[0] += getAt( i, j );
            return 0;
        } );
        return res[0] / (rows * cols);
    }

    public double variance() { return variance(mean()); }

    public double variance(double mean)
    {
        double[] res = new double[] { 0d };
        applyFunc( (m, i, j) -> {
            res[0] += Math.pow( getAt( i, j ) - mean, 2);
            return 0;
        } );
        return res[0] / (rows * cols);
    }

    public double std() { return std(variance()); }

    public double std( double variance ) {
        return Math.sqrt( variance );
    }

    public Matrix2d normalize()
    {
        double mean = mean();
        double variance = variance(mean);
        double std = std(variance);
        if (mean == 0 && std == 0)
            return new Matrix2d( rows, cols );
        return applyFunc( (mat,i,j) -> (m[i][j] - mean) / std );
    }

    public Matrix2d plus( double a )
    {
        return Matrix2d.applyFunc(( mat, i, j) -> m[i][j] + a, rows, cols);
    }

    public Matrix2d sub( double a ) { return plus(-a); }

    public Matrix2d mul( double a )
    {
        return Matrix2d.applyFunc(( mat, i, j) -> m[i][j] * a, rows, cols);
    }

    public Matrix2d div( double a )
    {
        return mul( 1 / a );
    }

    public Matrix2d pow( double a )
    {
        return Matrix2d.applyFunc( (mat, i, j) -> Math.pow(m[i][j], a), rows, cols);
    }

    public Matrix2d plus( Matrix2d other )
    {
        if (rows >1 && other.rows == 1 && cols == other.cols)
        {
            return applyFunc( (mat, i, j) -> m[i][j] + other.m[0][j] );
        }
        else if ( rows != other.rows || cols != other.cols ) throw new AssertionError();
        return Matrix2d.applyFunc( (mat, i, j) -> m[i][j] + other.m[i][j], rows, cols);
    }

    public Matrix2d sub( Matrix2d other )
    {
        return plus(other.mul(-1));
    }

    private Matrix2d mulSameSize( Matrix2d other )
    {
        return applyFunc( (mat, i, j) -> m[i][j] * other.m[i][j] );
    }

    public Matrix2d mul( Matrix2d other )
    {
        if ( rows == other.rows && cols == other.cols )
            return mulSameSize( other );
        else if ( cols != other.rows ) throw new AssertionError();

        return Matrix2d.applyFunc( (mat, i, j) -> {
            double value = 0;
            for ( int k = 0; k < cols; k++ )
            {
                value += this.m[i][k] * other.m[k][j];
            }
            return value;
        }, rows, other.cols);
    }

    public Matrix2d dot( Matrix2d other )
    {
        return transpose().mul(other);
    }

    public Matrix2d selectRows(int a, int b) {
        Matrix2d res = new Matrix2d(b - a, cols );
        for ( int i = a; i < b; i++ )
        {
            for ( int j = 0; j < cols; j++ )
            {
                res.setAt( i - a, j, getAt( i, j ) );
            }
        }
        return res;
    }

    public Matrix2d shuffleRows() {
        Matrix2d mat = new Matrix2d(this);
        Collections.shuffle(Arrays.asList(mat.m));
        return mat;
    }

    public Matrix2d shuffleRows(int[] indices) {
        if ( indices.length != rows ) throw new AssertionError();
        return Matrix2d.applyFunc( (mat, i, j) -> m[indices[i]][j], rows, cols);
    }

    public double[] getRow( int i )
    {
        return m[i];
    }

    public double getAt(int i, int j)
    {
        return m[i][j];
    }

    public boolean contains( int i, int j )
    {
        return i >= 0 && j >= 0 && i < rows && j < cols;
    }

    public void setRow(int i, double[] row)
    {
        for (int j = 0; j < cols; j++) {
            setAt(i, j, row[j]);
        }
    }

    public void setAt(int i, int j, double val)
    {
        m[i][j] = val;
    }

    public String shape()
    {
        StringJoiner sj = new StringJoiner( ", ", "(", ")" );
        sj.add( String.valueOf( rows ) );
        sj.add( String.valueOf( cols ) );
        return sj.toString();
    }

    public String toString( int _i, int _j )
    {
        StringJoiner main = new StringJoiner("\n", "[", "]");
        for ( int i = 0; i < rows; i++ )
        {
            StringJoiner sj = new StringJoiner(", ", "[", "]");
            for ( int j = 0; j < cols; j++ )
            {
                if ( _i == i && _j == j)
                    sj.add( "X" );
                else
                    sj.add( String.valueOf( m[i][j] ) );
            }
            main.add( sj.toString() );
        }
        return main.toString();
    }

    @Override
    public String toString()
    {
        return toString( -1, -1 );
    }
}
