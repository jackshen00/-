package cn.itcast.dashboard.storm.jdbc;

import cn.itcast.dashboard.storm.bolt.dau.DAUJdbcMapper;
import cn.itcast.dashboard.storm.bolt.order.OrderJdbcMapper;
import cn.itcast.dashboard.storm.bolt.user.UserJdbcMapper;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.topology.IRichBolt;

import java.util.HashMap;
import java.util.Map;

public class JdbcBoltBuilder {

    private static ConnectionProvider connectionProvider;

    static {
        // 定义数据库连接信息
        Map<String, Object> hikariConfigMap = new HashMap<String, Object>();
        hikariConfigMap.put("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://hadoop1:3306/dashboard??useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true");
        hikariConfigMap.put("dataSource.user", "root");
        hikariConfigMap.put("dataSource.password", "root");
        connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);
    }

    //构造方法私有化
    private JdbcBoltBuilder() {

    }

    public static IRichBolt buildOrderBolt() {
        return new JdbcInsertBolt(connectionProvider, new OrderJdbcMapper())
                .withInsertQuery("INSERT INTO `tb_order` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
                .withQueryTimeoutSecs(30);
    }

    public static IRichBolt buildUserBolt() {
        return new JdbcInsertBolt(connectionProvider, new UserJdbcMapper())
                .withInsertQuery("INSERT INTO `tb_user` VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?)")
                .withQueryTimeoutSecs(30);
    }

    public static IRichBolt buildDAUBolt() {
        return new JdbcInsertBolt(connectionProvider, new DAUJdbcMapper())
                .withInsertQuery("INSERT INTO `tb_dau` VALUES (NULL,?,?,?,NOW())")
                .withQueryTimeoutSecs(30);
    }
}
