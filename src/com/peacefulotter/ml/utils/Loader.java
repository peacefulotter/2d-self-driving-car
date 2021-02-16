package com.peacefulotter.ml.utils;

import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Loader
{
    private static final Color ROAD_COLOR = Color.color( 42f / 255, 170f / 255, 255f / 255, 255f / 255 );

    // an input stream to read the files
    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public Matrix2d loadHitboxMap( String path, double width, double height )
    {
        Image img = new Image( path, width, height, false, false );
        PixelReader reader = img.getPixelReader();

        Matrix2d res = new Matrix2d( (int) height, (int) width );
        for ( int i = 0; i < height; i++ )
        {
            for ( int j = 0; j < width; j++ )
            {
                Color color = reader.getColor( j, i );
                if ( color.equals( ROAD_COLOR ) )
                    res.setAt( i, j, 1 );
                else
                    res.setAt( i, j, 0 );
            }
        }
        return res;
    }

    public static void saveDrivingData( List<Matrix2d> X, List<Vector2d> Y )
    {
        try (OutputStream stream = new FileOutputStream(new File("res/input_data.txt")))
        {
            for (Matrix2d x: X)
            {
                stream.write(Arrays.toString(x.getRow(0)).replaceAll( " ", "" ).getBytes());
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }

        try (OutputStream stream = new FileOutputStream(new File("res/output_data.txt")))
        {
            for (Vector2d y: Y)
            {
                stream.write(y.toString().getBytes());
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }


    public HashMap<String, Matrix2d> loadDrivingData( int inDim, int outDim )
    {
        HashMap<String, Matrix2d> res = new HashMap<>();
        String[] line;

        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( resourceStream( "/input_data.txt") ) ) )
        {
            line = stream.readLine()
                    .replaceAll("\\[", "")
                    .replaceAll("\\]",",")
                    .split( "," );
            Matrix2d X = new Matrix2d( line.length / inDim, inDim );
            for (int i = 0; i < line.length / inDim; i++)
            {
                for (int j = 0; j < inDim; j++)
                {
                    X.setAt( i ,j, Double.parseDouble(line[i * inDim + j]));
                }
            }
            res.put( "X", X );
        } catch ( IOException e )
        {
            e.printStackTrace();
        }

        try ( BufferedReader stream = new BufferedReader( new InputStreamReader( resourceStream( "/output_data.txt") ) ) )
        {
            line = stream.readLine()
                    .replaceAll( "\\(", "" )
                    .replaceAll( "\\)", "," )
                    .split( "," );
            Matrix2d Y = new Matrix2d( line.length, outDim );
            for (int i = 0; i < line.length; i++)
            {
                String[] vec = line[i].split( ":" );
                for (int j = 0; j < outDim; j++)
                    Y.setAt( i, j, Double.parseDouble( vec[j] ) );
            }
            res.put( "Y", Y );
        } catch ( IOException e )
        {
            e.printStackTrace();
        }

        return res;
    }
}