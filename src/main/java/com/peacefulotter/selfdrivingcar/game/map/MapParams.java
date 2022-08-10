package com.peacefulotter.selfdrivingcar.game.map;

import com.peacefulotter.selfdrivingcar.maths.Vector2d;

public class MapParams {

    private final String img, hitbox;
    private final Vector2d position;
    private final double angle;

    MapParams(String img, String hitbox, Vector2d position, double angle)
    {
        this.img = img;
        this.hitbox = hitbox;
        this.position = position;
        this.angle = angle;
    }

    private String getFilename(String file) {
        return "/img/" + file + ".png";
    }

    public String getImg() { return getFilename(img); }
    public String getHitbox() { return getFilename(hitbox); }
    public Vector2d getPosition() { return position; }
    public double getAngle() { return angle; }
}
