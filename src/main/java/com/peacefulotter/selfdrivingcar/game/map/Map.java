package com.peacefulotter.selfdrivingcar.game.map;


import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;

public class Map extends StackPane
{
    private final Loader loader;
    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final ImageView circuitImage;
    private final int baseChildren; // number of children before the car images

    private boolean showDeadCars = true;

    public Map( int width, int height, Maps params )
    {
        this.loader = new Loader();
        this.canvas = new Canvas( width, height );
        this.ctx = canvas.getGraphicsContext2D();

        circuitImage = new ImageView();
        circuitImage.setX( 0 );
        circuitImage.setY( 0 );
        circuitImage.setFitWidth( canvas.getWidth() );
        circuitImage.setFitHeight( canvas.getHeight() );
        circuitImage.setPreserveRatio( true );

        setParams(params);

        getChildren().addAll( circuitImage, canvas );
        baseChildren = getChildren().size();

        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();

        // FIXME: Adding this messes with the getChildren().size() and generations thus dont work
        // Arrow.addRects(this); // TODO: remove
    }

    public void setParams( Maps map )
    {
        MapParams params = map.getParams();

        Car.originPosition = params.getPosition();
        Car.originAngle = params.getAngle();
        Car.hitbox = loader.loadHitbox( params.getHitbox(), canvas.getWidth(), canvas.getHeight() );
        Car.cost = loader.loadCost( params.getCost(), canvas.getWidth(), canvas.getHeight() );

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
        getChildren().remove( from + baseChildren, to + baseChildren );
    }

    public <CarT extends Car> void render( List<CarT> cars )
    {
        ctx.clearRect( 0, 0, canvas.getWidth(), canvas.getHeight() );

        for (CarT car: cars)
        {
            car.getCarImgView().setVisible( !car.isDead() || showDeadCars );
            car.render(ctx);
        }
    }

}
