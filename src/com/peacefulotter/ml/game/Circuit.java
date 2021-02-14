package com.peacefulotter.ml.game;

import com.peacefulotter.ml.Loader;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;


public class Circuit extends StackPane
{
    private static final int ARROWS = 5;
    
    private final DoubleProperty averageSpeed = new SimpleDoubleProperty(0);;

    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final Genetic genetic;
    private final List<Car> cars;
    private final Matrix2d hitboxMap;

    private int population;
    private int generation;

    public Circuit( Canvas canvas, int population )
    {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.genetic = new Genetic();
        this.cars = new ArrayList<>();
        this.population = population;

        Loader loader = new Loader();
        this.hitboxMap = loader.loadHitboxMap("/img/hitbox_map.png", (int)canvas.getWidth(), (int) canvas.getHeight() );

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

        // setOnKeyPressed( keyEvent -> Input.handleKeyPressed(cars.get( 0 ), keyEvent) );
        //setOnKeyReleased( keyEvent -> Input.handleKeyReleased(cars.get( 0 ), keyEvent) );
        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();
    }

    private void addCar()
    {
        Car car = new Car(hitboxMap, ARROWS, false);
        cars.add( car );
        setAlignment(car.getImgView(), Pos.TOP_LEFT);
        getChildren().add( car.getImgView() );
    }

    // SET CARS AND NNs
    public void genCars()
    {
        cars.clear();
        genetic.clear();

        for (int i = 0; i < population; i++)
        {
            addCar();
            genetic.createNN();
        }

    }

    public void nextGeneration( int newPopulation )
    {
        // remove the cars from the screen
        getChildren().remove( 2, 2 + cars.size() );

        List<Integer> parents = new ArrayList<>();
        // the selected cars become the parents of the next generation
        for (int i = 0; i < population; i++) {
            if ( cars.get( i ).isSelected() ) {
                parents.add( i );
            }
        }

        // clear all cars from the list and reconstruct them
        cars.clear();
        for (int i = 0; i < newPopulation; i++) { addCar(); }

        // do the crossover and mutation -> children
        genetic.nextGeneration( parents, newPopulation );
        generation += 1;
        this.population = newPopulation;
    }

    public void update(float deltaTime)
    {
        double speed = 0;
        for (int i = 0; i < population; i++)
        {
            Car car = cars.get( i );
            Matrix2d output = genetic.simulateNN( i, car.getCarData() );
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

        for (Car car: cars)
            car.render(ctx);
    }


    public int getGeneration() { return generation; }
    public DoubleProperty getAverageSpeed() { return averageSpeed; }
}
