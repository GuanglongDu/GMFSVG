
package com.gdu.bw.svg.model;


/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class Transposer {

    private boolean enabled = false;

    /**
     * Disables transposing of inputs.
     * 
     * @since 2.0
     */
    public void disable() {
        enabled = false;
    }

    /**
     * Enables transposing of inputs.
     * 
     * @since 2.0
     */
    public void enable() {
        enabled = true;
    }

    /**
     * Returns <code>true</code> if this Transposer is enabled.
     * 
     * @return <code>true</code> if this Transposer is enabled
     * @since 2.0
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of this Transposer.
     * 
     * @param e
     *            New enabled value
     * @since 2.0
     */
    public void setEnabled(boolean e) {
        enabled = e;
    }

    /**
     * Returns a new transposed Dimension of the input Dimension.
     * 
     * @param d
     *            Input dimension being transposed.
     * @return The new transposed dimension.
     * @since 2.0
     */
    public Dimension t(Dimension d) {
        if (isEnabled())
            return d.getTransposed();
        return d;
    }

    /**
     * Returns a new transposed Insets of the input Insets.
     * 
     * @param i
     *            Insets to be transposed.
     * @return The new transposed Insets.
     * @since 2.0
     */
    public Insets t(Insets i) {
        if (isEnabled())
            return i.getTransposed();
        return i;
    }

    /**
     * Returns a new transposed Point of the input Point.
     * 
     * @param p
     *            Point to be transposed.
     * @return The new transposed Point.
     * @since 2.0
     */
    public Point t(Point p) {
        if (isEnabled())
            return p.getTransposed();
        return p;
    }

    /**
     * Returns a new transposed Rectangle of the input Rectangle.
     * 
     * @param r
     *            Rectangle to be transposed.
     * @return The new trasnposed Rectangle.
     * @since 2.0
     */
    public Rectangle t(Rectangle r) {
        if (isEnabled())
            return r.getTransposed();
        return r;
    }

}
