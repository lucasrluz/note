package com.api.note.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.note.dtos.auth.AuthenticateDTORequest;
import com.api.note.services.auth.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Interceptor implements HandlerInterceptor {
    private AuthService authService;

    public Interceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final AllowAnnonymous allowAnnonymous = ((HandlerMethod)handler)
            .getMethod()
            .getAnnotation((AllowAnnonymous.class));

        if (allowAnnonymous != null) {
            return true;
        }

        if (request.getHeader("JWT") == null) {
            response.setStatus(400);
            response.getWriter().write("JWT is required");
            response.getWriter().flush();
            
            return false;
        }

        if (request.getHeader("UserId") == null) {
            response.setStatus(400);
            response.getWriter().write("UserId is required");
            response.getWriter().flush();
            
            return false;
        }

        try {
            String jwt = request.getHeader("JWT");
            String userId = request.getHeader("UserId");
        
            AuthenticateDTORequest authenticateDTORequest = new AuthenticateDTORequest(jwt, userId);

            this.authService.authenticate(authenticateDTORequest);

            return true;
        } catch (Exception exception) {
            response.setStatus(400);
            response.setContentType("text/xml; charset=UTF-8");
            response.getWriter().write(exception.getMessage());
            response.getWriter().flush();

            return false;
        }
    }
}
