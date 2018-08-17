package cn.itcast.dashboard.storm.bolt.order;


import cn.itcast.dashboard.storm.bean.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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

public class OrderBolt extends BaseBasicBolt {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String orderJson = input.getStringByField("value");
        System.out.println(orderJson);
        if (StringUtils.isEmpty(orderJson)){
            return;
        }

        try {
            Order order = MAPPER.readValue(orderJson,Order.class);

            collector.emit(new Values(order));
        } catch (Exception e) {
            LOGGER.error("Order反序列化时出错，orderJson = " + orderJson, e);
            throw new FailedException();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("order"));
    }
}