// src/hooks/useAuth.ts
import { useState, useEffect } from 'react';
import { authService } from '../lib/api/auth';
import { LoginRequest, RegisterRequest, User } from '../lib/types/auth';
import toast from 'react-hot-toast';

export const useAuth = () => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // 페이지 로드시 토큰 확인
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      setIsLoggedIn(true);
      // 필요하면 프로필 정보도 가져오기
      // loadProfile();
    }
  }, []);

  // 로그인 함수
  const login = async (data: LoginRequest) => {
    try {
      setIsLoading(true);
      const response = await authService.login(data);

      // 토큰 저장
      localStorage.setItem('accessToken', response.accessToken);
      setIsLoggedIn(true);

      toast.success('로그인 성공!');
      return true;
    } catch (error: any) {
      console.log('useAuth login 에러:', error);
      console.log('에러 메시지:', error.message);

      // 에러 메시지 직접 사용
      const errorMessage = error.message || '로그인 실패';
      toast.error(errorMessage);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  // 회원가입 함수
  const register = async (data: RegisterRequest) => {
    try {
      setIsLoading(true);
      await authService.register(data);

      toast.success('회원가입 성공! 로그인해주세요.');
      return true;
    } catch (error: any) {
      toast.error('회원가입 실패: ' + (error.response?.data?.message || '알 수 없는 오류'));
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  // 로그아웃 함수
  const logout = () => {
    localStorage.removeItem('accessToken');
    setUser(null);
    setIsLoggedIn(false);
    toast.success('로그아웃되었습니다.');
  };

  // 프로필 로드 함수
  const loadProfile = async () => {
    try {
      const userData = await authService.getProfile();
      setUser(userData);
    } catch (error) {
      console.error('프로필 로드 실패:', error);
    }
  };

  return {
    user,
    isLoading,
    isLoggedIn,
    login,
    register,
    logout,
    loadProfile,
  };
};