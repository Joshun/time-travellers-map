package org.timetravellersmap.sql;

import java.sql.SQLException;
import java.util.*;

import com.j256.ormlite.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.Layer;


/**
 * SQLBridge: load and save Event, LayerComponent, Layer and Descriptor data to database using an ORM
 */
public class SQLBridge {
    private final static String DB_PATH = "ttm.db";
    private ConnectionSource connectionSource;

    private Dao<Descriptor, String> descriptorDao;
    private Dao<Layer, String> layerDao;
    private Dao<Event, String> eventDao;
    private boolean initialised = false;

    public SQLBridge() throws QueryException {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + DB_PATH);

            TableUtils.createTableIfNotExists(connectionSource, Descriptor.class);
            TableUtils.createTableIfNotExists(connectionSource, Layer.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);

            descriptorDao = DaoManager.createDao(connectionSource, Descriptor.class);
            layerDao = DaoManager.createDao(connectionSource, Layer.class);
            eventDao = DaoManager.createDao(connectionSource, Event.class);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new QueryException("Could not establish connection to database", e);
        }
    }
}
