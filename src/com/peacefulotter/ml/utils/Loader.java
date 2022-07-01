package com.peacefulotter.ml.utils;

import com.peacefulotter.ml.ia.NeuralNetwork;
import com.peacefulotter.ml.ia.Record;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

public class Loader
{
    private static final Color ROAD_COLOR = Color.color( 42f / 255, 170f / 255, 255f / 255, 255f / 255 );

    // an input stream to read the files
    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    public Matrix2d loadHitbox( String path, double width, double height )
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

    private void appendMatrix(JSONObject layer, NeuralNetwork nn, int i) throws JSONException
    {
        JSONArray wMatrix = new JSONArray();
        JSONArray bMatrix = new JSONArray();
        Matrix2d w = nn.getW(i);
        Matrix2d b = nn.getB(i);
        System.out.println(w);
        System.out.println(b);
        for (int j = 0; j < w.cols; j++)
        {
            JSONArray tempW = new JSONArray();
            for (int k = 0; k < w.rows; k++) {
                tempW.put( w.getAt(k, j) );
            }
            wMatrix.put( tempW );
            bMatrix.put( b.getAt(0, j ) );
        }
        layer.put( "w", wMatrix );
        layer.put( "b", bMatrix );
    }

    public void saveModel( NeuralNetwork nn )
    {
        long id = Math.round(Math.random() * 10000);

        try (PrintWriter pw = new PrintWriter("res/model_" + id + ".json"))
        {
            JSONArray json = new JSONArray();
            for (int i = 1; i < nn.getLayers(); i++)
            {
                System.out.println(i + " " + nn.getLayers() + " " + List.of(nn.getDimensions()));
                JSONObject layer = new JSONObject();
                appendMatrix(layer,  nn, i);
                json.put( layer );
            }
            pw.println(json);
        } catch ( IOException e )
        {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public static void saveRecording( int carIndex, List<Record.Sample> samples )
    {
        System.out.println(carIndex + " " + samples.size());

        try (PrintWriter pw = new PrintWriter("res/" + carIndex + "_car.csv"))
        {
            pw.println("x,y,speed,acceleration");
            samples.stream()
                .map( Record.Sample::convertToCSV  )
                .forEach( pw::println );
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}