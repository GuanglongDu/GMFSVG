
package com.gdu.bw.svg.model;







/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class PointList implements java.io.Serializable,Translatable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int[] points = new int[0];
    private Rectangle bounds;
    private int size = 0;
    
    public PointList() {
    }
    
    public PointList(int points[]) {
        this.points = points;
        this.size = points.length / 2;
    }
    
    public PointList(int size) {
        points = new int[size * 2];
    }
    
    public void addAll(PointList source) {
        ensureCapacity(size + source.size);
        System.arraycopy(source.points, 0, points, size * 2, source.size * 2);
        size += source.size;
    }
    
    public void addPoint(Point p) {
        addPoint(p.x, p.y);
    }

    public void addPoint(int x, int y) {
        bounds = null;
        int index = size * 2;
        ensureCapacity(size + 1);
        points[index] = x;
        points[index + 1] = y;
        size++;
    }
    
    private void ensureCapacity(int newSize) {
        newSize *= 2;
        if (points.length < newSize) {
            int old[] = points;
            points = new int[Math.max(newSize, size * 4)];
            System.arraycopy(old, 0, points, 0, size * 2);
        }
    }
    
    public Rectangle getBounds() {
        if (bounds != null)
            return bounds;
        bounds = new Rectangle();
        if (size > 0) {
            bounds.setLocation(getPoint(0));
            for (int i = 0; i < size; i++)
                bounds.union(getPoint(i));
        }
        return bounds;
    }
    
    public PointList getCopy() {
        PointList result = new PointList(size);
        System.arraycopy(points, 0, result.points, 0, size * 2);
        result.size = size;
        result.bounds = null;
        return result;
    }
    
    public Point getFirstPoint() {
        return getPoint(0);
    }

    public Point getLastPoint() {
        return getPoint(size - 1);
    }
    
    public Point getPoint(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;
        return new Point(points[index], points[index + 1]);
    }
    
    public Point getPoint(Point p, int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;
        p.x = points[index];
        p.y = points[index + 1];
        return p;
    }

    public void insertPoint(Point p, int index) {
        if (bounds != null && !bounds.contains(p))
            bounds = null;
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        index *= 2;

        int length = points.length;
        int old[] = points;
        points = new int[length + 2];
        System.arraycopy(old, 0, points, 0, index);
        System.arraycopy(old, index, points, index + 2, length - index);

        points[index] = p.x;
        points[index + 1] = p.y;
        size++;
    }
    
    public int size() {
        return size;
    }
    
    public Point removePoint(int index) {
        bounds = null;
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$

        index *= 2;
        Point pt = new Point(points[index], points[index + 1]);
        if (index != size * 2 - 2)
            System.arraycopy(points, index + 2, points, index, size * 2 - index
                    - 2);
        size--;
        return pt;
    }
    
    public void removeAllPoints() {
        bounds = null;
        size = 0;
    }
    public void setPoint(Point pt, int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + //$NON-NLS-1$
                    ", Size: " + size); //$NON-NLS-1$
        if (bounds != null && !bounds.contains(pt))
            bounds = null;
        points[index * 2] = pt.x;
        points[index * 2 + 1] = pt.y;
    }
    
    public void setSize(int newSize) {
        if (points.length > newSize * 2) {
            size = newSize;
            return;
        }
        int[] newArray = new int[newSize * 2];
        System.arraycopy(points, 0, newArray, 0, points.length);
        points = newArray;
        size = newSize;
    }
    
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for(int i=0;i<this.size;i++){
            buffer.append("["+(getPoint(i).x+10)+","+(getPoint(i).y+10)+"]");
            if(i != size-1){
                buffer.append(",");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
    
    public void performScale(double factor) {
        for (int i = 0; i < points.length; i++)
            points[i] = (int) Math.floor(points[i] * factor);
        bounds = null;
    }

  
    public void performTranslate(int dx, int dy) {
        for (int i = 0; i < size * 2; i += 2) {
            points[i] += dx;
            points[i + 1] += dy;
        }
        if (bounds != null)
            bounds.translate(dx, dy);
    }
}
