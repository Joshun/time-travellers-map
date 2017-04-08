package org.timetravellersmap.basemapio;

import org.geotools.map.Layer;

import java.io.File;

/**
 * Created by joshua on 08/04/17.
 */
public interface BasemapLoader {
    Layer loadBasemap(File file) throws BasemapIOException;
}
