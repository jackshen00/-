package cn.itcast.dashboard.storm.bolt.order;

import cn.itcast.dashboard.storm.bean.Order;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.tuple.ITuple;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class OrderJdbcMapper implements JdbcMapper {

    @Override
    public List<Column> getColumns(ITuple tuple) {
        List<Column> columns = new ArrayList<Column>();
        Order order = (Order) tuple.getValueByField("order");
        columns.add(new Column("orderId", order.getOrderId(), Types.VARCHAR));
        columns.add(new Column("payment", order.getPayment(), Types.DOUBLE));
        columns.add(new Column("paymentType", order.getPaymentType(), Types.INTEGER));
        columns.add(new Column("postFee", order.getPostFee(), Types.VARCHAR));
        columns.add(new Column(" status", order.getStatus(), Types.INTEGER));
        columns.add(new Column("createTime", toDateTimestamp(order.getCreateTime()), Types.TIMESTAMP));
        columns.add(new Column("updateTime", toDateTimestamp(order.getUpdateTime()), Types.TIMESTAMP));
        columns.add(new Column("paymentTime", toDateTimestamp(order.getPaymentTime()), Types.TIMESTAMP));
        columns.add(new Column("consignTime", toDateTimestamp(order.getConsignTime()), Types.TIMESTAMP));
        columns.add(new Column("endTime", toDateTimestamp(order.getEndTime()), Types.TIMESTAMP));
        columns.add(new Column("closeTime", toDateTimestamp(order.getCloseTime()), Types.TIMESTAMP));
        columns.add(new Column("shippingName", order.getShippingName(), Types.VARCHAR));
        columns.add(new Column("shippingCode", order.getShippingCode(), Types.VARCHAR));
        columns.add(new Column("userId", order.getUserId(), Types.BIGINT));
        return columns;
    }

    private Timestamp toDateTimestamp(java.util.Date date) {
        if (null == date) {
            return null;
        }
        return new Timestamp(date.getTime());
    }
}