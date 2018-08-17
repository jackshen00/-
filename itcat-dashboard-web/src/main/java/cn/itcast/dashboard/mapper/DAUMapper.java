package cn.itcast.dashboard.mapper;

import org.apache.ibatis.annotations.Param;

public interface DAUMapper {

    /**
     * 根据时间范围查询数据量
     *
     * @param start 范围开始
     * @param end 范围结束
     * @return
     */
    Integer queryCountByDate(@Param("start") String start, @Param("end") String end);

}