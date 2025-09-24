
package com.stu.interceptor;

import com.stu.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String authHeader = request.getHeader(jwtUtil.getHeader());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"未提供有效的Token\"}");
            return false;
        }

        // 去掉Bearer前缀
        String token = authHeader.substring(7);

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"Token无效或已过期\"}");
            return false;
        }

        // 将用户ID存入request，方便后续使用
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }
}