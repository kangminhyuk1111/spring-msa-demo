// src/lib/api/auth.ts
import { LoginRequest, RegisterRequest, LoginResponse, User } from '../types/auth';

// 프록시 사용시 상대 경로로 변경
const USER_SERVICE_URL = '';

export const authService = {
  // 로그인
  async login(data: LoginRequest): Promise<LoginResponse> {
    try {
      const response = await fetch(`${USER_SERVICE_URL}/api/members/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        let errorMessage = '로그인 실패';

        try {
          const errorData = await response.json();
          console.log('에러 데이터:', errorData);

          // Spring Boot에서 보낸 에러 메시지 사용
          if (errorData.message) {
            errorMessage = errorData.message;
          }
        } catch (e) {
          // JSON 파싱 실패시 상태코드로 판단
          if (response.status === 404) {
            errorMessage = '존재하지 않는 이메일입니다.';
          } else if (response.status === 401) {
            errorMessage = '비밀번호가 일치하지 않습니다.';
          }
        }

        throw new Error(errorMessage);
      }

      return await response.json();

    } catch (error) {
      if (error instanceof TypeError && error.message.includes('fetch')) {
        throw new Error('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
      }

      throw error;
    }
  },

  // 회원가입
  async register(data: RegisterRequest): Promise<{ message: string }> {
    const response = await fetch(`${USER_SERVICE_URL}/api/members/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '회원가입 실패');
    }

    // 201 Created 상태코드와 빈 응답 본문 처리
    if (response.status === 201) {
      return { message: '회원가입 성공' };
    }

    // 다른 경우 JSON 파싱 시도
    try {
      return await response.json();
    } catch (e) {
      return { message: '회원가입 성공' };
    }
  },

  // 프로필 조회 (토큰 필요)
  async getProfile(): Promise<User> {
    const token = localStorage.getItem('accessToken');

    const response = await fetch(`${USER_SERVICE_URL}/api/members/profile`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || '프로필 조회 실패');
    }

    return response.json();
  },
};