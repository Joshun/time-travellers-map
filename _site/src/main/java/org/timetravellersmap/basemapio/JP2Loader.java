/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.basemapio;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.jp2kak.JP2KReader;
import org.geotools.coverageio.gdal.mrsid.MrSIDReader;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.styling.Style;

import java.io.File;

/**
 * JP2Loader: class for loading JPEG2000 files, requires external gdal native library
 */
public class JP2Loader extends ImageLoader {
    @Override
    public Layer loadBasemap(File file) {
        try {
            JP2KReader reader = new JP2KReader(file);

            Style rasterStyle = createRGBStyle(reader);

            return new GridReaderLayer(reader, rasterStyle);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
