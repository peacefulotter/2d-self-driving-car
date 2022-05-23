package com.peacefulotter.ml.utils;

import com.google.gson.*;

/** FIXME: NOT WORKING **/
public class Logger
{
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static void print( Object obj )
    {
        print( gson.toJson( obj ) );
    }

    public static void print( String str )
    {
        System.out.println( str );
    }
}
