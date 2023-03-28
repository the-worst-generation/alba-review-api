package com.alba.review.albareview.config.auth;

import com.alba.review.albareview.config.auth.dto.OAuthAttributes;
import com.alba.review.albareview.domain.user.User;
import com.alba.review.albareview.domain.user.UserRepository;
import com.alba.review.albareview.domain.user.dto.SignInRequestDTO;
import com.alba.review.albareview.domain.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public ResponseEntity<Long> signIn(SignInRequestDTO signInRequestDto){
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail((String) oAuth2User.getAttributes().get("email"));
        if(user.isPresent()){
            user.get().signInCustom(signInRequestDto.getSex(),
                    signInRequestDto.getAddress(),
                    signInRequestDto.getPhoneNumber(),
                    signInRequestDto.getAge());
            return ResponseEntity.ok().body(userRepository.save(user.get()).getId());
        }
        return ResponseEntity.badRequest().build();
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.signInAuth(
                        attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }


    public ResponseEntity<UserResponseDTO> getUser(long id) {
        if(!userRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok().body(UserResponseDTO.builder()
                .name(user.getName())
                .age(user.getAge())
                .sex(user.getSex())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRoleKey())
                .picture(user.getPicture())
                .address(user.getAddress())
                .build());
    }
}
