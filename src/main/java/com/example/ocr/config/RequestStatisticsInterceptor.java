package com.example.ocr.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestStatisticsInterceptor implements AsyncHandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestStatisticsInterceptor.class);
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private HibernateStatisticsInterceptor statisticsInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("Received-Time", System.currentTimeMillis());
        statisticsInterceptor.startCounter();

        logger.info("[REQUEST] {} {} | ReqID: {} | ReqUser: {} | ReqTime: {} | ClientID: {}", request.getMethod(), request.getRequestURI(),
                request.getHeader("TTSSession-Id"), request.getRemoteUser(), request.getHeader("TTSSession-Time"), request.getHeader("Corporate-Id"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            long duration = System.currentTimeMillis() - (Long) request.getAttribute("Received-Time");
            Long queryCount = statisticsInterceptor.getQueryCount();
            statisticsInterceptor.clearCounter();

            response.setHeader("TTSSession-Id", request.getHeader("TTSSession-Id"));
            response.setHeader("TTSSession-Time", request.getHeader("TTSSession-Time"));
            response.setHeader("Corporate-Id", request.getHeader("Corporate-Id"));

            logger.info("[RESPONSE] {} {} {} | ReqID: {} | Time: {} ms | Queries: {}", response.getStatus(), request.getMethod(), request.getRequestURI(), request.getHeader("TTSSession-Id"), duration, queryCount);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //concurrent handling cannot be supported here
        statisticsInterceptor.clearCounter();
    }
}
