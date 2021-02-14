package com.peacefulotter.ml;

import com.peacefulotter.ml.maths.Matrix2d;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.InputStream;

public class Loader
{
    private static final Color ROAD_COLOR = Color.color( 42f / 255, 170f / 255, 255f / 255, 255f / 255 );

    // an input stream to read the files
    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public Matrix2d loadHitboxMap( String path, int width, int height ) {
        Image img = new Image( path, width, height, false, false );
        PixelReader reader = img.getPixelReader();

        Matrix2d res = new Matrix2d( height, width );
        for (int i = 0; i < height; i++)
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

}
