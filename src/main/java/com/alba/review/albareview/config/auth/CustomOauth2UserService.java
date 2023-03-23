package com.alba.review.albareview.config.auth;

import com.alba.review.albareview.config.auth.dto.OAuthAttributes;
import com.alba.review.albareview.config.auth.dto.SessionUser;
import com.alba.review.albareview.domain.user.User;
import com.alba.review.albareview.domain.user.UserRepository;
import com.alba.review.albareview.domain.user.dto.SignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
//구글 로그인을 통해 가져온 사용자의 정보를 기반으로 가입 및 정보 수정
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registerId = userRequest.getClientRegistration().getRegistrationId(); //현재 로그인 중인 서비스를 구분하는 코드
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName(); //Oauth2 진행시에 키가 되는 필드값

        OAuthAttributes attributes = OAuthAttributes.of(registerId, userNameAttributeName, oAuth2User.getAttributes()); //Oauth2User의 attribute를 담음
        User user = saveOrUpdate(attributes);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }
    public String signIn(SignInRequestDto signInRequestDto){
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail((String) oAuth2User.getAttributes().get("email"));
        if(user.isPresent()){
            user.get().signIn(signInRequestDto.getSex(),
                    signInRequestDto.getAddress(),
                    signInRequestDto.getPhoneNumber(),
                    signInRequestDto.getAge());
            userRepository.save(user.get());
        }
        return "";
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(
                        attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }


}
