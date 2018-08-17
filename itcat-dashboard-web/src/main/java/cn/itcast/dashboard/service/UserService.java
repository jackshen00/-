package cn.itcast.dashboard.service;

import cn.itcast.dashboard.mapper.UserMapper;
import cn.itcast.dashboard.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<Map<String, Object>> queryUserCityGroupByCity() {
        Date date = new Date();
        String start = DateUtils.getDateBegin(DateUtils.formatDate(DateUtils.dateAddDays(date, -6)));
        String end = DateUtils.getDateEnd(DateUtils.formatDate(date));

        List<Map<String, Object>> list = this.userMapper.queryUserCityGroupByCity(start, end);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            Map<String, Object> h = new HashMap<>();
            h.put("name", StringUtils.replace((String) map.get("city"), "市", ""));
            h.put("value", map.get("num"));
            result.add(h);
        }
        return result;
    }
}
