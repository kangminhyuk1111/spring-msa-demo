// src/app/products/page.tsx
'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { productService } from "@/lib/api/product";
import { Product } from '../../lib/types/product';
import Header from '../../components/common/Header';
import toast from 'react-hot-toast';

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const router = useRouter();

  // 상품 목록 로드
  const loadProducts = async (page: number = 1, keyword: string = '') => {
    console.log('loadProducts 함수 시작됨', { page, keyword });

    try {
      setLoading(true);
      console.log('로딩 상태 true로 설정');

      let response;

      if (keyword.trim()) {
        console.log('검색 모드로 API 호출');
        const allProducts = await productService.getProducts(1, 100); // 전체 가져와서 필터링
        const filteredProducts = allProducts.filter(product =>
            product.name.toLowerCase().includes(keyword.toLowerCase())
        );
        setProducts(filteredProducts);
        setTotalPages(1);
        setCurrentPage(1);
      } else {
        console.log('일반 목록 모드로 API 호출');
        const products = await productService.getProducts(page, 12);
        setProducts(products);
        setTotalPages(1); // 백엔드에서 페이징 지원 안함
        setCurrentPage(1);
      }

      console.log('상태 업데이트 완료');
    } catch (error: any) {
      console.error('상품 로드 실패:', error);
      toast.error(error.message || '상품을 불러올 수 없습니다.');
      setProducts([]);
    } finally {
      setLoading(false);
      console.log('로딩 상태 false로 설정');
    }
  };

  // 컴포넌트 마운트시 상품 로드
  useEffect(() => {
    console.log('useEffect 실행됨 - 상품 로드 시작');
    loadProducts(1);
  }, []);

  // 검색 처리
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    loadProducts(1, searchKeyword);
  };

  // 페이지 변경
  const handlePageChange = (page: number) => {
    loadProducts(page, searchKeyword);
  };

  // 가격 포맷팅
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('ko-KR').format(price) + '원';
  };

  return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 via-white to-blue-50">
        <Header />

        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* 페이지 헤더 */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-4">상품 목록</h1>

            {/* 검색 바 */}
            <form onSubmit={handleSearch} className="max-w-md">
              <div className="flex">
                <input
                    type="text"
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    placeholder="상품명을 검색하세요..."
                    className="flex-1 px-4 py-3 border border-gray-300 rounded-l-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
                <button
                    type="submit"
                    className="bg-gradient-to-r from-blue-600 to-purple-600 text-white px-6 py-3 rounded-r-xl hover:from-blue-700 hover:to-purple-700 transition-all duration-200"
                >
                  검색
                </button>
              </div>
            </form>
          </div>

          {/* 로딩 상태 */}
          {loading ? (
              <div className="flex justify-center items-center py-20">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              </div>
          ) : (
              <>
                {/* 상품 그리드 */}
                {products.length > 0 ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
                      {products.map((product) => (
                          <div
                              key={product.id}
                              className="group bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-200/50 hover:scale-105 cursor-pointer"
                              onClick={() => router.push(`/products/${product.id}`)}
                          >
                            {/* 상품 이미지 플레이스홀더 */}
                            <div className="w-full h-48 bg-gradient-to-br from-gray-200 to-gray-300 flex items-center justify-center">
                              <svg className="w-16 h-16 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                              </svg>
                            </div>

                            {/* 상품 정보 */}
                            <div className="p-6">
                              <h3 className="text-lg font-semibold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors duration-200">
                                {product.name}
                              </h3>

                              {product.description && (
                                  <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                                    {product.description}
                                  </p>
                              )}

                              <div className="flex justify-between items-center">
                        <span className="text-xl font-bold text-blue-600">
                          {formatPrice(product.price)}
                        </span>

                                <div className="text-sm text-gray-500">
                                  재고: {product.stock}개
                                </div>
                              </div>

                              {/* 품절 표시 */}
                              {product.stock === 0 && (
                                  <div className="mt-3 bg-red-100 text-red-600 px-3 py-1 rounded-lg text-sm font-medium text-center">
                                    품절
                                  </div>
                              )}
                            </div>
                          </div>
                      ))}
                    </div>
                ) : (
                    /* 상품이 없을 때 */
                    <div className="text-center py-20">
                      <svg className="mx-auto h-16 w-16 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                      </svg>
                      <h3 className="text-lg font-medium text-gray-900 mb-2">상품이 없습니다</h3>
                      <p className="text-gray-600">
                        {searchKeyword ? '검색 결과가 없습니다.' : '등록된 상품이 없습니다.'}
                      </p>
                    </div>
                )}

                {/* 페이지네이션 */}
                {totalPages > 1 && (
                    <div className="flex justify-center space-x-2">
                      <button
                          onClick={() => handlePageChange(currentPage - 1)}
                          disabled={currentPage <= 1}
                          className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        이전
                      </button>

                      {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                        const page = Math.max(1, Math.min(totalPages - 4, currentPage - 2)) + i;
                        return (
                            <button
                                key={page}
                                onClick={() => handlePageChange(page)}
                                className={`px-4 py-2 text-sm font-medium rounded-lg ${
                                    currentPage === page
                                        ? 'bg-blue-600 text-white'
                                        : 'text-gray-700 bg-white border border-gray-300 hover:bg-gray-50'
                                }`}
                            >
                              {page}
                            </button>
                        );
                      })}

                      <button
                          onClick={() => handlePageChange(currentPage + 1)}
                          disabled={currentPage >= totalPages}
                          className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        다음
                      </button>
                    </div>
                )}
              </>
          )}
        </main>
      </div>
  );
}