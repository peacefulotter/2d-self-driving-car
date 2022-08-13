package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Arrows
{
    private static final int TOTAL_ANGLE = 180;

    private final boolean drawArrows;
    private final List<Arrow> arrows;

    public Arrows( Matrix2d hitbox, int nbArrows, boolean drawArrows, Vector2d direction )
    {
        this( hitbox, nbArrows, drawArrows, direction, TOTAL_ANGLE );
    }

    public Arrows( Matrix2d hitbox, int nbArrows, boolean drawArrows, Vector2d direction, double totalAngle )
    {
        this.arrows = new ArrayList<>();
        this.drawArrows = drawArrows;

        // initialize the arrows
        if ( nbArrows == 1 )
            this.arrows.add( new Arrow( hitbox, direction, 0 ) );
        else
            initArrows( arrows, hitbox, nbArrows, direction, totalAngle );
    }

    private void initArrows( List<Arrow> arrows, Matrix2d hitbox, int nbArrows, Vector2d dir, double totalAngle )
    {
        double baseAngle = -totalAngle / 2;
        double shiftAngle = -2 * baseAngle / ( nbArrows - 1 );
        for ( int i = 0; i < nbArrows; i++ )
        {
            double angle = baseAngle + i * shiftAngle;
            arrows.add( new Arrow( hitbox, dir, angle ) );
        }
    }

    public List<Double> getLengths()
    {
        return this.arrows.stream()
                .map(Arrow::getLength)
                .toList();
    }

    void update( Vector2d pos, double angle )
    {
        for ( Arrow arrow : arrows )
            arrow.updateParams( pos, angle );
    }

    void render( GraphicsContext ctx )
    {
        if ( !drawArrows ) return;

        for ( Arrow arrow : arrows )
            arrow.draw( ctx );
    }

    Arrow getCenterArrow()
    {
        int middleIndex = (int) Math.floor( arrows.size() / 2d );
        return arrows.get( middleIndex );
    }
}
