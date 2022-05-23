package com.peacefulotter.ml.game;


import com.peacefulotter.ml.game.car.Car;
import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.utils.Loader;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;

public class Map extends StackPane
{
    // number of children before the car images
    private static final int BASE_CHILDREN = 2;

    private final Canvas canvas;
    private final GraphicsContext ctx;

    private final ImageView circuitImage;

    private boolean showDeadCars = true;

    public Map( Canvas canvas, MapParams params )
    {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

        circuitImage = new ImageView();
        circuitImage.setX( 0 );
        circuitImage.setY( 0 );
        circuitImage.setFitWidth( canvas.getWidth() );
        circuitImage.setFitHeight( canvas.getHeight() );
        circuitImage.setStyle(  "-fx-spacing: 4; -fx-padding: 3px;" );
        circuitImage.setPreserveRatio( true );

        setParams(params);

        getChildren().addAll( circuitImage, canvas );

        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();
    }

    public void setParams( MapParams params )
    {
        Car.ORIGIN_POSITION = params.getDirection();
        Car.ORIGIN_ANGLE = params.getAngle();
        Car.hitbox = new Loader().loadHitbox( params.getHitbox(), canvas.getWidth(), canvas.getHeight() );
        Image img = new Image( params.getImg() );
        circuitImage.setImage( img );
    }

    public void toggleRenderDeadCars()
    {
        showDeadCars = !showDeadCars;
    }

    public void addCarToMap( Car car )
    {
        ImageView img = car.getCarImgView();
        setAlignment( img, Pos.TOP_LEFT );
        getChildren().add( img );
    }

    public void remove( int i )
    {
        remove( i, i + 1 );
    }

    public void remove( int from, int to )
    {
        getChildren().remove( from + BASE_CHILDREN, to + BASE_CHILDREN );
    }

    public void render(List<IACar> cars)
    {
        ctx.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight() );

        for (IACar car: cars)
        {
            car.getCarImgView().setVisible( !car.isDead() || showDeadCars );
            car.render(ctx);
        }
    }

}
