package com.peacefulotter.ml.game;

import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.utils.Loader;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import com.peacefulotter.ml.utils.Input;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;


public class Circuit extends StackPane
{
    // train one neural network before using GA
    // this uses supervised learning and gradient descent with a dataset I made playing the game
    private static final boolean TRAIN_BEFORE = true;
    private static final boolean USER_CONTROL = false;

    private static final List<Matrix2d> positions = new ArrayList<>();
    private static final List<Vector2d> controls = new ArrayList<>();
    
    private final DoubleProperty averageSpeed = new SimpleDoubleProperty(0);;

    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final Genetic genetic;
    private final List<IACar> cars;

    private int population;
    private int generation;

    public Circuit( Canvas canvas, int population,
                    SpinnerValueFactory<Double> crossRate,
                    SpinnerValueFactory<Double> mutStrength,
                    SpinnerValueFactory<Double> mutRate )
    {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.genetic = new Genetic(crossRate, mutStrength, mutRate);
        this.cars = new ArrayList<>();
        this.population = population;

        Car.hitbox = new Loader().
                loadHitboxMap(
                        "/img/hitbox_map.png",
                        canvas.getWidth(), canvas.getHeight() );

        ImageView circuit = new ImageView();
        circuit.setX( 0 );
        circuit.setY( 0 );
        circuit.setFitWidth( canvas.getWidth() );
        circuit.setFitHeight( canvas.getHeight() );
        circuit.setStyle(  "-fx-spacing: 4; -fx-padding: 3px;" );
        circuit.setPreserveRatio( true );
        Image img = new Image( "/img/map.png" );
        circuit.setImage( img );

        getChildren().addAll( circuit, canvas );
        genCars();

        setOnKeyPressed( keyEvent -> Input.handleKeyPressed(cars.get( 0 ), keyEvent) );
        setOnKeyReleased( keyEvent -> Input.handleKeyReleased(cars.get( 0 ), keyEvent) );

        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();
    }

    private void addCarToCircuit(ImageView img)
    {
        setAlignment( img, Pos.TOP_LEFT );
        getChildren().add( img );
    }

    public void genCars()
    {
        for (int i = 0; i < population; i++)
        {
            IACar car = new IACar();
            cars.add( car );
            addCarToCircuit( car.getCarImgView() );
        }
    }

    public void nextGeneration( int newPopulation )
    {
        List<Integer> indices = new ArrayList<>();
        // the selected cars become the parents of the next generation
        for (int i = 0; i < population; i++) {
            Car car = cars.get( i );
            if ( car.isSelected() )
            {
                indices.add( i );
                car.resetCar();
                car.setParent( true );
            }
        }

        // apply crossovers and mutations
        genetic.nextGeneration( cars, indices, newPopulation );

        // if new population < old population, remove unnecessary cars
        if ( newPopulation < population )
        {
            cars.subList( newPopulation, population ).clear();
            getChildren().remove( newPopulation + 2, population + 2 );
        }
        // if the new population is bigger, add the new cars to the circuit
        else if ( newPopulation > population )
            for ( int i = population; i < newPopulation; i++ )
                addCarToCircuit(cars.get( i ).getCarImgView());

        this.population = newPopulation;
        generation += 1;
    }

    public void update(float deltaTime)
    {
        if (USER_CONTROL)
        {
            // cars.get( 0 ).update( deltaTime );
            // Matrix2d x = cars.get( 0 ).getCarData();
            Vector2d y = Input.getVector();
            // positions.add( x );
            controls.add( y.copy() );
            if (positions.size() > 3500)
            {
                Loader.saveDrivingData( positions, controls );
                System.out.println("SAVED");
                positions.clear();
                controls.clear();
            }
            return;
        }

        double speed = 0;
        for (int i = 0; i < population; i++)
        {
            IACar car = cars.get( i );
            Matrix2d output = car.simulate();
            double throttle = output.getAt( 0, 0 );
            double turn = output.getAt( 0, 1 );
            car.accelerate( throttle );
            car.turn( turn );
            car.update(deltaTime);
            speed += car.getSpeed();
        }
        averageSpeed.setValue( speed / population );
    }

    public void render()
    {
        ctx.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight() );

        for (IACar car: cars)
            car.render(ctx);
    }


    public int getGeneration() { return generation; }
    public DoubleProperty getAverageSpeed() { return averageSpeed; }
}
