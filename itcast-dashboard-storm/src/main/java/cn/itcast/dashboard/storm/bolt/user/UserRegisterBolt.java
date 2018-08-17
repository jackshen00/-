package cn.itcast.dashboard.storm.bolt.user;

import cn.itcast.dashboard.storm.bean.User;
import cn.itcast.dashboard.storm.bolt.order.OrderBolt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserRegisterBolt extends BaseBasicBolt {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisterBolt.class);

    public void execute(Tuple input, BasicOutputCollector collector) {
        String json = input.getStringByField("value");
        System.out.println(json);
        try {
            User user = MAPPER.readValue(json, User.class);
            // 业务逻辑处理
            collector.emit(new Values(user));
        } catch (Exception e) {
            LOGGER.error("User反序列化时出错，Json = " + json, e);
            throw new FailedException();
        }
    }


    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("user"));
    }
}
