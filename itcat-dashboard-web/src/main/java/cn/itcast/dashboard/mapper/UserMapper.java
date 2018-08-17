package cn.itcast.dashboard.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    /**
     * 根据时间范围查询注册用户的城市分布情况
     *
     * @param start
     * @param end
     * @return
     */
    List<Map<String, Object>> queryUserCityGroupByCity(@Param("start") String start, @Param("end") String end);
}
