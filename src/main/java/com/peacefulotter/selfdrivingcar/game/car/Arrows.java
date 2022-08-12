package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Arrows
{
    private final boolean drawArrows;
    protected final List<Arrow> arrows;

    public Arrows( Matrix2d hitbox, int nbArrows, boolean drawArrows, Vector2d dir )
    {
        this.arrows = new ArrayList<>();
        this.drawArrows = drawArrows;

        // initialize the arrows
        if ( nbArrows == 1 )
            this.arrows.add( new Arrow( hitbox, dir, 0 ) );
        else
            initArrows( hitbox, nbArrows, dir );
    }

    private void initArrows(  Matrix2d hitbox, int nbArrows, Vector2d dir )
    {
        int baseAngle = -90;
        int shiftAngle = -2 * baseAngle / ( nbArrows - 1 );
        for ( int i = 0; i < nbArrows; i++ )
        {
            int angle = baseAngle + i * shiftAngle;
            this.arrows.add( new Arrow( hitbox, dir, angle ) );
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
        if ( drawArrows )
            for ( Arrow arrow : arrows )
                arrow.draw( ctx );
    }
}
