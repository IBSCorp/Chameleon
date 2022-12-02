package ru.ibsqa.qualit.elements.db;

import ru.ibsqa.qualit.db.context.DefaultRowObject;
import ru.ibsqa.qualit.db.context.IRowObject;
import ru.ibsqa.qualit.db.context.sql.NamedParameterStatement;
import ru.ibsqa.qualit.definitions.repository.db.MetaQuery;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class DefaultResultSet implements IFacadeResultSet {

    private MetaQuery metaQuery;
    private NamedParameterStatement statement;
    private ResultSet resultSet;

    @Override
    public Iterator<IRowObject> iterator() {
        // Выбрать результат или перевыбрать результат, если он уже выбирался, но поддерживает перебор только вперед
        try {
            if (null == resultSet
                    || statement.getStatement().getFetchDirection() == ResultSet.FETCH_FORWARD
                    || statement.getStatement().getFetchDirection() == ResultSet.TYPE_FORWARD_ONLY ) {
                execute();
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(ILocaleManager.message("dbQueryExecuteErrorMessage", metaQuery.getName()));
        }
        return new ResultSetIterator(metaQuery, resultSet);
    }

    @Override
    public void initialize(MetaQuery metaQuery, NamedParameterStatement statement) {
        this.metaQuery = metaQuery;
        this.statement = statement;
    }

    private ResultSet execute() throws SQLException {
        close();
        log.debug(String.format("SQL execute query and get resultSet"));
        resultSet = statement.executeQuery();
        return resultSet;
    }

    @Override
    public void close() {
        if (null != resultSet) {
            try {
                log.debug(String.format("SQL close resultSet"));
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
    }

    @Override
    public String getFieldValue(String locator) {
        assertNotNull(resultSet, ILocaleManager.message("noCurrentQueryErrorMessage"));
        try {
            return resultSet.getString(locator);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        return "";
    }

    @Override
    public JSONArray exportToJson() {
        return null; //TODO
    }

    private static class ResultSetIterator implements Iterator<IRowObject> {

        private MetaQuery metaQuery;
        private ResultSet resultSet;
        private boolean isEmpty;

        public ResultSetIterator(MetaQuery metaQuery, ResultSet resultSet) {
            this.metaQuery = metaQuery;
            this.resultSet = resultSet;
            this.isEmpty = true;
            try {
                this.isEmpty = !resultSet.isBeforeFirst();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }

        private IRowObject nextRow;
        private boolean last;

        @Override
        public boolean hasNext() {
            if (Objects.nonNull(nextRow)) {
                return true;
            }
            try {
                if (resultSet.next()) {
                    IRowObject row = new DefaultRowObject();
                    row.initialize(metaQuery, resultSet);
                    nextRow = row;
                } else {
                    last = true;
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }

            return !last;
        }

        @Override
        public IRowObject next() {
            if (last) {
                return null;
            }
            if (Objects.isNull(nextRow)) {
                if (!hasNext()) {
                    return null;
                }
            }
            IRowObject row = nextRow;
            nextRow = null;

            return row;
        }

    }
}
