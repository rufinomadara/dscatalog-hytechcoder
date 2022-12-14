package com.hytechcoder.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hytechcoder.dscatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
