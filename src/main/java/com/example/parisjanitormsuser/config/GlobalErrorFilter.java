package com.example.parisjanitormsuser.config;

import com.example.parisjanitormsuser.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;


/**
 * Global filter to catch  unhandled errors
 * and return standardized JSON response
 */
@Component
public class GlobalErrorFilter implements Filter {

    private final ObjectMapper objectMapper ;

    public GlobalErrorFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try{
            chain.doFilter(request, response); // Intercept request
        }catch(Exception ex){

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message(ex.getMessage())
                    .path(httpServletRequest.getRequestURI())
                    .build();

            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            httpServletResponse.getWriter().write(jsonResponse);
            httpServletResponse.getWriter().flush();

        }
    }

}
