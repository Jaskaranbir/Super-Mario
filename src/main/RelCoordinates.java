package main;

import static main.Main.REL_HEIGHT;

/**
 * <b>Name:</b> Jaskaranbir Dhillon <br>
 * <b>Date:</b> Thursday, April 22, 2016 <br>
 * <b>Class Description:</b> <br><br>
 * Used to calculation relative position of a point.<br>
 * Calculates the location with 224 pixel height has base.
 */
public class RelCoordinates {
    
    //------------In all methods below, "REL_HEIGHT" refers to constant equal to 224 / (Actual Game Window Height)
    
    /**
     * Gives the relative position of X-ordinate with that respective height.
     * @param x The position of x-ordinate when height of window is 224.
     * @return The position of relative x-ordinate at given height.
     */
    public static float getRelX(int x) {
        return x * REL_HEIGHT;
    }

    /**
     * Gives the relative position of Y-ordinate with that respective height.
     * @param y The position of y-abscissa when height of window is 224.
     * @return The position of relative y-abscissa at given height.
     */
    public static float getRelY(int y) {
        return y * REL_HEIGHT;
    }

    /**
     * Gives the relative position of coordinates with that respective height.
     * Intended to be used in SVG Collision Data definition Strings.
     * @param x The position of x-ordinate when height of window is 224.
     * @param y The position of y-abscissa when height of window is 224.
     * @return The String format of relative coordinates separated by comma.
     */
    public static String getRelCoords(int x, int y) {
        return (x * REL_HEIGHT) + "," + (y * REL_HEIGHT);
    }

    /**
     * Gives the relative position of coordinates with that respective height.
     * Intended to be used in SVG Collision Data definition Strings.<br>
     * Almost same as <em>getRelCoords</em> method; except provides procedure for two points instead of one.
     * @param x The position of first x-ordinate when height of window is 224.
     * @param y The position of first y-abscissa when height of window is 224.
     * @param x1 The position of second x-ordinate when height of window is 224.
     * @param y1 The position of second y-abscissa when height of window is 224.
     * @return The String format of two relative coordinates each separated by comma.
     */
    public static String getRelCoords(int x, int y, int x1, int y1) {
        // Stats with "Space" for easier separation between collision data Strings.
        return " " + (x * REL_HEIGHT) + "," + (y * REL_HEIGHT) + " " + (x1 * REL_HEIGHT) + "," + (y1 * REL_HEIGHT);
    }

    /**
     * Gives an array containing relative position of coordinates with that respective height.
     * Intened to be used for <em>generators</em> in <em>Main</em> class.
     * @param x The position of x-ordinate when height of window is 224.
     * @param y The position of y-abscissa when height of window is 224.
     * @return Array containing relative coordinates at given height.
     */
    public static float[] getRelCoordsArr(int x, int y) {
        return new float[]{(x * REL_HEIGHT) + 0.5f, y * REL_HEIGHT};
    }
    
}
