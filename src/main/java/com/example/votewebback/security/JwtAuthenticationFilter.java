package com.example.votewebback.security;

import com.example.votewebback.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component //꼭 등록해야함. 없으면 bean 등록 안됨.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //한번만 동작하게
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            String user_id = null; //username 즉 회원의 id임
            //토큰이 올바르지 않으면 pass 하기
            if(authHeader==null || !authHeader.startsWith("Bearer ")){ // 토큰이 해당 문자로 시작됨.
                filterChain.doFilter(request,response);
                return;
            }
            jwt = authHeader.substring(7); // bearer 이후의 문자열
            user_id = jwtService.extractUsername(jwt);

            if(user_id != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(user_id);
                try {
                    if(jwtService.isTokenValid(jwt)){
                        Authentication authToken = jwtService.getAuthentication(jwt); // 권한 정보를 포함한 인증 객체 생성
                        if (authToken != null && authToken.getAuthorities() != null && !authToken.getAuthorities().isEmpty()) {
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                } catch (CustomException e) {
                    handleException(e, response);
                }
            }
            filterChain.doFilter(request,response);
        } catch (CustomException e) {
            handleException(e, response);
        }
    }
    private void handleException(CustomException ex, HttpServletResponse response) throws IOException {
        // 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
        // 예외 메시지를 응답에 작성
        response.setContentType("application/json; charset=UTF-8"); // 내용 유형을 JSON으로 설정
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getErrorCode());
        errorResponse.put("message", ex.getMessage());
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}
