import axios from "axios";
import { ElMessage } from "element-plus";
/**
 * 请求工具类
 * 封装Axios，提供标准请求前置头和基础URL
 */
class Request {
  constructor() {
    // 创建axios实例
    this.instance = axios.create({
      baseURL: "http://localhost:8080", // 基础URL
      timeout: 5000, // 请求超时时间
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
    });

    // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem("token");
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response) => {
        // 处理响应数据
        return response.data;
      },
      (error) => {
        // 处理响应错误
        if (
          (error.response && error.response.status === 401) ||
          (error.response && error.response.status === 403)
        ) {
          // 401错误，清除本地存储并跳转到登录页
          localStorage.removeItem("token");
          localStorage.removeItem("userInfo");
          // 跳转到登录页
          ElMessage.error("登录已过期，请重新登录");
          window.location.href = "/login";
        }
        return Promise.reject(error);
      }
    );
  }

  /**
   * GET请求
   * @param {string} url - 请求URL
   * @param {object} params - 请求参数
   * @param {object} config - 额外配置
   * @returns {Promise}
   */
  get(url, params = {}, config = {}) {
    return this.instance.get(url, { params, ...config });
  }

  /**
   * POST请求
   * @param {string} url - 请求URL
   * @param {object} data - 请求数据
   * @param {object} config - 额外配置
   * @returns {Promise}
   */
  post(url, data = {}, config = {}) {
    return this.instance.post(url, data, config);
  }

  /**
   * PUT请求
   * @param {string} url - 请求URL
   * @param {object} data - 请求数据
   * @param {object} config - 额外配置
   * @returns {Promise}
   */
  put(url, data = {}, config = {}) {
    return this.instance.put(url, data, config);
  }

  /**
   * DELETE请求
   * @param {string} url - 请求URL
   * @param {object} params - 请求参数
   * @param {object} config - 额外配置
   * @returns {Promise}
   */
  delete(url, params = {}, config = {}) {
    return this.instance.delete(url, { params, ...config });
  }
}

// 导出实例
export default new Request();
