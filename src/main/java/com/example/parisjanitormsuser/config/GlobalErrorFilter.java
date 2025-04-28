package com.example.parisjanitormsuser.config;


import com.example.parisjanitormsuser.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class GlobalErrorFilter implements Filter {

    private final ObjectMapper objectMapper ;

    public GlobalErrorFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try{
            chain.doFilter(request, response);
        }catch(Exception ex){
            // Handle global error
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // Response of error standardized
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    ex.getMessage(),
                    request.getServletContext().getContextPath()
            );
            String jsonResponse = objectMapper.writeValueAsString(apiError);
            httpServletResponse.getWriter().write(jsonResponse);

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
