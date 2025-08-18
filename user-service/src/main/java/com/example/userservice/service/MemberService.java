package com.example.userservice.service;

import com.example.userservice.client.PointServiceClient;
import com.example.userservice.domain.Member;
import com.example.userservice.dto.request.CreateMemberRequest;
import com.example.userservice.dto.response.MemberResponse;
import com.example.userservice.global.ErrorCode;
import com.example.userservice.global.MemberException;
import com.example.userservice.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PointServiceClient pointServiceClient;

  public MemberService(final MemberRepository memberRepository,
      final PointServiceClient pointServiceClient) {
    this.memberRepository = memberRepository;
    this.pointServiceClient = pointServiceClient;
  }

  @Transactional
  public MemberResponse createMember(final CreateMemberRequest request) {
    // 1. 이메일 중복 체크 + 비관적 락 적용
    Optional<Member> existingMember = memberRepository.findByEmailWithLock(request.email());

    if (existingMember.isPresent()) {
      throw new MemberException(ErrorCode.EMAIL_ALREADY_EXIST.getMessage());
    }

    // 2. 멤버 생성
    final Member saved = memberRepository.save(request.toDomain());

    // 3. 생성 멤버 Response 객체로 변환
    return MemberResponse.of(saved);
  }

  public MemberResponse findById(final Long id) {
    final Member member = memberRepository.findById(id)
        .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    return MemberResponse.of(member);
  }

  public List<MemberResponse> findAll() {
    return memberRepository.findAll().stream().map(MemberResponse::of).toList();
  }
}