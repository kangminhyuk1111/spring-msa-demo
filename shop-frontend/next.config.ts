/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/members/:path*',
        destination: 'http://localhost:8081/api/members/:path*',
      },
      {
        source: '/api/products/:path*',
        destination: 'http://localhost:8082/api/products/:path*',
      },
      {
        source: '/api/orders/:path*',
        destination: 'http://localhost:8083/api/orders/:path*',
      },
      {
        source: '/api/points/:path*',
        destination: 'http://localhost:8084/api/points/:path*',
      },
    ];
  },
};

module.exports = nextConfig;