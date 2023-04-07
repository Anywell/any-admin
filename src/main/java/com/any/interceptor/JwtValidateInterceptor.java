package com.any.interceptor;

import com.alibaba.fastjson2.JSON;
import com.any.common.vo.JwtUtil;
import com.any.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtValidateInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "需要验证" + token);

        if (token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug("验证通过。");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug("验证失败，禁止访问。");

        Result<Object> fail = Result.fail(20003, "Jwt验证失败,请重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(fail));

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
