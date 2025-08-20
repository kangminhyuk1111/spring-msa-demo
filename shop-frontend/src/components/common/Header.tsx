// src/components/common/Header.tsx
'use client';

import { useRouter } from 'next/navigation';
import { useAuth } from '../../hooks/useAuth';

export default function Header() {
  const { isLoggedIn, logout, user } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  return (
      <header className="bg-white/80 backdrop-blur-md border-b border-gray-200/50 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-20">
            {/* 로고 */}
            <div className="flex items-center">
              <button
                  onClick={() => router.push('/')}
                  className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent hover:from-blue-700 hover:to-purple-700 transition-all duration-200"
              >
                MSA Shop
              </button>
            </div>

            {/* 네비게이션 메뉴 */}
            <nav className="hidden md:flex items-center space-x-1">
              <button
                  onClick={() => router.push('/products')}
                  className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200"
              >
                상품
              </button>
              {isLoggedIn && (
                  <>
                    <button
                        onClick={() => router.push('/orders')}
                        className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200"
                    >
                      주문내역
                    </button>
                    <button
                        onClick={() => router.push('/profile')}
                        className="text-gray-600 hover:text-gray-900 hover:bg-gray-100 px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200"
                    >
                      내 정보
                    </button>
                  </>
              )}
            </nav>

            {/* 로그인/로그아웃 버튼 */}
            <div className="flex items-center space-x-3">
              {isLoggedIn ? (
                  <div className="flex items-center space-x-3">
                    <div className="flex items-center space-x-2 bg-gray-50 px-3 py-2 rounded-xl">
                      <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center">
                    <span className="text-white text-sm font-medium">
                      {(user?.name || '사용자')[0].toUpperCase()}
                    </span>
                      </div>
                      <span className="text-sm font-medium text-gray-700">
                    {user?.name || '사용자'}님
                  </span>
                    </div>
                    <button
                        onClick={handleLogout}
                        className="bg-gray-900 text-white px-4 py-2 rounded-xl text-sm font-medium hover:bg-gray-800 transition-all duration-200"
                    >
                      로그아웃
                    </button>
                  </div>
              ) : (
                  <div className="flex items-center space-x-3">
                    <button
                        onClick={() => router.push('/login')}
                        className="text-gray-600 hover:text-gray-900 px-4 py-2 text-sm font-medium transition-all duration-200"
                    >
                      로그인
                    </button>
                    <button
                        onClick={() => router.push('/register')}
                        className="bg-gradient-to-r from-blue-600 to-purple-600 text-white px-6 py-2 rounded-xl text-sm font-medium hover:from-blue-700 hover:to-purple-700 transform hover:scale-105 transition-all duration-200 shadow-lg hover:shadow-xl"
                    >
                      회원가입
                    </button>
                  </div>
              )}
            </div>
          </div>
        </div>
      </header>
  );
}