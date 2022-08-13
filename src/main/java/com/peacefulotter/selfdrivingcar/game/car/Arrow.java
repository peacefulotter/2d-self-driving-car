package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Arrow
{
    private static final int LENGTH = 1;
    private static final int PADDING = 10;

    private final Matrix2d hitbox;
    private final double innerAngle;

    private Vector2d direction, origin;
    private int length = LENGTH;

    // increase or reduce pad may cause problems if the hitbox wall has 2 differences on the same side
    private static final int pad = 7;
    private static Rectangle[][] rects = new Rectangle[pad][pad];

    private Vector2d wall, reflect;

    public Arrow( Matrix2d hitbox, Vector2d direction, double innerAngle )
    {
        this.hitbox = hitbox;
        this.direction = direction.rotate( innerAngle );
        this.innerAngle = innerAngle;
        this.origin = Vector2d.getZero();
        this.wall = Vector2d.getZero();
    }

    public static void addRects( Map map )
    {
        int size = 10;
        for ( int i = 0; i < pad; i++ )
        {
            for ( int j = 0; j < pad; j++ )
            {
                Rectangle rect = new Rectangle( 0, 0, size, size );
                rect.setTranslateX( i * size - 300 );
                rect.setTranslateY( j * size - 200 );
                rect.setFill( Color.WHITE );
                rects[i][j] = rect;
                map.getChildren().add( rect );
            }
        }
    }

    private void getWallPoint( int length, int padding )
    {
        if ( length < 1 )
        {
            this.length = 1;
            return;
        } else if ( padding <= 1 ) return;

        this.length = length;
        this.direction = direction.scale( length );

        Vector2d point = direction.add( origin );
        double x = point.getX();
        double y = point.getY();
        int isRoad = 0;
        if ( x < hitbox.cols && y < hitbox.rows && x >= 0 && y >= 0 )
            isRoad = (int) hitbox.getAt( (int) y, (int) x );

        if ( isRoad == 1 )
            getWallPoint( length + padding, padding );
        else
            getWallPoint( length - padding / 2, padding / 2 );
    }

    private Vector2d calcReflection( Vector2d point )
    {
        // rectangle colors
        for ( int i = 0; i < pad; i++ )
        {
            for ( int j = 0; j < pad; j++ )
            {
                int x = (int) point.getX() + i - pad / 2;
                int y = (int) point.getY() + j - pad / 2;
                int hb = hitbox.contains( y, x )
                    ? (int) hitbox.getAt( y, x )
                    : 0;
                // Color color = hb == 1 ? Color.BLUE : Color.BLACK;
                // rects[i][j].setFill( color );
                // if ( i == pad / 2 && j == pad / 2 )
                //    rects[i][j].setFill(Color.RED);
            }
        }

        // find the 2 points to calculate the angle of the wall the arrow bounces on
        List<Vector2d> plan = Stream.of(
                checkHitbox( point, 0, 1, 0, pad ),
                checkHitbox( point, pad - 1, pad, 0, pad ),
                checkHitbox( point, 0, pad, pad - 1, pad ),
                checkHitbox( point, 0, pad, 0, 1 )
            )
            .filter( Objects::nonNull )
            .toList();

            return new Vector2d(1, 0);
//        if ( plan.size() != 2 )
//        {
//            System.out.println( plan );
//            throw new Error( "Impossible hitbox plan on arrow" );
//        }
//
//        Vector2d x = plan.get( 0 );
//        Vector2d y = plan.get( 1 );
//        Vector2d mirror = new Vector2d( 1, 0 ); // x.sub( y );
//
//        System.out.println(direction.normalize() + " " + mirror.normalize());
//
//        double k = 1; // x.dot( y ) / y.dot( y );
//        Vector2d reflectDirection = direction.mul( -1 ).add( mirror.mul( 2 * k ) );
//        return point.add( reflectDirection.normalize().mul( 50 ).rotate( 45 ) );
    }

    public void updateParams( Vector2d origin, double angle )
    {
        this.origin = origin.copy();
        this.direction = new Vector2d( 1, 0 ).rotate( angle + innerAngle );
        getWallPoint( LENGTH, PADDING );
        wall = origin.add( direction );
        // reflect = calcReflection( wall );
    }

    public void draw( GraphicsContext ctx )
    {
        double red = Math.min( 1, Math.max( 1 - (length / 255d), 0 ) );
        double green = Math.max( 0, Math.min( length / 255d, 1 ) );
        ctx.setStroke( new Color( red, green, 0.35, 0.9 ) );
        ctx.setLineWidth( 3 );
        ctx.strokeLine( origin.getX(), origin.getY(), wall.getX(), wall.getY() );

//        ctx.setStroke( new Color( 0.1, 0.1, 1.0, 1.0 ) );
//        ctx.setLineWidth( 2 );
//        ctx.strokeLine( wall.getX(), wall.getY(), reflect.getX(), reflect.getY() );
    }

    // FIXME: can return 2 vectors if the edges are on the same side
    private Vector2d checkHitbox( Vector2d point, int minI, int maxI, int minJ, int maxJ )
    {
        int prevHitbox = -1;
        for ( int i = minI; i < maxI; i++ )
        {
            for ( int j = minJ; j < maxJ; j++ )
            {
                int x = (int) point.getX() + i - pad / 2;
                int y = (int) point.getY() + j - pad / 2;
                int curHitbox = hitbox.contains( y, x )
                    ? (int) hitbox.getAt( y, x )
                    : 0;
                Color color = curHitbox == 1 ? Color.BLUE : Color.BLACK;
                rects[i][j].setFill( color );
                if ( prevHitbox != -1 && prevHitbox != curHitbox )
                {
                    rects[i][j].setFill( Color.RED );
                    return new Vector2d( x, y );
                }
                prevHitbox = curHitbox;
            }
        }
        return null;
    }

    public double getLength()
    {
        return length;
    }

    public Vector2d getDirection()
    {
        return direction.copy();
    }
}
