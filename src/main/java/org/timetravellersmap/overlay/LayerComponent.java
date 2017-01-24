package org.timetravellersmap.overlay;

import org.timetravellersmap.Annotation;

/**
 * Created by joshua on 23/01/17.
 */
public abstract class LayerComponent {
    private Annotation layerAnnotation;
    public abstract void draw();
    public abstract void displayAnnotation();

}
