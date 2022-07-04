package com.peacefulotter.ml.game.car;

import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Arrow
{
    private final Matrix2d hitbox;
    private final double innerAngle;

    private Vector2d direction, origin;
    private int length = 1;

    public Arrow( Matrix2d hitbox, Vector2d direction, double innerAngle )
    {
        this.hitbox = hitbox;
        this.direction = direction.rotate( innerAngle );
        this.origin = new Vector2d( 0, 0 );
        this.innerAngle = innerAngle;
    }

    private void getWallPoint(int length, int padding)
    {
        if (length < 1) { this.length = 1; return; }
        else if (padding <= 1) return;

        this.length = length;
        this.direction = direction.scale(length);

        Vector2d point = direction.add( origin );
        double x = point.getX(); double y = point.getY();
        int isRoad = 0;
        if (x < hitbox.cols && y < hitbox.rows && x >= 0 && y >= 0 )
            isRoad = (int) hitbox.getAt( (int) y, (int) x );

        if ( isRoad == 1 )
            getWallPoint( length + padding, padding );
        else
            getWallPoint( length - padding / 2, padding / 2 );
    }

    public void updateParams(Vector2d origin, double angle )
    {
        this.origin = origin.copy();
        this.direction = new Vector2d( 1, 0 ).rotate( angle + innerAngle );
        getWallPoint( 1, 20 );
    }

    public void draw( GraphicsContext ctx )
    {
        Vector2d point = origin.add( direction );
        double red = Math.min(1, Math.max(1 - (length / 255d), 0));
        double green = Math.max(0, Math.min(length / 255d, 1));
        ctx.setStroke( new Color( red, green, 0.35, 0.9 ) );
        ctx.setLineWidth( 3 );
        ctx.strokeLine( origin.getX(), origin.getY(), point.getX(), point.getY() );
    }

    public double getLength() { return length; }
}
