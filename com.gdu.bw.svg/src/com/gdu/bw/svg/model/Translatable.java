
package com.gdu.bw.svg.model;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public interface Translatable {

    void performTranslate(int dx, int dy);
    
    void performScale(double factor);
}
