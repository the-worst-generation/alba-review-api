package com.alba.review.albareview.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndSocialType(String email, SocialType socialType);
    Boolean existsByEmailAndSocialType(String email, SocialType socialType);
    Boolean existsByNickname(String nickname);
}
