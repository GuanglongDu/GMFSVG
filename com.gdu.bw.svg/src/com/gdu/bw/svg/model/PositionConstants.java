
package com.gdu.bw.svg.model;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public interface PositionConstants {


    /** None */
    int NONE = 0;

    /** Left */
    int LEFT = 1;
    /** Center (Horizontal) */
    int CENTER = 2;
    /** Right */
    int RIGHT = 4;
    /** Bit-wise OR of LEFT, CENTER, and RIGHT */
    int LEFT_CENTER_RIGHT = LEFT | CENTER | RIGHT;
    /**
     * Used to signify left alignment regardless of orientation (i.e., LTR or
     * RTL)
     */
    int ALWAYS_LEFT = 64;
    /**
     * Used to signify right alignment regardless of orientation (i.e., LTR or
     * RTL)
     */
    int ALWAYS_RIGHT = 128;

    /** Top */
    int TOP = 8;
    /** Middle (Vertical) */
    int MIDDLE = 16;
    /** Bottom */
    int BOTTOM = 32;
    /** Bit-wise OR of TOP, MIDDLE, and BOTTOM */
    int TOP_MIDDLE_BOTTOM = TOP | MIDDLE | BOTTOM;

    /** North */
    int NORTH = 1;
    /** South */
    int SOUTH = 4;
    /** West */
    int WEST = 8;
    /** East */
    int EAST = 16;

    /** A constant indicating horizontal direction */
    int HORIZONTAL = 64;
    /** A constant indicating vertical direction */
    int VERTICAL = 128;

    /** North-East: a bit-wise OR of {@link #NORTH} and {@link #EAST} */
    int NORTH_EAST = NORTH | EAST;
    /** North-West: a bit-wise OR of {@link #NORTH} and {@link #WEST} */
    int NORTH_WEST = NORTH | WEST;
    /** South-East: a bit-wise OR of {@link #SOUTH} and {@link #EAST} */
    int SOUTH_EAST = SOUTH | EAST;
    /** South-West: a bit-wise OR of {@link #SOUTH} and {@link #WEST} */
    int SOUTH_WEST = SOUTH | WEST;
    /** North-South: a bit-wise OR of {@link #NORTH} and {@link #SOUTH} */
    int NORTH_SOUTH = NORTH | SOUTH;
    /** East-West: a bit-wise OR of {@link #EAST} and {@link #WEST} */
    int EAST_WEST = EAST | WEST;

    /** North-South-East-West: a bit-wise OR of all 4 directions. */
    int NSEW = NORTH_SOUTH | EAST_WEST;


}
