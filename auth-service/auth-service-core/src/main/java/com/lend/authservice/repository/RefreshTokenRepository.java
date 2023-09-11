package com.lend.authservice.repository;

import com.lend.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Query("SELECT refresh FROM RefreshToken refresh WHERE refresh.user.id =:userId")
    Optional<RefreshToken> getByUserId(@Param("userId") String userId);

    @Query("SELECT refresh FROM RefreshToken refresh WHERE refresh.token =:token AND refresh.user.id =:userId")
    Optional<RefreshToken> getByUserIdAndToken(@Param("userId") String userId,
                                               @Param("token") String token);
}
