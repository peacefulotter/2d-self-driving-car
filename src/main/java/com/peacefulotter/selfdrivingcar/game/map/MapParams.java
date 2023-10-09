package com.peacefulotter.selfdrivingcar.game.map;

import com.peacefulotter.selfdrivingcar.maths.Vector2d;

public class MapParams {

    private static final String FOLDER = "/map/";
    private final String img, hitbox, cost;
    private final Vector2d position;
    private final double angle;

    MapParams(String img, String hitbox, String cost, Vector2d position, double angle)
    {
        this.img = img;
        this.hitbox = hitbox;
        this.cost = cost;
        this.position = position;
        this.angle = angle;
    }

    private String getFilename(String file) {
        return FOLDER + file + ".png";
    }

    public String getImg() { return getFilename(img); }
    public String getHitbox() { return getFilename(hitbox); }
    public String getCost() { return getFilename(cost); }

    public Vector2d getPosition() { return position; }
    public double getAngle() { return angle; }
}
