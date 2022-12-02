package ru.ibsqa.qualit.influx.tools;

import ru.ibsqa.qualit.influx.context.InfluxEntry;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InfluxToolsImpl implements IInfluxTools {

    public InfluxDB instanceOf(String url, String username, String password) {

        if (url == null) {
            throw new IllegalArgumentException("url не определен");
        }

        String $case = (username == null ? "0" : "1") + (password == null ? "0" : "1");

        switch ($case) {
            case "00":
                return InfluxDBFactory.connect(url);
            case "11":
                return InfluxDBFactory.connect(url, username, password);
            case "10":
                throw new IllegalArgumentException(String.format("имя пользователя [%s] присутствует, пароль не определен", username));
            default:
                throw new IllegalArgumentException("пароль присутствует, имя пользователя не определено");
        }
    }

    public Point createPoint(String measurement, InfluxEntry entry) {
        return createPoint(measurement, entry.getFields(), entry.getTags());
    }

    public Point createPoint(String measurement, Map<String, Object> fields, Map<String, String> tags) {

        Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        return builder.build();
    }

    public void writePoint(InfluxDB influx, String database, Point point, String retentionPolicy) {
        influx.write(database, retentionPolicy, point);
    }

    public void createDatabase(InfluxDB influx, String name) {

        if (!isDataBaseExists(influx, name)) {
            influx.query(new Query("create database " + name, null));
        }
    }

    public boolean isDataBaseExists(InfluxDB influx, String name) {
        return databasesList(influx).contains(name);
    }

    public List<String> databasesList(InfluxDB influx) {

        QueryResult result = influx.query(new Query("show databases", null));
        List<List<Object>> values = result.getResults().get(0).getSeries().get(0).getValues();
        return values.stream().map(list -> String.valueOf(list.get(0))).collect(Collectors.toList());
    }

}
