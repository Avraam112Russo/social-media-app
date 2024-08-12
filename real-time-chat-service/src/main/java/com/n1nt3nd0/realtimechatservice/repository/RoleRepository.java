package com.n1nt3nd0.realtimechatservice.repository;



import com.n1nt3nd0.realtimechatservice.entity.RoleEntity;
import com.n1nt3nd0.realtimechatservice.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
  Optional<RoleEntity> findByName(ERole name);
}
