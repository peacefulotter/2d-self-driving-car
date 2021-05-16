package com.peacefulotter.ml.game;

import com.peacefulotter.ml.Main;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.utils.Loader;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;

public class Map extends StackPane
{
    private final Canvas canvas;
    private final GraphicsContext ctx;

    private boolean showDeadCars = true;

    public Map( Canvas canvas )
    {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

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

        // Used to control a car - only to test or add new features in the future
        // setOnKeyPressed( keyEvent -> Input.handleKeyPressed(cars.get( 0 ), keyEvent) );
        // setOnKeyReleased( keyEvent -> Input.handleKeyReleased(cars.get( 0 ), keyEvent) );

        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();
    }

    public void toggleRenderDeadCars()
    {
        showDeadCars = !showDeadCars;
    }


    public void addCarToMap(ImageView img)
    {
        setAlignment( img, Pos.TOP_LEFT );
        getChildren().add( img );
    }

    public void remove( int from, int to )
    {
        getChildren().remove( from, to );
    }

    protected void render( List<IACar> cars )
    {
        ctx.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight() );

        for (IACar car: cars)
        {
            car.getCarImgView().setVisible( !car.isDead() || showDeadCars );
            car.render(ctx);
        }

    }
}
