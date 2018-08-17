package cn.itcast.dashboard.generate.service;

import cn.itcast.dashboard.generate.bean.Order;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

//生成订单数据
@Component
public class OrderGenerateImpl implements IGenerate {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void start() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        generateOrder();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void generateOrder() throws Exception {
        //生成数据，将数据发送给kafka
        Order order = new Order();
        int maxUserId = Integer.valueOf(stringRedisTemplate.opsForValue().get("itcast-dashboard-generate-user-max-id").toString());
//        Long userId = RandomUtils.nextLong(1, maxUserId);
        order.setUserId(Long.valueOf(RandomUtils.nextInt(1,maxUserId)));
        order.setPayment(RandomUtils.nextDouble(100,9999));
        order.setUpdateTime(new Date());
        order.setCreateTime(order.getUpdateTime());
        order.setStatus(RandomUtils.nextInt(1,7));
        if (order.getStatus() != 1){
            order.setPaymentTime(new Date());
        }
        order.setPostFee("0");
        order.setPaymentType(1);
        order.setOrderId(StringUtils.replace(UUID.randomUUID().toString(),"-",""));
        if (order.getStatus() >= 4){
            order.setConsignTime(new Date());
            order.setShippingCode("3243243");
            order.setShippingName("顺丰");
        }
        if (order.getStatus() >= 5){
            order.setEndTime(new Date());
        }

        //发送
        this.kafkaTemplate.sendDefault(order);

        try {
            Thread.sleep(RandomUtils.nextLong(10,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}