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
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
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
  ElMessage,
} from "element-plus";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
// 导入路由
import { useRouter, useRoute } from "vue-router";
// 导入Vue hooks
import { ref, reactive, onMounted } from "vue";

// 导入登录API
import { login } from "@/utils/api";

// 初始化路由
const router = useRouter();
const route = useRoute();

// 记住我状态
const rememberMe = ref(false);

// 表单数据
const formData = reactive({
  userName: "",
  password: "",
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
 * 验证表单后调用登录API，成功后存储用户信息和token并跳转
 * 根据rememberMe状态存储或清除本地凭证
 */
function handleLogin() {
  loginForm.value.validate((valid) => {
    if (valid) {
      // 调用登录API
      loading.value = true;
      login(formData)
        .then((response) => {
          loading.value = false;
          const { code, message, data } = response;
          if (code === 200) {
            // 登录成功，存储用户信息和token
            const { userInfo, token } = data;
            localStorage.setItem("userInfo", JSON.stringify(userInfo));
            localStorage.setItem("token", token);

            // 根据rememberMe状态处理本地凭证
            if (rememberMe.value) {
              // 存储账号密码
              localStorage.setItem(
                "rememberedCredentials",
                JSON.stringify({
                  userName: formData.userName,
                  password: formData.password,
                })
              );
            } else {
              // 清除本地存储的凭证
              localStorage.removeItem("rememberedCredentials");
            }

            // 显示成功消息
            ElMessage.success(message);

            // 跳转到首页
            router.push("/");
          } else {
            // 显示失败消息
            ElMessage.error(message || "登录失败");
          }
        })
        .catch((error) => {
          loading.value = false;
          console.error("登录请求失败:", error);
          // 1. 有响应，且为 401
          if (error.response?.status === 401) {
            ElMessage.error("用户名或密码错误");
          }
          // 2. 有响应，其它状态码
          else if (error.response) {
            ElMessage.error(error.response.data?.message || "登录失败");
          }
          // 3. 无响应（断网、超时、CORS）
          else if (error.request) {
            ElMessage.error("网络异常，请检查网络连接");
          }
          // 4. 其它未知错误
          else {
            ElMessage.error("系统异常，请稍后重试");
          }
        });
    }
  });
}

/**
 * 从本地存储加载记住的凭证
 */
function loadRememberedCredentials() {
  const storedCredentials = localStorage.getItem("rememberedCredentials");
  if (storedCredentials) {
    try {
      const { userName, password } = JSON.parse(storedCredentials);
      formData.userName = userName;
      formData.password = password;
      rememberMe.value = true;
    } catch (error) {
      console.error("解析记住的凭证失败:", error);
      localStorage.removeItem("rememberedCredentials");
    }
  }
}

// 组件挂载时执行
onMounted(() => {
  // 加载记住的凭证
  loadRememberedCredentials();

  // 检查是否有注册成功的提示
  if (route.query.registered) {
    // 显示注册成功的提示
    ElMessage.success("注册成功，请登录");
  }
});
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
