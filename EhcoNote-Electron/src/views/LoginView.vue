<template>
  <Layout>
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>欢迎回来</h2>
          <p>登录你的EchoNote账户</p>
        </div>
        <el-form
          ref="loginForm"
          :model="formData"
          :rules="rules"
          class="auth-form"
        >
          <el-form-item prop="userName">
            <el-input
              v-model="formData.userName"
              placeholder="用户名"
              :prefix-icon="UserFilled"
              :validate-event="false"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="formData.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              :show-password="showPassword"
              @click:password="showPassword = !showPassword"
              :validate-event="false"
            ></el-input>
          </el-form-item>
          <el-form-item class="remember-me">
            <el-checkbox v-model="formData.rememberMe">记住我</el-checkbox>
            <router-link to="/forgot-password" class="forgot-password link"
              >忘记密码?</router-link
            >
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="auth-button"
              @click="handleLogin"
              :loading="loading"
              :disabled="loading"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="auth-footer">
          <p>
            还没有账户？<router-link to="/register" class="link"
              >立即注册</router-link
            >
          </p>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup>
/**
 * 登录页面组件
 * 提供用户登录功能，包含表单验证和提交逻辑
 */
// 导入所需图标
import { UserFilled, Lock } from "@element-plus/icons-vue";
// 导入Element Plus组件
import {
  ElForm,
  ElFormItem,
  ElInput,
  ElButton,
  ElCheckbox,
} from "element-plus";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
// 导入路由
import { useRouter, useRoute } from "vue-router";
// 导入Vue hooks
import { ref, reactive } from "vue";

// 初始化路由
const router = useRouter();
const route = useRoute();

// 表单数据
const formData = reactive({
  userName: "",
  password: "",
  rememberMe: false,
});

// 表单规则
const rules = {
  userName: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 20, message: "用户名长度在3-20个字符之间", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在6-20个字符之间", trigger: "blur" },
  ],
};

// 表单引用
const loginForm = ref(null);

// 加载状态
const loading = ref(false);

// 密码显示状态
const showPassword = ref(false);

// 登录处理函数
/**
 * 处理用户登录逻辑
 * 验证表单后模拟登录请求，成功后存储用户信息并跳转
 */
function handleLogin() {
  loginForm.value.validate((valid) => {
    if (valid) {
      // 模拟登录请求
      loading.value = true;
      setTimeout(() => {
        loading.value = false;
        // 模拟获取用户信息(实际应用中应从服务器返回)
        const userInfo = {
          userName: formData.userName,
          email: `${formData.userName}@example.com`, // 实际应用中应从服务器获取
        };

        // 存储用户信息到本地存储
        localStorage.setItem("userInfo", JSON.stringify(userInfo));

        // 登录成功，跳转到首页
        router.push("/");
      }, 1500);
    }
  });
}

// 检查是否有注册成功的提示
if (route.query.registered) {
  // 这里可以添加注册成功的提示
  console.log("注册成功，请登录");
}
</script>

<style scoped>
/* 认证容器样式 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 120px);
  padding: 2rem;
}

/* 认证卡片样式 */
.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 2.5rem;
  border-radius: 16px;
  background-color: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

/* 暗黑模式适配 */
.dark .auth-card {
  background-color: #2a2a2a;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

/* 认证头部样式 */
.auth-header {
  text-align: center;
  margin-bottom: 2rem;
}

.auth-header h2 {
  font-size: 1.8rem;
  margin-bottom: 0.5rem;
  color: #42b983;
}

.dark .auth-header h2 {
  color: #42b983;
}

.auth-header p {
  color: #666;
}

.dark .auth-header p {
  color: #b0b0b0;
}

/* 认证表单样式 */
.auth-form {
  width: 100%;
}

/* 表单项样式 */
.el-form-item {
  margin-bottom: 1.5rem;
}

/* 记住我和忘记密码样式 */
.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-password {
  margin-left: auto;
}

/* 认证按钮样式 */
.auth-button {
  width: 100%;
  padding: 0.75rem;
  font-size: 1rem;
  border-radius: 8px;
  background: linear-gradient(90deg, #42b983, #35495e);
  border: none;
  transition: all 0.3s ease;
}

.auth-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(66, 185, 131, 0.4);
}

.dark .auth-button {
  background: linear-gradient(90deg, #42b983, #5ac8fa);
}

/* 认证页脚样式 */
.auth-footer {
  text-align: center;
  margin-top: 1.5rem;
  color: #666;
}

.dark .auth-footer {
  color: #b0b0b0;
}

/* 链接样式 */
.link {
  color: #42b983;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link:hover {
  color: #35495e;
  text-decoration: underline;
}

.dark .link:hover {
  color: #5ac8fa;
}
</style>
