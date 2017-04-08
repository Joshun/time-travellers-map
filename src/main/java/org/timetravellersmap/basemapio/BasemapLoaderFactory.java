package org.timetravellersmap.basemapio;

/**
 * Created by joshua on 08/04/17.
 */
public class BasemapLoaderFactory {
    public static BasemapLoader basemapLoaderFromFile(String fileExt) throws BasemapIOException {
        BasemapLoader loader;
        switch (fileExt) {
            case "shp":
                loader = new ShapefileLoader();
                break;
            default:
                throw new BasemapIOException("Unsupported file extension " + fileExt);
        }
        return loader;
    }
}
