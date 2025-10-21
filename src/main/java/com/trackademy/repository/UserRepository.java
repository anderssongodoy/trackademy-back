package com.trackademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trackademy.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByExternalId(String externalId);
    Optional<User> findByProviderAndExternalId(String provider, String externalId);
    Optional<User> findByProviderAndProviderSubject(String provider, String providerSubject);
}
