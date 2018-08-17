package cn.itcast.dashboard.storm.topology;

import cn.itcast.dashboard.storm.bolt.dau.DAUBolt;
import cn.itcast.dashboard.storm.jdbc.JdbcBoltBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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

public class DAUTopology {

    public static void main(String[] args) {
        //第一步，定义TopologyBuilder对象，用于构建拓扑
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        KafkaSpoutConfig.Builder<String, String> kafkaSpoutBuilder = KafkaSpoutConfig.builder("hadoop1:9092", "topic-itcast-dashboard-generate-dau");
        kafkaSpoutBuilder.setGroupId("group-dau-1");
        kafkaSpoutBuilder.setOffsetCommitPeriodMs(10000);

        //第二步，设置spout和bolt
        topologyBuilder.setSpout("KafkaSpout", new KafkaSpout<>(kafkaSpoutBuilder.build()),3);

        topologyBuilder.setBolt("DAUBolt", new DAUBolt()).localOrShuffleGrouping("KafkaSpout");
        topologyBuilder.setBolt("DAUJdbcBolt", JdbcBoltBuilder.buildDAUBolt()).localOrShuffleGrouping("DAUBolt");

        //第三步，构建Topology对象
        StormTopology topology = topologyBuilder.createTopology();
        Config config = new Config();


        if (args == null || args.length == 0) {
            // 本地模式

            //第四步，提交拓扑到集群，这里先提交到本地的模拟环境中进行测试
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("DAUTopology", config, topology);
        } else {
            // 集群模式

            config.setNumWorkers(2); // 设置工作进程数

            config.setNumAckers(2);
            try {
                //提交到集群，并且将参数作为拓扑的名称
                StormSubmitter.submitTopology(args[0], config, topology);
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