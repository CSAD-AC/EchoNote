<template>
  <Layout>
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>创建账户</h2>
          <p>开始你的EchoNote之旅</p>
        </div>
        <el-form
          ref="registerForm"
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
          <el-form-item prop="email">
            <el-input
              v-model="formData.email"
              placeholder="电子邮箱"
              :prefix-icon="MessageFilled"
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
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="formData.confirmPassword"
              type="password"
              placeholder="确认密码"
              :prefix-icon="Lock"
              :show-password="showConfirmPassword"
              @click:password="showConfirmPassword = !showConfirmPassword"
              :validate-event="false"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="auth-button"
              @click="handleRegister"
              :loading="loading"
              :disabled="loading"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="auth-footer">
          <p>
            已有账户？<router-link to="/login" class="link"
              >立即登录</router-link
            >
          </p>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup>
/**
 * 注册页面组件
 * 提供用户注册功能，包含表单验证和提交逻辑
 */
// 导入所需图标
import { UserFilled, MessageFilled, Lock } from "@element-plus/icons-vue";
// 导入Element Plus组件
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage } from "element-plus";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
// 导入路由
import { useRouter } from "vue-router";
// 导入Vue hooks
import { ref, reactive } from "vue";
// 导入注册API
import { register } from "@/utils/api";

// 初始化路由
const router = useRouter();

// 表单数据
const formData = reactive({
  userName: "",
  email: "",
  password: "",
  confirmPassword: "",
});

// 表单规则
const rules = {
  userName: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 20, message: "用户名长度在3-20个字符之间", trigger: "blur" },
  ],
  email: [
    { required: true, message: "请输入电子邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在6-20个字符之间", trigger: "blur" },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/,
      message: "密码必须包含字母和数字",
      trigger: "blur",
    },
  ],
  confirmPassword: [
    { required: true, message: "请确认密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error("两次输入密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

// 表单引用
const registerForm = ref(null);

// 加载状态
const loading = ref(false);

// 密码显示状态
const showPassword = ref(false);
const showConfirmPassword = ref(false);

function reflushRegisterForm() {
  formData.userName = "";
  formData.email = "";
  formData.password = "";
  formData.confirmPassword = "";
}
// 注册处理函数
function handleRegister() {
  registerForm.value.validate((valid) => {
    if (valid) {
      // 准备注册数据
      const registerData = {
        userName: formData.userName,
        password: formData.password,
        email: formData.email,
      };

      // 发送注册请求
      loading.value = true;
      register(registerData)
        .then((response) => {
          loading.value = false;
          if (response.code === 200) {
            ElMessage.success(response.message || "注册成功");
            // 注册成功，跳转到登录页
            router.push({
              path: "/login",
              query: { registered: true, userName: formData.userName },
            });
          } else {
            reflushRegisterForm();
            ElMessage.error(response.message || "注册失败，请重试");
          }
        })
        .catch((error) => {
          loading.value = false;
          reflushRegisterForm();
          ElMessage.error("网络错误，请稍后重试");
          console.error("注册失败:", error);
        });
    }
  });
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
