package com.peacefulotter.ml.ia.activation;

import com.peacefulotter.ml.maths.Matrix2d;

public interface ActivationFunc
{
    Matrix2d forward(Matrix2d z);
    Matrix2d gradient(Matrix2d z);
}
