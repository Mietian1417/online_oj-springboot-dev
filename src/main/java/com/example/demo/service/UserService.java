package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import com.example.demo.redis.ProblemsKey;
import com.example.demo.redis.RedisCacheTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:36
 */

@Service
@Validated
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    public int saveUserSubmitCode(int userId, int problemId, String submitCode) {
        return userMapper.saveUserSubmitCode(userId, problemId, submitCode);
    }

    public String getLastSubmitCode(Integer problemId, int userId) {
        return userMapper.getLastSubmitCode(problemId, userId);
    }

    public Integer isPass(int userId, int problemId) {
        return userMapper.isPass(userId, problemId);
    }

    public Integer setPass(Integer id, Integer problemId) {
        return userMapper.setPass(id, problemId);
    }

    /**
     * 返回 0 表示 没有全部通过, 1 表示全部通过, -1 表示 sql 执行出错
     * @param stdout
     * @param userId
     * @param problemId
     * @return
     */
    public int updateUserOfProblemIsPass(String stdout, int userId, Integer problemId) {
        // 去除空格, 统计通过个数
        String isOk = stdout.replaceAll("\\s*", "");
        int passTestCount = 0;
        for (int i = 0; i < isOk.length(); i++) {
            if (isOk.charAt(i) == 'o') {
                passTestCount++;
            }
        }
        if (passTestCount == 10) {
            // 存数据库
            Integer integer = setPass(userId, problemId);
            if (integer == null || integer == 0) {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
}
