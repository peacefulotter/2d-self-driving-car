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

    private static final double MAX_SPEED = 1.5;
    private static final double SLOWNESS = 0.1;
    private static final double ACCELERATION_FACTOR = 0.2;
    private static final double BRAKING_FACTOR = 0.3;
    private static final double TURN_DEGREE = 0.15;

    private static final Vector2d SHIFT_ORIGIN = new Vector2d( CAR_WIDTH / 2f, CAR_HEIGHT / 2f );

    private static final ColorAdjust SELECTED_COLOR = new ColorAdjust(8, 1, 0.9, 1);
    private static final ColorAdjust DEAD_COLOR = new ColorAdjust(1, 1, 0.2, 1);

    private final ImageView car;

    private final Matrix2d hitbox;
    private final List<Arrow> arrows;
    private final boolean drawArrows;

    private double speed, acceleration, angle, angleSpeed;
    private Vector2d position, direction;
    private boolean alive, selected;

    public Car( Matrix2d hitbox, int nbArrows, boolean drawArrows)
    {
        this.hitbox = hitbox;
        this.arrows = new ArrayList<>();
        this.drawArrows = drawArrows;

        this.speed = 0;
        this.acceleration = 0;
        this.position = new Vector2d( CAR_POSITION_X, CAR_POSITION_Y );
        this.direction = new Vector2d( 1, 0 ).rotate( CAR_ANGLE );
        this.angle = CAR_ANGLE;
        this.angleSpeed = 0;

        this.alive = true;
        this.selected = false;

        int baseAngle = -90;
        int shiftAngle = -2 * baseAngle / (nbArrows - 1);
        for (int i = 0; i < nbArrows; i++)
        {
            int angle = baseAngle + i * shiftAngle;
            this.arrows.add( new Arrow( hitbox, direction, angle ) );
        }

        Image img = new Image( "/img/car.png", CAR_WIDTH, CAR_HEIGHT, true, false );
        this.car = new ImageView( img );
        this.car.setOnMouseClicked( (event) -> selected = !selected );

        updatePos();
    }

    // arrows length, speed, angle, angleSpeed
    public Matrix2d getCarData()
    {
        int nbArrows = arrows.size();
        Matrix2d data = new Matrix2d( 1, Genetic.DIMENSIONS[0] );
        for (int i = 0; i < nbArrows; i++)
            data.setAt( 0, i, arrows.get( i ).getLength() );
        data.setAt( 0, nbArrows, speed );
        data.setAt( 0, nbArrows + 1, acceleration );
        data.setAt( 0, nbArrows + 2, angle );
        data.setAt( 0, nbArrows + 3, angleSpeed );
        return data.normalize();
    }

    public ImageView getImgView()
    {
        return car;
    }

    private boolean checkHitbox()
    {
        Vector2d center = position.add( SHIFT_ORIGIN );
        int x = (int) center.getX(); int y = (int) center.getY();
        if (x < 0 || x >= hitbox.cols || y < 0 || y >= hitbox.rows)
            return false;
        return hitbox.getAt( y, x ) == 1;
    }

    public void accelerate(double acc)
    {
        if (acc > 0)
            acceleration = acc * ACCELERATION_FACTOR;
        else
            acceleration = acc * BRAKING_FACTOR;
    }

    public void turn(double angle)
    {
        angleSpeed = angle * TURN_DEGREE;
    }

    private void updatePos()
    {
        car.setTranslateX( position.getX() );
        car.setTranslateY( position.getY() );
        car.setRotate( angle );
    }

    public void update(float deltaTime)
    {
        if (!alive) return;

        double newSpeed = speed + (acceleration - SLOWNESS) * deltaTime;
        if (newSpeed <= MAX_SPEED && newSpeed >= 0)
            speed = newSpeed;

        if (angleSpeed != 0)
        {
            direction = direction.rotate( angleSpeed );
            angle += angleSpeed;
            this.car.setRotate( angle );
        }

        position = position.add( direction.mul( speed ) );
        updatePos();

        for ( Arrow arrow: arrows )
            arrow.updateParams( position.add( SHIFT_ORIGIN ), angleSpeed );

        alive = checkHitbox();
    }

    public void render( GraphicsContext ctx )
    {
        if (selected)
        {
            car.setEffect( SELECTED_COLOR );
        } else if (!alive) {
            car.setEffect( DEAD_COLOR );
        } else {
            car.setEffect( null );
        }

        if (drawArrows)
            for (Arrow arrow: arrows)
                arrow.draw( ctx );
    }

    public boolean isSelected() { return selected; }
    public double getSpeed() {
        if (!alive) { return 0; }
        return speed;
    }
}
