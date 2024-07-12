package com.n1nt3nd0.social_media_app.repository;

import com.n1nt3nd0.social_media_app.entity.UserSocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserSocialMedia, Long> {
}
