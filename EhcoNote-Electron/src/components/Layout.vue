<template>
  <div :class="['home-container', darkMode ? 'dark' : '']">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="logo-container">
        <img alt="EchoNote Logo" src="@/assets/logo.png" class="logo" />
        <router-link to="/">
          <h1 class="app-name">EchoNote</h1>
        </router-link>
      </div>
      <div class="right-controls">
        <div class="nav-container">
          <router-link to="/about">
            <h3 class="nav-item">关于</h3>
          </router-link>
        </div>
        <div class="auth-controls">
          <template v-if="userInfo">
            <div class="user-info">
              <span class="username">[{{ userInfo.userName }}]</span>
              <button @click="handleLogout" class="logout-btn">注销</button>
            </div>
          </template>
          <template v-else>
            <router-link to="/login" class="auth-link">登录</router-link>
          </template>
        </div>
        <div class="theme-toggle">
          <el-switch
            v-model="darkMode"
            :active-icon="Moon"
            :inactive-icon="Sunny"
            width="40px"
            @change="toggleDarkMode"
          ></el-switch>
        </div>
      </div>
    </header>

    <!-- 内容区域 -->
    <main class="main-content">
      <slot></slot>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2025 EchoNote. 保留所有权利。</p>
    </footer>
  </div>
</template>

<script setup>
/**
 * 公共布局组件
 * 包含顶部导航栏和页脚栏，提供内容插槽
 * 处理用户认证状态显示
 */
// 导入所需图标
import { Sunny, Moon } from "@element-plus/icons-vue";
import { ElSwitch } from "element-plus";
import { ref, onMounted, onBeforeUnmount, shallowRef } from "vue";
import { useRouter } from "vue-router";

// 定义暗黑模式状态
const darkMode = ref(false);
// 定义用户信息状态
const userInfo = ref(null);
// 定义路由
const router = useRouter();
// 定义resizeObserver引用
const resizeObserver = shallowRef(null);

// 主题切换函数
function toggleDarkMode() {
  if (darkMode.value) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
}

/**
 * 处理注销功能
 * 清除本地存储中的用户信息并跳转至登录页
 */
function handleLogout() {
  // 清除本地存储中的用户信息
  localStorage.removeItem("userInfo");
  // 更新用户信息状态
  userInfo.value = null;
  // 跳转到登录页
  router.push("/login");
}

/**
 * 从本地存储加载用户信息
 */
function loadUserInfo() {
  const storedUserInfo = localStorage.getItem("userInfo");
  if (storedUserInfo) {
    try {
      userInfo.value = JSON.parse(storedUserInfo);
    } catch (error) {
      console.error("解析用户信息失败:", error);
      localStorage.removeItem("userInfo");
    }
  }
}

// 挂载时执行
onMounted(() => {
  // 加载用户信息
  loadUserInfo();

  // 检查系统偏好
  if (
    window.matchMedia &&
    window.matchMedia("(prefers-color-scheme: dark)").matches
  ) {
    darkMode.value = true;
    document.documentElement.classList.add("dark");
  }

  resizeObserver.value = new ResizeObserver(() => {});

  // 观察当前组件根元素
  const el = document.querySelector(".home-container");
  if (el) {
    resizeObserver.value.observe(el);
  }
});

// 卸载前执行
onBeforeUnmount(() => {
  if (resizeObserver.value) {
    resizeObserver.value.disconnect();
  }
});
</script>

<style scoped>
/* 基础样式 */
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: "Avenir", "Helvetica", "Arial", sans-serif;
  color: #333;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* 暗黑模式样式 */
.dark {
  background-color: #1a1a1a;
  color: #e0e0e0;
}

/* 头部样式 */
.header {
  padding: 1rem 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  transition: background-color 0.3s ease;
}

.dark .header {
  background-color: #2a2a2a;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.right-controls {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

/* 用户认证控件样式 */
.auth-controls {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.username {
  font-weight: 500;
  color: #333;
  transition: color 0.3s ease;
}

.dark .username {
  color: #e0e0e0;
}

.logout-btn {
  padding: 0.35rem 0.75rem;
  border: none;
  border-radius: 4px;
  background-color: #f56c6c;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-btn:hover {
  background-color: #e53935;
}

.dark .logout-btn {
  background-color: #ff4d4f;
}

.dark .logout-btn:hover {
  background-color: #d9363e;
}

.auth-link {
  color: #42b983;
  text-decoration: none;
  transition: color 0.3s ease;
}

.auth-link:hover {
  color: #35495e;
  text-decoration: underline !important;
}

.dark .auth-link {
  color: #4cd964;
}

.dark .auth-link:hover {
  color: #5ac8fa;
}

.nav-container {
  margin: 0;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo {
  height: 2.5rem;
}

.app-name {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(90deg, #42b983, #35495e);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-decoration: none;
}

.dark .app-name {
  background: linear-gradient(90deg, #4cd964, #5ac8fa);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 0 10px rgba(76, 217, 100, 0.5);
}

.nav-item {
  color: #333;
  font-size: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
  text-decoration: none;
}

.nav-item:hover {
  color: #42b983;
  background-color: rgba(66, 185, 131, 0.1);
}

.dark .nav-item {
  color: #e0e0e0;
}

.dark .nav-item:hover {
  color: #4cd964;
  background-color: rgba(76, 217, 100, 0.1);
}

/* 主题切换按钮文本样式 */
.el-switch__label {
  color: #333;
  transition: color 0.3s ease;
}

.dark .el-switch__label {
  color: #e0e0e0;
}

/* 确保span元素在暗黑模式下可见 */
.dark span {
  color: #e0e0e0 !important;
}

/* 主内容区域样式 */
.main-content {
  flex: 1;
}

/* 页脚样式 */
.footer {
  text-align: center;
  padding: 1.5rem;
  color: #666;
  background-color: white;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.dark .footer {
  background-color: #2a2a2a;
  color: #b0b0b0;
}
/* 全局不设置下划线 */
.logo-container a,
.nav-container a {
  text-decoration: none;
}
</style>
