package com.peacefulotter.ml.game;


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
    private static final String trainMapImageFile = "/img/map.png";
    private static final String testMapImageFile = "/img/map_test.png";

    private static final String trainHitboxImageFile = "/img/hitbox_map.png";
    private static final String testHitboxImageFile = "/img/hitbox_map_test.png";

    // number of children before the car images
    private static final int BASE_CHILDREN = 2;

    private final Canvas canvas;
    private final GraphicsContext ctx;

    private final ImageView circuitImage;

    private boolean showDeadCars = true;

    public Map( Canvas canvas )
    {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();

        Car.hitbox = new Loader().
                loadHitboxMap( trainHitboxImageFile, canvas.getWidth(), canvas.getHeight() );

        circuitImage = new ImageView();
        circuitImage.setX( 0 );
        circuitImage.setY( 0 );
        circuitImage.setFitWidth( canvas.getWidth() );
        circuitImage.setFitHeight( canvas.getHeight() );
        circuitImage.setStyle(  "-fx-spacing: 4; -fx-padding: 3px;" );
        circuitImage.setPreserveRatio( true );
        Image img = new Image( trainMapImageFile);
        circuitImage.setImage( img );

        getChildren().addAll( circuitImage, canvas );

        setOnMouseClicked( mouseEvent -> canvas.requestFocus() );
        canvas.requestFocus();
    }

    public void toggleRenderDeadCars()
    {
        showDeadCars = !showDeadCars;
    }


    public void addCarToMap( ImageView img )
    {
        setAlignment( img, Pos.TOP_LEFT );
        getChildren().add( img );
    }

    public void remove( int i )
    {
        getChildren().remove( i + BASE_CHILDREN );
    }

    public void remove( int from, int to )
    {
        getChildren().remove( from + BASE_CHILDREN, to + BASE_CHILDREN );
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

    public void setTestMap()
    {
        Car.hitbox = new Loader().
                loadHitboxMap( testHitboxImageFile, canvas.getWidth(), canvas.getHeight() );

        Image img = new Image( testMapImageFile );
        circuitImage.setImage( img );
    }
}
