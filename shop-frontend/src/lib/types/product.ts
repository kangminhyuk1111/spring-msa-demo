// src/lib/types/product.ts
export interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  description?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface ProductListResponse {
  products: Product[];
  totalCount?: number;
  currentPage?: number;
  totalPages?: number;
}

export interface CreateProductRequest {
  name: string;
  price: number;
  stock: number;
  description?: string;
}

export interface UpdateProductRequest {
  name?: string;
  price?: number;
  stock?: number;
  description?: string;
  isActive?: boolean;
}