package com.peacefulotter.selfdrivingcar.utils;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import org.json.JSONArray;
import org.json.JSONException;


public class Json {

    public static int[] toIntArray(JSONArray json)
    {
        int length = json.length();
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                arr[i] = json.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public static double[] toDoubleArray(JSONArray json)
    {
        int length = json.length();
        double[] arr = new double[length];
        for (int i = 0; i < length; i++) {
            try {
                arr[i] = json.getDouble(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public static String[] toStringArray(JSONArray json)
    {
        int length = json.length();
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            try {
                arr[i] = json.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public static Matrix2d toMatrix(JSONArray json, int rows, int cols) throws JSONException {
        Matrix2d matrix = new Matrix2d(rows, cols);
        for (int i = 0; i < json.length(); i++) {
            double[] col = toDoubleArray( (JSONArray) json.get(i) );
            for (int j = 0; j < cols; j++) {
                matrix.setAt(i, j, col[j]);
            }
        }
        return matrix;
    }
}
