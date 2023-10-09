package com.peacefulotter.selfdrivingcar.utils;

import com.peacefulotter.selfdrivingcar.maths.MatrixLambda;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.NeuralNetwork;
import com.peacefulotter.selfdrivingcar.game.car.Record;
import com.peacefulotter.selfdrivingcar.ml.activation.Activations;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Loader
{
    private interface ImgLoaderLambda {
        double apply(Matrix2d res, int i, int j, Color color);
    }

    private static final Color ROAD_COLOR = Color.color( 42f / 255, 170f / 255, 255f / 255, 255f / 255 );
    private static final Color VOID_COLOR = Color.color( 255f / 255, 42f / 255, 42f / 255, 255f / 255 );

    // an input stream to read the files
    private InputStream resourceStream( String resourceName )
    {
        return getClass().getResourceAsStream( resourceName );
    }

    // TODO: double?
    private Matrix2d loadImg( String path, double width, double height, ImgLoaderLambda func )
    {
        Image img = new Image( path, width, height, false, false );
        PixelReader reader = img.getPixelReader();
        return new Matrix2d( (int) height, (int) width )
            .applyFunc( (mat, i, j) -> func.apply(mat, i, j, reader.getColor(j, i)) );
    }

    public Matrix2d loadHitbox( String path, double width, double height )
    {
        return loadImg( path, width, height, (mat, i, j, color) ->
            color.equals( ROAD_COLOR ) ? 1 : 0
        );
    }

    public Matrix2d loadCost( String path, double width, double height )
    {
        return loadImg( path, width, height, (mat, i, j, color) ->
            color.equals( VOID_COLOR ) ? 0 : color.getGreen() // the greener the lower the cost
        );
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

        for (int j = 0; j < w.rows; j++)
        {
            JSONArray tempW = new JSONArray();
            for (int k = 0; k < w.cols; k++) {
                tempW.put( w.getAt(j, k) );
            }
            wMatrix.put( tempW );
        }
        for (int j = 0; j < w.cols; j++) {
            bMatrix.put( b.getAt(0, j ) );
        }

        layer.put( "w", wMatrix );
        layer.put( "b", bMatrix );
    }

    public void saveModel( IACar car )
    {
        NeuralNetwork nn = car.getCopyNN();
        long id = Math.round(Math.random() * 10000);

        try (PrintWriter pw = new PrintWriter("res/model_" + id + ".json"))
        {
            System.out.println("Saving model: " + id);
            JSONObject main = new JSONObject();

            JSONArray layers = new JSONArray();
            for (int i = 1; i < nn.getLayers(); i++)
            {
                JSONObject layer = new JSONObject();
                appendMatrix(layer,  nn, i);
                layers.put( layer );
            }

            main.put("arrows", car.nbArrows );
            main.put("futureArrows", car.nbFutureArrows );
            main.put("dimensions", nn.getDimensions() );
            main.put("activations", Arrays.stream(nn.getActivationFuncs())
                .map( (act) -> act.getFunc().name )
                .collect( Collectors.toList() )
            );
            main.put("layers", layers);

            pw.println(main);
        } catch ( IOException | JSONException e )
        {
            e.printStackTrace();
        }
    }

    public IACar importModel( String filename )
    {
        try
        {
            System.out.println("Loading model: " + filename);
            Path path = Paths.get("res/" + filename + ".json");
            String text = new String(Files.readAllBytes(path));
            JSONObject main = new JSONObject(text);

            int arrows = (int) main.get("arrows");
            int futureArrows = (int) main.get("futureArrows");
            JSONArray dimensions = (JSONArray) main.get("dimensions");
            JSONArray activationsName = (JSONArray) main.get("activations");
            JSONArray layers = (JSONArray) main.get("layers");

            int[] dimensionsArray = Json.toIntArray(dimensions);
            Activations[] activations = Arrays.stream(Json.toStringArray(activationsName))
                    .map(Activations::getActivation)
                    .toArray(Activations[]::new);

            NeuralNetwork network = new NeuralNetwork(dimensionsArray, activations);

            for (int i = 1; i < dimensions.length(); i++ )
            {
                JSONObject layer = (JSONObject) layers.get(i - 1);
                JSONArray weights = (JSONArray) layer.get("w");
                JSONArray biases = (JSONArray) layer.get("b");

                Matrix2d w = Json.toMatrix(weights, dimensionsArray[i - 1], dimensionsArray[i]);
                Matrix2d b = new Matrix2d(1, dimensionsArray[i]);
                b.setRow(0, Json.toDoubleArray(biases) );

                network.setW(i, w);
                network.setB(i, b);
            }

            return new IACar( arrows, futureArrows, network );
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
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