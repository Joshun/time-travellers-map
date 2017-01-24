package org.timetravellersmap.overlay;

import org.timetravellersmap.Annotation;

/**
 * Created by joshua on 23/01/17.
 */
public abstract class LayerComponent {
    private Annotation annotation;
    public abstract void draw();
    public abstract void displayAnnotation();

}
