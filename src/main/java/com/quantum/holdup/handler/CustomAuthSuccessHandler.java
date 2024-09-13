package com.quantum.holdup.handler;

import com.quantum.holdup.domain.entity.Member;
import com.quantum.holdup.service.CustomUserDetails;
import com.quantum.holdup.util.ConvertUtil;
import com.quantum.holdup.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/*
 * Login 성공했을때 동작하는 Handler
 *
 * JWT를 생성하고, responseHeader(응답 헤더) 넘겨주는 역할
 * */
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        // Security Context에 저장된 로그인한 사용자 정보를 꺼내온다.
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // member 객체를 json형태의 객체로 변환
        JSONObject jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(member);

        // 응답 하기위한 Map
        HashMap<String, Object> responseMap = new HashMap<>();

        // 토큰 생성
        String token = TokenUtils.generateJwtToken(member);

        // 응답하기 위해 담아주는 역할
        responseMap.put("token", token);
        responseMap.put("userInfo", jsonValue);
        responseMap.put("message", "로그인 성공");

        // 응답 인코딩 설정
        response.setCharacterEncoding("UTF-8");

        // 응답 MIME 설정
        response.setContentType("application/json");

        // JSON 응답 생성
        JSONObject jsonObject = new JSONObject(responseMap);

        // 만든 response를 내보내기
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
