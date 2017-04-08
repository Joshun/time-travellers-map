package org.timetravellersmap.basemapio;

/**
 * Created by joshua on 08/04/17.
 */
public class BasemapLoaderFactory {
    public static BasemapLoader basemapLoaderFromFile(String fileExt) throws BasemapIOException {
        System.out.println("Trying to load " + fileExt);
        BasemapLoader loader;
        switch (fileExt) {
            case "shp":
                loader = new ShapefileLoader();
                break;
            case "jpeg":
            case "tiff":
            case "tif":
            case "gif":
                loader = new ImageLoader();
                break;
            default:
                throw new BasemapIOException("Unsupported file extension " + fileExt);
        }
        return loader;
    }

    public static String getExtension(String fileName) {
        int extPos = fileName.lastIndexOf(".") + 1;
        if (extPos < 0 || extPos > (fileName.length()-1)) {
            return null;
        }
        else {
            return fileName.substring(extPos);
        }
    }
}
