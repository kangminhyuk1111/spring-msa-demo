// src/lib/api/products.ts
import { Product, ProductListResponse, CreateProductRequest, UpdateProductRequest } from '../types/product';

const PRODUCT_SERVICE_URL = '';

export const productService = {
  // 상품 목록 조회 (페이징)
  async getProducts(page: number = 1, size: number = 10): Promise<ProductListResponse> {
    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products?page=${page}&size=${size}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품 목록을 불러올 수 없습니다.');
    }

    return response.json();
  },

  // 상품 상세 조회
  async getProduct(id: number): Promise<Product> {
    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품을 찾을 수 없습니다.');
    }

    return response.json();
  },

  // 상품 검색
  async searchProducts(keyword: string, page: number = 1, size: number = 10): Promise<ProductListResponse> {
    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products/search?name=${encodeURIComponent(keyword)}&page=${page}&size=${size}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품 검색에 실패했습니다.');
    }

    return response.json();
  },

  // 상품 등록 (관리자용)
  async createProduct(data: CreateProductRequest): Promise<Product> {
    const token = localStorage.getItem('accessToken');

    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품 등록에 실패했습니다.');
    }

    return response.json();
  },

  // 상품 수정 (관리자용)
  async updateProduct(id: number, data: UpdateProductRequest): Promise<Product> {
    const token = localStorage.getItem('accessToken');

    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품 수정에 실패했습니다.');
    }

    return response.json();
  },

  // 상품 삭제 (관리자용)
  async deleteProduct(id: number): Promise<void> {
    const token = localStorage.getItem('accessToken');

    const response = await fetch(`${PRODUCT_SERVICE_URL}/api/products/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || '상품 삭제에 실패했습니다.');
    }
  },
};