// src/app/page.tsx
'use client';

import { useRouter } from 'next/navigation';
import { useAuth } from '../hooks/useAuth';
import Header from '../components/common/Header';

export default function HomePage() {
  const { isLoggedIn, user } = useAuth();
  const router = useRouter();

  return (
      <div className="min-h-screen bg-white">
        <Header />

        {/* 메인 컨텐츠 */}
        <main className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Hero Section */}
          <div className="pt-20 pb-16 text-center">
            <div className="max-w-3xl mx-auto">
              <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-4">
                <span className="text-blue-600">MSA Shop</span>
              </h1>
              <p className="text-lg md:text-xl text-gray-600 mb-8">
                간편하고 안전한 온라인 쇼핑몰
              </p>

              {!isLoggedIn ? (
                  <div className="flex flex-col sm:flex-row justify-center gap-3 mb-12">
                    <button
                        onClick={() => router.push('/register')}
                        className="bg-blue-600 text-white px-6 py-3 rounded-lg text-base font-medium hover:bg-blue-700 transition-colors duration-200"
                    >
                      회원가입
                    </button>
                    <button
                        onClick={() => router.push('/login')}
                        className="bg-white text-blue-600 px-6 py-3 rounded-lg text-base font-medium border border-blue-600 hover:bg-blue-50 transition-colors duration-200"
                    >
                      로그인
                    </button>
                  </div>
              ) : (
                  <div className="max-w-sm mx-auto mb-12">
                    <div className="bg-blue-50 rounded-xl p-6 border border-blue-100">
                      <div className="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center mx-auto mb-3">
                    <span className="text-white text-lg font-medium">
                      {(user?.name || '사용자')[0].toUpperCase()}
                    </span>
                      </div>
                      <h2 className="text-xl font-semibold text-gray-900 mb-2">
                        환영합니다
                      </h2>
                      <p className="text-gray-600 mb-4">
                        {user?.name || '사용자'}님
                      </p>
                      <button
                          onClick={() => router.push('/products')}
                          className="w-full bg-blue-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-blue-700 transition-colors duration-200"
                      >
                        상품 보기
                      </button>
                    </div>
                  </div>
              )}
            </div>
          </div>

          {/* 기능 소개 섹션 */}
          <div className="grid md:grid-cols-3 gap-6 mb-16">
            <div className="bg-white rounded-xl p-6 border border-gray-200 hover:border-blue-200 hover:shadow-sm transition-all duration-200">
              <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mb-4">
                <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                </svg>
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">다양한 상품</h3>
              <p className="text-gray-600 text-sm">엄선된 상품들을 합리적인 가격으로</p>
            </div>

            <div className="bg-white rounded-xl p-6 border border-gray-200 hover:border-blue-200 hover:shadow-sm transition-all duration-200">
              <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mb-4">
                <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                </svg>
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">포인트 적립</h3>
              <p className="text-gray-600 text-sm">구매할 때마다 포인트를 적립하세요</p>
            </div>

            <div className="bg-white rounded-xl p-6 border border-gray-200 hover:border-blue-200 hover:shadow-sm transition-all duration-200">
              <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mb-4">
                <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">안전한 거래</h3>
              <p className="text-gray-600 text-sm">신뢰할 수 있는 결제 시스템</p>
            </div>
          </div>

          {/* 빠른 링크 - 로그인한 사용자만 */}
          {isLoggedIn && (
              <div className="mb-16">
                <div className="bg-gray-50 rounded-xl p-6">
                  <h2 className="text-xl font-semibold text-gray-900 mb-6 text-center">바로 가기</h2>
                  <div className="grid md:grid-cols-3 gap-4">
                    <button
                        onClick={() => router.push('/products')}
                        className="text-left p-4 bg-white border border-gray-200 rounded-lg hover:border-blue-200 hover:bg-blue-50 transition-all duration-200"
                    >
                      <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center mb-3">
                        <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                        </svg>
                      </div>
                      <h3 className="font-medium text-gray-900 mb-1">상품 목록</h3>
                      <p className="text-sm text-gray-600">다양한 상품을 둘러보세요</p>
                    </button>

                    <button
                        onClick={() => router.push('/orders')}
                        className="text-left p-4 bg-white border border-gray-200 rounded-lg hover:border-blue-200 hover:bg-blue-50 transition-all duration-200"
                    >
                      <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center mb-3">
                        <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                        </svg>
                      </div>
                      <h3 className="font-medium text-gray-900 mb-1">주문 내역</h3>
                      <p className="text-sm text-gray-600">주문 상태를 확인하세요</p>
                    </button>

                    <button
                        onClick={() => router.push('/profile')}
                        className="text-left p-4 bg-white border border-gray-200 rounded-lg hover:border-blue-200 hover:bg-blue-50 transition-all duration-200"
                    >
                      <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center mb-3">
                        <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                      </div>
                      <h3 className="font-medium text-gray-900 mb-1">내 정보</h3>
                      <p className="text-sm text-gray-600">프로필과 포인트 관리</p>
                    </button>
                  </div>
                </div>
              </div>
          )}

          {/* 푸터 */}
          <footer className="text-center py-8 border-t border-gray-200">
            <p className="text-gray-500 text-sm">&copy; 2025 MSA Shop. All rights reserved.</p>
          </footer>
        </main>
      </div>
  );
}