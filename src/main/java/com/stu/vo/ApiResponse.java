package com.stu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用API响应包装")
public class ApiResponse<T> {
    @Schema(description = "业务状态码：200成功，其他为失败")
    private int code;
    @Schema(description = "提示信息")
    private String message;
    @Schema(description = "数据载体")
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.setCode(200);
        r.setMessage("成功");
        r.setData(data);
        return r;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.setCode(500);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}

