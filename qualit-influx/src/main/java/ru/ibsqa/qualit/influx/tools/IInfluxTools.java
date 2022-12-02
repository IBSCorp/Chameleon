package ru.ibsqa.qualit.influx.tools;

import ru.ibsqa.qualit.influx.context.InfluxEntry;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.util.List;
import java.util.Map;

public interface IInfluxTools {
    default InfluxDB instanceOf(String url) {
        return instanceOf(url, null, null);
    }

    InfluxDB instanceOf(String url, String username, String password);

    Point createPoint(String measurement, InfluxEntry entry);

    Point createPoint(String measurement, Map<String, Object> fields, Map<String, String> tags);

    void writePoint(InfluxDB influx, String database, Point point, String retentionPolicy);

    // createDatabase, isDataBaseExists добавлены потому, что в библиотеке они объявлены устаревшими.
    void createDatabase(InfluxDB influx, String name);

    boolean isDataBaseExists(InfluxDB influx, String name);

    List<String> databasesList(InfluxDB influx);

}
