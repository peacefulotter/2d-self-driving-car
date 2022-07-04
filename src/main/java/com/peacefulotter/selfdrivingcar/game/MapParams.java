package com.peacefulotter.selfdrivingcar.game;

import com.peacefulotter.selfdrivingcar.maths.Vector2d;

public enum MapParams {

    DEFAULT( "map", "hitbox_map", "rewards", new Vector2d( 325, 345 ), -55 ),
    TEST( "map_test", "hitbox_map_test", null, new Vector2d( 475, 230 ), -55 + 180 );

    private final String img, hitbox, reward;
    private final Vector2d direction;
    private final double angle;

    MapParams(String img, String hitbox, String reward, Vector2d direction, double angle) {
        this.img = img;
        this.hitbox = hitbox;
        this.reward = reward;
        this.direction = direction;
        this.angle = angle;
    }

    private String get(String file) {
        return "/img/" + file + ".png";
    }

    public String getImg() { return get(img); }
    public String getHitbox() { return get(hitbox); }
    public String getReward() { return get(reward); }
    public Vector2d getDirection() { return direction; }
    public double getAngle() { return angle; }
}
