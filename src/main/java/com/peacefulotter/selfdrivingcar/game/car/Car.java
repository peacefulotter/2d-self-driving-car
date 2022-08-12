package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.game.circuit.GeneticCircuit;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.CacheHint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car
{
    private static final double CAR_LENGTH = 43.333;
    private static final double CAR_WIDE = 21.5;
    private static final double CAR_INNER_LENGTH = 39; // reduced car length
    private static final double CAR_INNER_WIDE = 17; // car wide without mirrors

    private static final double SPEED_EASE = 4;
    private static final double ANGLE_EASE = 500; // 300 USER_CONTROL;
    private static final double MAX_SPEED = 1.5;
    private static final double SLOWNESS = 0.05;
    private static final double ACCELERATION_FACTOR = 0.15;
    private static final double BRAKING_FACTOR = 0.2;
    private static final double TURN_DEGREE = 0.2;

    private static final Vector2d SHIFT_ORIGIN = new Vector2d( -CAR_LENGTH / 2, -CAR_WIDE / 2 );
    private static final Image CAR_IMG = new Image( "/img/car.png", CAR_LENGTH, CAR_WIDE, false, false );
    private static final double DEAD_OPACITY = 0.3;

    public static Matrix2d hitbox;
    public static Vector2d originPosition;
    public static double originAngle;

    private final ImageView car;
    protected final Arrows arrows;

    private Vector2d direction;
    private boolean isReset;

    protected CarColor colorEffect = CarColor.NO_COLOR;
    protected Vector2d position;
    protected double speed, acceleration, angle, angleSpeed;
    protected boolean alive, selected, colorChanged;

    public Car( int nbArrows, boolean drawArrows )
    {
        this.arrows = new Arrows( hitbox, nbArrows, drawArrows, getOriginDirection() );
        this.car = createImageView();
        resetCar();
    }

    private Vector2d getOriginDirection()
    {
        return new Vector2d( 1, 0 ).rotate( Car.originAngle );
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

        angle = originAngle;
        position = originPosition;
        direction = getOriginDirection();
        alive = true;
        selected = false;
        isReset = false;
        colorChanged = true;
        colorEffect = CarColor.NO_COLOR;

        car.setOpacity(1);
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

    private boolean checkCornerHitbox( Vector2d corner )
    {
        int x = (int) corner.getX();
        int y = (int) corner.getY();

        return x >= 0 && x < hitbox.cols &&
                y >= 0 && y < hitbox.rows &&
                hitbox.getAt( y, x ) == 1;
    }

    private boolean checkHitbox()
    {
        Vector2d topLeft = new Vector2d( CAR_INNER_LENGTH / 2d, -CAR_INNER_WIDE / 2d )
                .rotate( angle )
                .add( position );
        Vector2d topRight = new Vector2d( CAR_INNER_LENGTH / 2d, CAR_INNER_WIDE / 2d )
                .rotate( angle )
                .add( position );
        Vector2d bottomLeft = new Vector2d( -CAR_INNER_LENGTH / 2d, -CAR_INNER_WIDE / 2d )
                .rotate( angle )
                .add( position );
        Vector2d bottomRight = new Vector2d( -CAR_INNER_LENGTH / 2d, CAR_INNER_WIDE / 2d )
                .rotate( angle )
                .add( position );

        return checkCornerHitbox(topLeft) && checkCornerHitbox(topRight) &&
                checkCornerHitbox(bottomLeft) && checkCornerHitbox(bottomRight);
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

    public void update( double deltaTime )
    {
        if ( !alive ) return;

        double newSpeed = speed + ( acceleration - SLOWNESS ) * deltaTime * SPEED_EASE;
        if ( newSpeed <= MAX_SPEED && newSpeed >= 0 )
            speed = newSpeed;

        if ( angleSpeed != 0 )
        {
            double deltaAngle = angleSpeed * ANGLE_EASE * deltaTime;
            direction = direction.rotate( deltaAngle );
            angle += deltaAngle;
        }

        position = position.add( direction.mul( speed ) );

        this.arrows.update( position, angle );

        alive = checkHitbox();
        if ( !alive )
            car.setOpacity( DEAD_OPACITY );
    }

    public void render( GraphicsContext ctx )
    {
        // setting the effect of an image view each frame is expensive
        if ( colorChanged )
        {
            CarColor.setCarColor( car, colorEffect.getColor() );
            colorChanged = false;
        }

//        Color color = CarColor.interpolateColors(Color.RED, Color.GREEN, speed / MAX_SPEED );
//        CarColor.setCarColor( car, color );

        // don't update the car rendering if it is dead
        if ( isDead() ) return;

        car.setTranslateX( position.getX() + SHIFT_ORIGIN.getX() );
        car.setTranslateY( position.getY() + SHIFT_ORIGIN.getY()  );
        car.setRotate( angle );

        this.arrows.render( ctx );
    }

    private ImageView createImageView()
    {
        ImageView img = new ImageView( CAR_IMG );
        img.setClip( new ImageView(CAR_IMG) );
        img.setCache( true );
        img.setCacheHint( CacheHint.SPEED );
        img.setOnMouseClicked( ( event ) -> {
            selected = !selected;
            GeneticCircuit.addSelected(selected ? 1 : -1);
            colorEffect = selected
                    ? CarColor.SELECTED_COLOR
                    :  alive
                        ? CarColor.NO_COLOR
                        : CarColor.DEAD_COLOR;
            colorChanged = true;
        } );
        return img;
    }

    public boolean isDead()
    {
        return !alive;
    }
    public boolean isReset() { return isReset; }
}