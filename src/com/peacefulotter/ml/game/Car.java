package com.peacefulotter.ml.game;

import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Car
{
    private static final int CAR_WIDTH = 43;
    private static final int CAR_HEIGHT = 21;
    private static final int CAR_POSITION_X = 425;
    private static final int CAR_POSITION_Y = 480;
    private static final int CAR_ANGLE = -55;

    private static final double CONTROLS_EASE = 6;
    private static final double MAX_SPEED = 2.5;
    private static final double SLOWNESS = 0.1;
    private static final double ACCELERATION_FACTOR = 0.2;
    private static final double BRAKING_FACTOR = 0.3;
    private static final double TURN_DEGREE = 0.15;

    private static final Vector2d DEFAULT_POSITION = new Vector2d( CAR_POSITION_X, CAR_POSITION_Y );
    private static final Vector2d DEFAULT_DIRECTION = new Vector2d( 1, 0 ).rotate( CAR_ANGLE );
    private static final Vector2d SHIFT_ORIGIN = new Vector2d( CAR_WIDTH / 2f, CAR_HEIGHT / 2f );

    private static final ColorAdjust SELECTED_COLOR = new ColorAdjust( 0.6, 1, 0.5, 1 );
    private static final ColorAdjust DEAD_COLOR = new ColorAdjust( 0.95, 0.2, 0.1, 0.4 );
    private static final ColorAdjust PARENT_COLOR = new ColorAdjust( -0.6, 0.7, 0.4, 1 );

    private static final Image CAR_IMG = new Image( "/img/car.png", CAR_WIDTH, CAR_HEIGHT, true, false );

    public static Matrix2d hitbox;

    private final ImageView car;
    private final boolean drawArrows;
    protected final List<Arrow> arrows;

    private Vector2d position, direction;
    protected double speed, acceleration, angle, angleSpeed;
    private boolean alive, selected, isParent, isReset;

    public Car( int nbArrows, boolean drawArrows )
    {
        this.arrows = new ArrayList<>();
        this.drawArrows = drawArrows;

        // initialize the 5 arrows
        int baseAngle = -90;
        int shiftAngle = -2 * baseAngle / ( nbArrows - 1 );
        for ( int i = 0; i < nbArrows; i++ )
        {
            int angle = baseAngle + i * shiftAngle;
            this.arrows.add( new Arrow( hitbox, DEFAULT_DIRECTION, angle ) );
        }

        // initialize the car image
        this.car = new ImageView( CAR_IMG );
        this.car.setOnMouseClicked( ( event ) -> {
            selected = !selected;
            Circuit.addSelectedParent(selected ? 1 : -1);

            // if ( selected ) car.setEffect( SELECTED_COLOR );
            // else  car.setEffect( null );
        } );

        resetCar();
    }

    /**
     * Resets speed, acceleration and angle speed
     */
    public void partialReset()
    {
        speed = 0;
        acceleration = 0;
        angleSpeed = 0;
        isReset = true;
    }

    /**
     * Fully resets the car to its default state
     */
    public void resetCar()
    {
        partialReset();

        angle = CAR_ANGLE;
        position = DEFAULT_POSITION;
        direction = DEFAULT_DIRECTION;
        alive = true;
        selected = false;
        isParent = false;
        isReset = false;

        car.setOpacity(1);
        car.setEffect( null );
        car.setVisible( true );
    }

    public ImageView getCarImgView()
    {
        return car;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public double getSpeed()
    {
        if ( !alive )
        {
            return 0;
        }
        return speed;
    }

    public void setParent( boolean isParent )
    {
        this.isParent = isParent;
        // if ( isParent ) car.setEffect( PARENT_COLOR );
    }


    private boolean checkHitbox()
    {
        Vector2d center = position.add( SHIFT_ORIGIN );
        int x = (int) center.getX();
        int y = (int) center.getY();
        if ( x < 0 || x >= hitbox.cols || y < 0 || y >= hitbox.rows )
            return false;
        return hitbox.getAt( y, x ) == 1;
    }

    public void accelerate( double acc )
    {
        if ( acc > 0 )
            acceleration = acc * ACCELERATION_FACTOR;
        else
            acceleration = acc * BRAKING_FACTOR;
    }

    public void turn( double angle )
    {
        angleSpeed = angle * TURN_DEGREE;
    }

    public void update( float deltaTime )
    {
        if ( !alive ) return;
        double newSpeed = speed + ( acceleration - SLOWNESS ) * deltaTime * CONTROLS_EASE;
        if ( newSpeed <= MAX_SPEED && newSpeed >= 0 )
            speed = newSpeed;

        if ( angleSpeed != 0 )
        {
            direction = direction.rotate( angleSpeed * CONTROLS_EASE );
            angle += angleSpeed * CONTROLS_EASE;
        }

        position = position.add( direction.mul( speed ) );

        for ( Arrow arrow : arrows )
            arrow.updateParams( position.add( SHIFT_ORIGIN ), angle );

        alive = checkHitbox();
        if ( !alive ) {
            // if (!selected) car.setEffect( DEAD_COLOR );
            car.setOpacity(0.4);
        }
    }

    public void render( GraphicsContext ctx )
    {
        if ( selected )
            car.setEffect( SELECTED_COLOR );
        else if ( isParent )
            car.setEffect( PARENT_COLOR );
        else if ( !alive )
            car.setEffect( DEAD_COLOR );
        else
            car.setEffect( null );

        // don't update the car rendering if it is dead
        if ( isDead() ) return;

        car.setTranslateX( position.getX() );
        car.setTranslateY( position.getY() );
        car.setRotate( angle );

        if ( drawArrows )
            for ( Arrow arrow : arrows )
                arrow.draw( ctx );
    }

    public boolean isDead()
    {
        return !alive;
    }
    public boolean isReset() { return isReset; }
}