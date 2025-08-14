package com.example.userservice.controller;

import com.example.userservice.dto.request.CreateMemberRequest;
import com.example.userservice.dto.response.MemberResponse;
import com.example.userservice.service.MemberService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(final MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping
  public List<MemberResponse> findAllMembers() {
    return memberService.findAll();
  }

  @GetMapping("/{id}")
  public MemberResponse findMemberById(@PathVariable Long id) {
    return memberService.findById(id);
  }

  @PostMapping
  public MemberResponse createMember(@RequestBody CreateMemberRequest request) {
    return memberService.createMember(request);
  }
}
