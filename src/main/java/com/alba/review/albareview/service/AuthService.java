package com.alba.review.albareview.service;

import com.alba.review.albareview.config.auth.DTO.KakaoUserInfoDto;
import com.alba.review.albareview.config.auth.DTO.TokenDto;
import com.alba.review.albareview.constants.Constants;
import com.alba.review.albareview.domain.user.*;
import com.alba.review.albareview.domain.user.DTO.SignInRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final Constants constants;
    private final SecurityService securityService;

    public String getKakaoToken(String code){ //인가 코드 받아서 token 받아옴
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .build();

        final JsonObject result = new Gson().fromJson(webClient.post()
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", constants.getClientId())
                        .with("redirect_uri", constants.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class)
                .block(), JsonObject.class);
        String access_Token = result.getAsJsonObject().get("access_token").getAsString();

        return access_Token;
    }
    public KakaoUserInfoDto getKakaoUserInfo(String token) { // -->  토큰으로 회원 정보 가져오기
        final WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        final JsonElement element = new Gson().fromJson(webClient.post()
                .retrieve()
                .bodyToMono(String.class)
                .block(), JsonObject.class);

        String email = String.valueOf(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email"));
        String profilePicture = String.valueOf(element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image"));

        KakaoUserInfoDto userInfoDto = new KakaoUserInfoDto(email, profilePicture);

        return userInfoDto;
    }

    //유저가 있다면 jwt를 발급해주자
    //인가코드를 기반으로 DB에 저장 -> jwt 헤더에 리턴
    public ResponseEntity<String> getJwtValue(String authorizationCode) {
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
            System.out.println("access: " + tokenDto.getAccessToken());
            return ResponseEntity.ok().headers(headers).build();
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

    public boolean checkNickNameDuplicate(String username){
        return userRepository.existsByNickname(username);
    }

    @Transactional
    public ResponseEntity<Long> signIn(String requestEmail, SignInRequestDTO signInRequestDTO) {
        if(!userRepository.findByEmailAndSocialType(requestEmail, SocialType.KAKAO).isPresent()){
            return ResponseEntity.badRequest().body(-1L);
        }
        User user = userRepository.findByEmailAndSocialType(requestEmail, SocialType.KAKAO).get();
        if(checkNickNameDuplicate(signInRequestDTO.getNickname())){
            return ResponseEntity.badRequest().body(-1L);
        }
        user.toEntityCustomData(signInRequestDTO.getBirthDate(), signInRequestDTO.getNickname(), signInRequestDTO.getSex());

        return ResponseEntity.ok().body(user.getId());
    }
}
