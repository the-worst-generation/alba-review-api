package com.alba.review.albareview.config.auth;

import com.alba.review.albareview.constants.Constants;
import com.alba.review.albareview.domain.user.*;
import com.alba.review.albareview.domain.user.dto.SignInRequestDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
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
            System.out.println("responseCode : " + responseCode);
            System.out.println(sb);


            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = getJsonElement(conn);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            bw.close();
            createKakaoUser(access_Token);

        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }
    public void createKakaoUser(String token) { // -->  토큰으로 회원가입 진행

        String reqURL = "https://kapi.kakao.com/v2/user/me";

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

            if(userRepository.existsByEmail(email)){
                System.out.println("이미 있는 유저임.");
                //처리 해야함
            }
            else{
                User user = User.builder()
                        .email(email)
                        .profilePicture(profilePicture)
                        .socialType(SocialType.KAKAO)
                        .build();
                userRepository.save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement getJsonElement(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);
        br.close();
        return JsonParser.parseString(result);
    }

    public ResponseEntity<Long> signIn(SignInRequestDTO signInRequestDTO) {
        //닉네임, 이메일 중복있는지 확인한 후에 현재 로그인 중인 User 찾아서 정보 기입
        return ResponseEntity.ok().body(0L);
    }
}
