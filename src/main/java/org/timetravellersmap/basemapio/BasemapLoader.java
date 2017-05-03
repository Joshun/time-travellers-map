package org.timetravellersmap.basemapio;

import org.geotools.map.Layer;

import java.io.File;

/**
 * BasemapLoader: interface for loading different basemap file formats
 */
public interface BasemapLoader {
    Layer loadBasemap(File file) throws BasemapIOException;
}
