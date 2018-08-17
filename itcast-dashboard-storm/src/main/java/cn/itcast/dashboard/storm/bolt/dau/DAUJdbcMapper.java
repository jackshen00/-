package cn.itcast.dashboard.storm.bolt.dau;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.tuple.ITuple;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DAUJdbcMapper implements JdbcMapper {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public List<Column> getColumns(ITuple tuple) {
        List<Column> columns = new ArrayList<Column>();
        String value = tuple.getStringByField("value");
        String[] values = StringUtils.split(value, '|');
        columns.add(new Column("userId", values[0], Types.VARCHAR));
        columns.add(new Column("visit", values[1], Types.VARCHAR));
        try {
            columns.add(new Column("date", toDateTimestamp(format.parse(values[2])), Types.TIMESTAMP));
        } catch (ParseException e) {
            e.printStackTrace();
            columns.add(new Column("date", null, Types.TIMESTAMP));
        }
        return columns;
    }

    private Timestamp toDateTimestamp(java.util.Date date) {
        if (null == date) {
            return null;
        }
        return new Timestamp(date.getTime());
    }
}