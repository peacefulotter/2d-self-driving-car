package com.peacefulotter.ml.maths;

public class Vector2d
{
    private static final double TO_RADIANS = 180d / Math.PI;
    private double x, y;

    public Vector2d( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public static Vector2d getZero() { return new Vector2d( 0, 0 ); }

    public static double calcAngle( Vector2d v1, Vector2d v2 )
    {
        double actualAngle = Math.atan2( v1.getY(), v1.getX() ) - Math.atan2( v2.getY(), v2.getX() );
        return actualAngle * TO_RADIANS;
    }

    public double length()
    {
        return (double) Math.sqrt( x*x + y*y );
    }

    public double dot( Vector2d other )
    {
        return x * other.getX() + y * other.getY();
    }

    public Vector2d normalize()
    {
        double length = length();
        if ( length == 0 )
            return new Vector2d( x, y );
        return new Vector2d( x / length, y / length );
    }

    public Vector2d rotate( double angleDeg )
    {
        double rad = Math.toRadians( angleDeg );
        double cos = Math.cos( rad );
        double sin = Math.sin( rad );
        return new Vector2d( x * cos - y * sin, x * sin + y * cos );
    }

    public Vector2d add( Vector2d other )
    {
        return new Vector2d( x + other.getX(), y + other.getY() );
    }

    public Vector2d add( double r )
    {
        return new Vector2d( x + r, y + r );
    }

    public Vector2d sub( Vector2d other )
    {
        return new Vector2d( x - other.getX(), y - other.getY() );
    }


    public Vector2d sub( double r )
    {
        return new Vector2d( x - r, y - r );
    }

    public Vector2d mul( Vector2d other )
    {
        return new Vector2d( x * other.getX(), y * other.getY() );
    }

    public Vector2d mul( double r )
    {
        return new Vector2d( x * r, y * r );
    }

    public Vector2d div( Vector2d other )
    {
        return new Vector2d( x / other.getX(), y / other.getY() );
    }

    public Vector2d div( double r )
    {
        return new Vector2d( x / r, y / r );
    }

    public Vector2d abs() { return new Vector2d( Math.abs( x ), Math.abs( y ) ); }

    public Vector2d lerp( Vector2d destination, double lerpFactor )
    {
        return destination.sub( this ).mul( lerpFactor ).add( this );
    }

    public double cross( Vector2d other )
    {
        return x * other.getY() - y * other.getX();
    }

    public Vector2d scale(double length) { return normalize().mul( length ); }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX( double newX ) { x = newX; }
    public void setY( double newY ) { y = newY; }

    public void set( double x, double y ) { setX( x ); setY( y ); }
    public void set( Vector2d vector ) { set( vector.getX(), vector.getY() ); }

    @Override
    public boolean equals( Object other )
    {
        if ( !( other instanceof Vector2d ) ) return false;
        Vector2d vec = (Vector2d) other;
        return  x == vec.getX() &&
                y == vec.getY();
    }

    public Vector2d copy()
    {
        return new Vector2d( x, y );
    }

    @Override
    public String toString()
    {
        return "(" + x + ":" + y + ")";
    }
}