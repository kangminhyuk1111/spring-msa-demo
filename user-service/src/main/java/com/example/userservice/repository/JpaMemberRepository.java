package com.example.userservice.repository;

import com.example.userservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMemberRepository extends MemberRepository, JpaRepository<Member, Long> {

}
