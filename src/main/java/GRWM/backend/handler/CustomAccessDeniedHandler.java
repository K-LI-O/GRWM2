package GRWM.backend.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("AccessDeniedException occurred! Message: {}", accessDeniedException.getMessage());
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.setContentType("application/json;charset=UTF-8"); // JSON 형식으로 응답할 경우
            response.getWriter().write("{\"message\": \"Access Denied: You do not have permission to access this resource.\"}");
            response.getWriter().flush();
            response.getWriter().close(); // 스트림을 닫아 응답을 강제로 커밋
        } else {
            // log.warn("Response already committed, cannot set status to 403.");
        }
    }
}
