package com.stu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatUtil {

    private static final Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Value("${wechat.auth-url}")
    private String authUrl;

    // 添加测试模式配置
    @Value("${wechat.test.mode:false}")
    private boolean testMode;

    // 测试用户映射
    private Map<String, Map<String, String>> testUsers = new HashMap<>();

    // 初始化测试用户
    @PostConstruct
    public void initTestUsers() {
        // 测试用户1 (保留原有)
        Map<String, String> user1 = new HashMap<>();
        user1.put("openid", "test_openid_001");
        user1.put("session_key", "test_session_key_001");
        user1.put("unionid", "test_unionid_001");
        user1.put("errcode", "0");
        user1.put("errmsg", "");
        testUsers.put("test_code_123", user1);

        // 测试用户2 (保留原有)
        Map<String, String> user2 = new HashMap<>();
        user2.put("openid", "test_openid_002");
        user2.put("session_key", "test_session_key_002");
        user2.put("unionid", "test_unionid_002");
        user2.put("errcode", "0");
        user2.put("errmsg", "");
        testUsers.put("test_code_456", user2);

        // 新增：个人测试代码
        Map<String, String> personal = new HashMap<>();
        personal.put("openid", "test_person_openid");
        personal.put("session_key", "test_person_session");
        personal.put("unionid", "test_person_union");
        personal.put("errcode", "0");
        personal.put("errmsg", "");
        testUsers.put("test_code_person", personal);

        // 新增：高校测试代码
        Map<String, String> campus = new HashMap<>();
        campus.put("openid", "test_campus_openid");
        campus.put("session_key", "test_campus_session");
        campus.put("unionid", "test_campus_union");
        campus.put("errcode", "0");
        campus.put("errmsg", "");
        testUsers.put("test_code_campus", campus);

        logger.info("初始化测试用户完成，测试模式: {}，可用测试code: {}", testMode, testUsers.keySet());
    }

    public Map<String, String> getOpenidByCode(String code) {
        // 测试模式：任何以 test_code 开头的 code 都走本地模拟，或在表中已有映射
        if (testMode) {
            Map<String, String> found = testUsers.get(code);
            if (found == null && code != null && code.startsWith("test_code")) {
                // 动态生成一个模拟用户，避免需要频繁改代码
                Map<String, String> dynamic = new HashMap<>();
                dynamic.put("openid", "mock_" + code);
                dynamic.put("session_key", "mock_session_" + System.currentTimeMillis());
                dynamic.put("unionid", "mock_union_" + code);
                dynamic.put("errcode", "0");
                dynamic.put("errmsg", "");
                testUsers.put(code, dynamic);
                logger.info("测试模式动态生成用户, code={}, openid={}", code, dynamic.get("openid"));
                return dynamic;
            }
            if (found != null) {
                logger.info("使用测试模式映射用户, code={}, openid={}", code, found.get("openid"));
                return found;
            }
        }

        // 非测试模式或不匹配测试规则时，走真实微信接口
        String url = authUrl.replace("{0}", appId).replace("{1}", appSecret).replace("{2}", code);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonObject = JSON.parseObject(response.getBody());

            Map<String, String> result = new HashMap<>();
            result.put("openid", jsonObject.getString("openid"));
            result.put("session_key", jsonObject.getString("session_key"));
            result.put("unionid", jsonObject.getString("unionid"));
            result.put("errcode", jsonObject.getString("errcode"));
            result.put("errmsg", jsonObject.getString("errmsg"));

            logger.info("微信登录响应: {}", jsonObject.toJSONString());

            return result;
        } catch (Exception e) {
            logger.error("调用微信API失败: {}", e.getMessage(), e);
            throw new RuntimeException("微信登录服务暂时不可用");
        }
    }

    // 添加getter和setter方法，便于测试
    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}