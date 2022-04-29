package com.minbae.owner.repository;

import com.minbae.owner.entity.Owner;
import com.minbae.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
    @Query(value = "SELECT * FROM owner where owner_email = :owner_email and owner_pwd = :owner_pwd", nativeQuery = true)
    Owner findByEmailAndPwd(String owner_email, String owner_pwd);

    Optional<Owner> findByOwnerEmailAndOwnerPwd(String email, String pwd);
}
