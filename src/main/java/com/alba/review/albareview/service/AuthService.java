package com.alba.review.albareview.service;

import com.alba.review.albareview.domain.user.DTO.KakaoUserInfoDto;
import com.alba.review.albareview.config.auth.DTO.TokenDto;
import com.alba.review.albareview.constants.Constants;
import com.alba.review.albareview.domain.user.*;
import com.alba.review.albareview.domain.user.DTO.SignInRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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

    //custom signIn 하는 부분
    @Transactional
    public ResponseEntity<String> customSignIn(String requestEmail, SignInRequestDTO signInRequestDTO) {
        if(!userRepository.findByEmailAndSocialType(requestEmail, signInRequestDTO.getSocialType()).isPresent()){
            return ResponseEntity.badRequest().body(requestEmail+"의 email은 회원가입이 되지 않았습니다.");
        }
        if(checkNickNameDuplicate(signInRequestDTO.getNickname())){
            return ResponseEntity.badRequest().body(signInRequestDTO.getNickname()+ "의 닉네임은 이미 존재합니다.");
        }
        UserEntity user = userRepository.findByEmailAndSocialType(requestEmail, SocialType.KAKAO).get();
        user.toEntityCustomData(signInRequestDTO.getBirthDate(), signInRequestDTO.getNickname(), signInRequestDTO.getSex());

        return ResponseEntity.ok().body(String.valueOf(user.getId()));
    }

    public ResponseEntity<String> kakaoSignIn(String code) { //email 우리 DB에 없으면 저장 하고 있으면 nickname check
        String kakaoAccessToken = getKakaoToken(code);
        KakaoUserInfoDto userInfoDto = getKakaoUserInfo(kakaoAccessToken);
        //유저가 없다면 저장
        if (!userRepository.existsByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO)) {
            UserEntity user = UserEntity.builder()
                    .email(userInfoDto.getEmail())
                    .profilePicture(userInfoDto.getProfilePicture())
                    .socialType(SocialType.KAKAO)
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
        if(userRepository.findByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO).get().getNickname() != null){
            return ResponseEntity.badRequest().body("이미 회원가입 되어 있는 user입니다.");
        }
        //토큰 발급하기
        try {
            TokenDto tokenDto = securityService.login(userInfoDto.getEmail());
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("token 발급시에 오류 발생. 서버 문의 바랍니다.");
        }
    }

    public ResponseEntity<String> kakaoLogIn(String code) {
        String kakaoAccessToken = getKakaoToken(code);
        KakaoUserInfoDto userInfoDto = getKakaoUserInfo(kakaoAccessToken);
        //유저가 없으면
        if (!userRepository.existsByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO)) {
            return ResponseEntity.badRequest().body("회원가입 되지 않은 user입니다.");
        }
        if(userRepository.findByEmailAndSocialType(userInfoDto.getEmail(), SocialType.KAKAO).get().getNickname() == null){
            return ResponseEntity.badRequest().body("custom 회원가입이 되어 있지않습니다.");
        }
        //토큰 발급하기
        try {
            TokenDto tokenDto = securityService.login(userInfoDto.getEmail());
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("token 발급시에 오류 발생. 서버 문의 바랍니다.");
        }
    }
}
