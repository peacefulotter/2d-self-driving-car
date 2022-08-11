package com.peacefulotter.selfdrivingcar.game.map;

import com.peacefulotter.selfdrivingcar.maths.Vector2d;

public enum Maps {

    DEFAULT( "map", "hitbox_map", new Vector2d( 325, 345 ), -55 ),
    TEST( "map_test", "hitbox_map_test", new Vector2d( 475, 230 ), -55 + 180 ); // + 180

    private final MapParams params;

    Maps(String img, String hitbox, Vector2d position, double angle) {
        this.params = new MapParams(img, hitbox, position, angle);
    }

    public MapParams getParams() {
        return params;
    }
}
