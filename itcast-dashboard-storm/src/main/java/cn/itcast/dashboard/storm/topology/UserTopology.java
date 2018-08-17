package cn.itcast.dashboard.storm.topology;

import cn.itcast.dashboard.storm.bolt.order.OrderBolt;
import cn.itcast.dashboard.storm.bolt.user.UserRegisterBolt;
import cn.itcast.dashboard.storm.jdbc.JdbcBoltBuilder;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class UserTopology {
    public static void main(String[] args) {
        //拓扑构建器
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        //设置kafkaSpout
        KafkaSpoutConfig.Builder<String,String> kafkaBuilder =
                KafkaSpoutConfig.builder("hadoop1:9092","topic-itcast-dashboard-generate-user-register");
        //设置消费组名称
        kafkaBuilder.setGroupId("group-user-1");
        kafkaBuilder.setOffsetCommitPeriodMs(1000);//offset提交时间

        topologyBuilder.setSpout("KafkaSpout",new KafkaSpout<>(kafkaBuilder.build()),3);//并行度为3，因为partition为3

        //设置bolt
        topologyBuilder.setBolt("UserRegisterBolt",new UserRegisterBolt()).localOrShuffleGrouping("KafkaSpout");
        topologyBuilder.setBolt("UserJdbcBolt",JdbcBoltBuilder.buildUserBolt()).localOrShuffleGrouping("UserRegisterBolt");


        StormTopology topology = topologyBuilder.createTopology();
        Config config = new Config();

        if (args == null || args.length == 0){
            //本地模式
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("UserTopology",config,topology);
        }else {
            //集群模式
            config.setNumWorkers(2);
            config.setNumAckers(2);
            try {
                StormSubmitter.submitTopology(args[0],config,topology);
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }


    }
}
