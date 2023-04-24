package com.alba.review.albareview.service;

import com.alba.review.albareview.config.auth.DTO.KakaoUserInfoDto;
import com.alba.review.albareview.config.auth.DTO.LoginResponseDto;
import com.alba.review.albareview.config.auth.DTO.TokenDto;
import com.alba.review.albareview.constants.Constants;
import com.alba.review.albareview.domain.user.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final Constants constants;
    private final SecurityService securityService;

    public String getKakaoToken(String code) throws IOException{ //인가 코드 받아서 token 받아옴
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+ constants.getClientId()); // REST_API_KEY 입력
            sb.append("&redirect_uri=" + constants.getRedirectUri()); // 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                return "";
            }
            System.out.println("responseCode : " + responseCode);
            System.out.println(sb);


            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = getJsonElement(conn);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }
    public KakaoUserInfoDto getKakaoUserInfo(String token) { // -->  토큰으로 회원 정보 가져오기

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        KakaoUserInfoDto userInfoDto = null;
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            JsonElement element = getJsonElement(conn);

            String email = String.valueOf(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email"));
            String profilePicture = String.valueOf(element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image"));

            userInfoDto = new KakaoUserInfoDto(email, profilePicture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(userInfoDto == null){
            return null;
        }
        return userInfoDto;
    }

    private JsonElement getJsonElement(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);
        br.close();
        return JsonParser.parseString(result);
    }


//    public LoginResponseDto kakaoSignin(String authorizationCode) throws IOException {
//        String kakaoAccessToken = getKakaoToken(authorizationCode);
//        KakaoUserInfoDto userInfoDto = getKakaoUserInfo(kakaoAccessToken);
//        if(!userRepository.existsByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO)){ //회원가입 X
//            User user = User.builder()
//                    .email(userInfoDto.getEmail())
//                    .profilePicture(userInfoDto.getProfilePicture())
//                    .socialType(SocialType.KAKAO)
//                    .role(Role.ROLE_USER)
//                    .build();
//            userRepository.save(user);
//        }
//
//        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
//                .email(userInfoDto.getEmail())
//                .profilePicture(userInfoDto.getProfilePicture())
//                .socialType(SocialType.KAKAO)
//                .build();
//
//        return loginResponseDto;
//    }

    //유저가 있다면 jwt를 발급해주자
    public ResponseEntity<String> kakaoLogin(String authorizationCode) throws IOException {
        String kakaoAccessToken = getKakaoToken(authorizationCode);
        KakaoUserInfoDto userInfoDto = getKakaoUserInfo(kakaoAccessToken);
        //유저가 없다면 저장
        if (!userRepository.existsByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO)) {
            User user = User.builder()
                    .email(userInfoDto.getEmail())
                    .profilePicture(userInfoDto.getProfilePicture())
                    .socialType(SocialType.KAKAO)
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
        //토큰 발급하기
        try {
            TokenDto tokenDto = securityService.login(userInfoDto.getEmail());
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).body(tokenDto.getAccessToken());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("오류");
        }

    }

    public HttpHeaders setTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정했음
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());
        headers.add("Authorization", tokenDto.getAccessToken());

        return headers;
    }
}
