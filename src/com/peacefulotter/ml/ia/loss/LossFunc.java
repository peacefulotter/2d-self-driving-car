package com.peacefulotter.ml.ia.loss;

import com.peacefulotter.ml.maths.Matrix2d;

public interface LossFunc
{
    double loss( Matrix2d pred, Matrix2d y );
    Matrix2d gradient( Matrix2d pred, Matrix2d y );
}
