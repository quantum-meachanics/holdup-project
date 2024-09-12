package com.quantum.holdup.filter;

import com.quantum.holdup.common.AuthConstants;
import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.domain.entity.Role;
import com.quantum.holdup.service.CustomUserDetails;
import com.quantum.holdup.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * JWT를 사용해 게시글 작성 권한을 처리하는 필터
 *
 * HTTP 요청이 들어올 때 JWT 토큰을 확인하고 유효한 토큰의 경우
 * 사용자 정보를 인증 컨텍스트에 등록하여 해당 요청이 인증된 사용자로서 처리되도록 한다.
 */
public class JwtPostAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtPostAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 게시글 조회 URL
        List<String> postReadUrlList = Arrays.asList(
                "/report/(.*)",
                "/spaces/(.*)"
                );

        if (postReadUrlList.stream().anyMatch(uri -> Pattern.matches(uri, request.getRequestURI()))) {
            String header = request.getHeader(AuthConstants.AUTH_HEADER);
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    if (TokenUtils.isValidToken(token)) {
                        Claims claims = TokenUtils.getClaimsFromToken(token);
                        Member member = Member.builder()
                                .password(claims.get("memberName").toString())
                                .email(claims.get("memberEmail").toString())
                                .role(Role.valueOf(claims.get("memberRole").toString()))
                                .build();
                        CustomUserDetails userDetails = new CustomUserDetails();
                        userDetails.setMember(member);
                        AbstractAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        // 권한 확인 (예: 사용자만 조회 가능한 경우)
                        if (member.getRole() != Role.USER) {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("권한이 없습니다.");
                            return;
                        }
                    } else {
                        throw new RuntimeException("토큰이 유효하지 않습니다.");
                    }
                } catch (Exception e) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    PrintWriter printWriter = response.getWriter();
                    JSONObject jsonObject = jsonresponseWrapper(e);
                    printWriter.print(jsonObject);
                    printWriter.flush();
                    printWriter.close();
                    return;
                }
            }
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 토큰 관련된 Exception 발생 시 예외 응답
     */
    private JSONObject jsonresponseWrapper(Exception e) {
        String resultMsg = "";

        if (e instanceof ExpiredJwtException) {
            // 토큰 만료 exception
            resultMsg = "Token Expired";

        } else if (e instanceof SignatureException) {
            // 토큰 서명 exception
            resultMsg = "TOKEN SignatureException Login";
        }

        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            System.out.println(e.getMessage());
            resultMsg = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        return jsonObject;
    }
}
