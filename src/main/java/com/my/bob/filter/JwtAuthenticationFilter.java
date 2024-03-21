package com.my.bob.filter;

import com.my.bob.constants.AuthConstant;
import com.my.bob.service.BobUserService;
import com.my.bob.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter
    // 동일한 request 안에서 한번만 실행되는 필터로, 현재 프로젝트에선 jwt token 인증에 사용된다
    // UsernamePasswordAuthenticationFilter 전에  authentication 객체를 셋팅해 둔다
    // 해당 filter의 경우 api/a가 끝난후 api/b로 리다이렉트 될 경우 Filter가 두번 호출되거나 하는 것을 막을 수 있다

    private final JwtTokenProvider jwtTokenProvider;
    private final BobUserService bobUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AuthConstant.AUTH_HEADER);
        String token = parsingToken(authHeader);
        if(! StringUtils.isEmpty(token)) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String userEmail = userDetails.getUsername();

                if(bobUserService.existByEmail(userEmail)) {
                    // 로그인 (다음 filter 에서 인증을 위한)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                // do nothing
            }
        }

        filterChain.doFilter(request, response);
    }

    /* private method */
    private String parsingToken(String authHeader){
        if(StringUtils.isEmpty(authHeader) || ! authHeader.startsWith(AuthConstant.TOKEN_TYPE)) return Strings.EMPTY;
        // String form is 'Bearer jwtTokenValue'
        String[] tokenSplit = authHeader.split(" ");
        return tokenSplit[1];
    }
}
