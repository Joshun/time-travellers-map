package org.timetravellersmap.sql;

import java.sql.SQLException;
import java.util.*;

import com.j256.ormlite.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.Layer;


/**
 * SQLBridge: load and save Event, LayerComponent, Layer and Descriptor data to database using an ORM
 */
public class SQLBridge {
    private ConnectionSource connectionSource;

    private Dao<Descriptor, String> descriptorDao;
    private Dao<Layer, String> layerDao;
    private Dao<Event, String> eventDao;
    private boolean initialised = false;

    public SQLBridge() {

    }
}
