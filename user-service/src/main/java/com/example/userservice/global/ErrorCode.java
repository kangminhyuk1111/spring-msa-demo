package com.example.userservice.global;

public enum ErrorCode {

  NAME_REQUIRED("이름은 필수 입니다."),
  EMAIL_REQUIRED("이메일은 필수 입니다."),
  EMAIL_VALIDATION_FAILED("올바른 이메일 형식이 아닙니다."),
  MEMBER_NAME_TOO_SHORT("이름이 너무 짧습니다."),

  EMAIL_ALREADY_EXIST("이미 사용중인 이메일입니다."),

  MEMBER_NOT_FOUND("찾을 수 없는 멤버입니다."),

  LOGIN_FAIL("아이디 혹은 비밀번호가 일치하지 않습니다.");

  private final String message;

  ErrorCode(final String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
