package com.stu.vo;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public static Result success(Map<String, Object> data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

//    public static Result success(Object message) {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMessage(message);
//        return result;
//    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public Result put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}