package org.timetravellersmap.sql;

import java.sql.SQLException;
import java.util.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;


/**
 * SQLBridge: load and save Event, LayerComponent, Layer and Descriptor data to database using an ORM
 */
public class SQLBridge {
    private final static String DB_PATH = "ttm.db";
    private ConnectionSource connectionSource;

    private Dao<Descriptor, String> descriptorDao;
    private Dao<Layer, String> layerDao;
    private Dao<Event, String> eventDao;
//    private Dao<LayerComponent, String> layerComponentDao;
    private Dao<DBLayerComponent, String> dbLayerComponentDao;
    private boolean initialised = false;

    public SQLBridge() throws QueryException {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + DB_PATH);

            TableUtils.createTableIfNotExists(connectionSource, Descriptor.class);
            TableUtils.createTableIfNotExists(connectionSource, Layer.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
//            TableUtils.createTableIfNotExists(connectionSource, LayerComponent.class);
            TableUtils.createTableIfNotExists(connectionSource, DBLayerComponent.class);

            descriptorDao = DaoManager.createDao(connectionSource, Descriptor.class);
            layerDao = DaoManager.createDao(connectionSource, Layer.class);
            eventDao = DaoManager.createDao(connectionSource, Event.class);
//            layerComponentDao = DaoManager.createDao(connectionSource, LayerComponent.class);
            dbLayerComponentDao = DaoManager.createDao(connectionSource, DBLayerComponent.class);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new QueryException("Could not establish connection to database", e);
        }

    }

    public void addLayerComponent(LayerComponent layerComponent, Event event) throws QueryException {
        DBLayerComponent dbLayerComponent = new DBLayerComponent(layerComponent);
        try {
            dbLayerComponentDao.createIfNotExists(dbLayerComponent);
            event.addLayerComponent(dbLayerComponent);
            eventDao.refresh(event);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
