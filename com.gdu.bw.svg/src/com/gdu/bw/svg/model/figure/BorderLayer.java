
package com.gdu.bw.svg.model.figure;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class BorderLayer {

    private int x;
    private int y;
    
    public BorderLayer(){}
    
    public BorderLayer(int x,int y){
        this.x = Math.abs(x);
        this.y = Math.abs(y);
    }

    /**
     * @return the x
     */
    public int getX() {
        return this.x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = Math.abs(x);
    }

    /**
     * @return the y
     */
    public int getY() {
        return this.y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y =  Math.abs(y);
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if(obj instanceof BorderLayer){
            BorderLayer layer = (BorderLayer)obj;
            return this.x == layer.x && this.y == layer.y;
        }
        return false;
    }
}
