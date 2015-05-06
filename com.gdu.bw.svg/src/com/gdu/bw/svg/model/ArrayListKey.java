
package com.gdu.bw.svg.model;


/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ArrayListKey {
    private BWNode connectAnchor1;
    private BWNode connectAnchor2;

    public ArrayListKey(BWNode source,BWNode target) {
        connectAnchor1 = source;
        connectAnchor2 = target;
    }

    public BWNode getSourceAnchor() {
        return connectAnchor1;
    }

    public BWNode getTargetAnchor() {
        return connectAnchor2;
    }

    public int hashCode() {
        return connectAnchor1.hashCode() ^ connectAnchor2.hashCode();
    }

    public boolean equals(Object object) {
        boolean isEqual = false;
        ArrayListKey listKey;

        if (object instanceof ArrayListKey) {
            listKey = (ArrayListKey) object;
            BWNode lk1 = listKey.getSourceAnchor();
            BWNode lk2 = listKey.getTargetAnchor();

            isEqual =
                (lk1.equals(connectAnchor1) && lk2.equals(connectAnchor2))
                    || (lk1.equals(connectAnchor2) && lk2.equals(connectAnchor1));
        }
        return isEqual;
    }
}
