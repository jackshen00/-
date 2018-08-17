package cn.itcast.dashboard.generate.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RedisJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //从数据库中读取最大用户ID的值
        ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap()
                .get("applicationContext");
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT MAX(id) FROM tb_user");
            int maxId = 0;
            while (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }

            if (maxId == 0) {
                return;
            }

            System.out.println("用户最大的ID为："+ maxId);

            //将值写入redis中
            StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);

            stringRedisTemplate.opsForValue().set("itcast-dashboard-generate-user-max-id", String.valueOf(maxId));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}