package org.timetravellersmap.basemapio;

/**
 * BasemapLoaderFactory: factory for providing the right constructor given file format
 */
public class BasemapLoaderFactory {
    private static final String[] SUPPORTED_FORMATS = { "shp", "tiff", "tif" };
    private static final String[] SUPPORTED_FORMATS_EXPERIMENTAL = { "jp2" };

    public static BasemapLoader basemapLoaderFromFile(String fileExt) throws BasemapIOException {
        System.out.println("Trying to load " + fileExt);
        BasemapLoader loader;
        switch (fileExt) {
            case "shp":
                loader = new ShapefileLoader();
                break;
            case "tiff":
            case "tif":
                loader = new ImageLoader();
                break;
            case "jp2":
                loader = new JP2Loader();
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

    public static String getFilebrowserDescription(boolean allowExperimentalFormats) {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<SUPPORTED_FORMATS.length; i++) {
            sb.append("*.");
            sb.append(SUPPORTED_FORMATS[i]);
            if (i < SUPPORTED_FORMATS.length-1) {
                sb.append(", ");
            }
        }
        if (allowExperimentalFormats) {
            sb.append(", ");

            for (int i=0; i<SUPPORTED_FORMATS_EXPERIMENTAL.length; i++) {
                sb.append("*.");
                sb.append(SUPPORTED_FORMATS_EXPERIMENTAL[i]);
                if (i < SUPPORTED_FORMATS_EXPERIMENTAL.length-1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }

    public static String[] getSupportedFormats(boolean allowExperimentalFormats) {
        if (allowExperimentalFormats) {
            String[] all = new String[SUPPORTED_FORMATS.length + SUPPORTED_FORMATS_EXPERIMENTAL.length];
            System.arraycopy(SUPPORTED_FORMATS, 0, all, 0, SUPPORTED_FORMATS.length);
            System.arraycopy(SUPPORTED_FORMATS_EXPERIMENTAL, 0, all, SUPPORTED_FORMATS.length, SUPPORTED_FORMATS_EXPERIMENTAL.length);
            return all;
        }
        else {
            return SUPPORTED_FORMATS;
        }
    }
}
