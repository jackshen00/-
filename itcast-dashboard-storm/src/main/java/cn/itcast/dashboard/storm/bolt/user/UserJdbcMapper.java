package cn.itcast.dashboard.storm.bolt.user;

import cn.itcast.dashboard.storm.bean.User;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.tuple.ITuple;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserJdbcMapper implements JdbcMapper {

    @Override
    public List<Column> getColumns(ITuple tuple) {
        List<Column> columns = new ArrayList<Column>();
        User user = (User)tuple.getValueByField("user");
        columns.add(new Column("sex", user.getSex(), Types.INTEGER));
        columns.add(new Column("vipLevel", user.getVipLevel(), Types.INTEGER));
        columns.add(new Column("birthYear", user.getBirthYear(), Types.VARCHAR));
        columns.add(new Column("province", user.getProvince(), Types.VARCHAR));
        columns.add(new Column("city", user.getCity(), Types.VARCHAR));
        columns.add(new Column("channel", user.getChannel(), Types.VARCHAR));
        columns.add(new Column("first_pay", toDateTimestamp(user.getFirstPay()), Types.TIMESTAMP));
        columns.add(new Column("first_visit", toDateTimestamp(user.getFirstVisit()), Types.TIMESTAMP));
        columns.add(new Column("latest_pay", toDateTimestamp(user.getLatestPay()), Types.TIMESTAMP));
        columns.add(new Column("latest_visit", toDateTimestamp(user.getLatestVisit()), Types.TIMESTAMP));
        columns.add(new Column("signup_time", toDateTimestamp(user.getSignupTime()), Types.TIMESTAMP));
        return columns;
    }

    private Timestamp toDateTimestamp(java.util.Date date){
        if(null == date){
            return null;
        }
        return new Timestamp(date.getTime());
    }
}