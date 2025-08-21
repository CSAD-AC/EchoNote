<template>
  <Layout>
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>忘记密码</h2>
          <p>请输入你的邮箱，我们将发送重置密码的链接</p>
        </div>
        <el-form
          ref="forgotPasswordForm"
          :model="formData"
          :rules="rules"
          class="auth-form"
        >
          <el-form-item prop="email">
            <el-input
              v-model="formData.email"
              placeholder="电子邮箱"
              :prefix-icon="MessageFilled"
              :validate-event="false"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="auth-button"
              @click="handleSubmit"
              :loading="loading"
              :disabled="loading"
            >
              发送重置链接
            </el-button>
          </el-form-item>
        </el-form>
        <div class="auth-footer">
          <p>
            记得密码了？<router-link to="/login" class="link"
              >返回登录</router-link
            >
          </p>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup>
/**
 * 忘记密码页面组件
 * 提供密码重置功能，通过邮箱发送重置链接
 */
// 导入所需图标
import { MessageFilled } from "@element-plus/icons-vue";
// 导入Element Plus组件
import { ElForm, ElFormItem, ElInput, ElButton } from "element-plus";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
// 导入路由
import { useRouter } from "vue-router";
// 导入Vue hooks
import { ref, reactive } from "vue";

// 初始化路由
const router = useRouter();

// 表单数据
const formData = reactive({
  email: "",
});

// 表单规则
const rules = {
  email: [
    { required: true, message: "请输入电子邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
  ],
};

// 表单引用
const forgotPasswordForm = ref(null);

// 加载状态
const loading = ref(false);

// 提交处理函数
function handleSubmit() {
  forgotPasswordForm.value.validate((valid) => {
    if (valid) {
      // 模拟发送重置链接请求
      loading.value = true;
      setTimeout(() => {
        loading.value = false;
        // 模拟发送成功，跳转到登录页
        router.push("/login");
      }, 1500);
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
