package com.example.userservice.repository;

import com.example.userservice.domain.Member;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {

  private final Map<Long, Member> store = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(0);

  @Override
  public Optional<Member> findById(final Long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Member> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public Member save(final Member member) {
    Member memberToSave = member;
    if (member.getId() == null) {
      // ID를 새로 생성한 Member 인스턴스로 부여
      memberToSave = new Member(
          idGenerator.incrementAndGet(),
          member.getName(),
          member.getEmail(),
          member.getCreatedAt(),
          member.getUpdatedAt()
      );
    }
    store.put(memberToSave.getId(), memberToSave);
    return memberToSave;
  }

  @Override
  public void update(final Member member) {
    if (!store.containsKey(member.getId())) {
      throw new NoSuchElementException("Member not found with id: " + member.getId());
    }
    store.put(member.getId(), member);
  }

  @Override
  public void deleteById(final Long id) {
    store.remove(id);
  }

  @Override
  public boolean existsByEmail(final String email) {
    return store.values().stream()
        .anyMatch(member -> member.getEmail().equals(email));
  }
}
