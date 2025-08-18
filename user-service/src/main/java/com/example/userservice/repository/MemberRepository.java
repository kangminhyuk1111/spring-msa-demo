package com.example.userservice.repository;

import com.example.userservice.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
  Optional<Member> findByEmailWithLock(String email);
  Optional<Member> findById(Long id);
  List<Member> findAll();
  Member save(Member member);
  boolean existsByEmail(String email);
}
