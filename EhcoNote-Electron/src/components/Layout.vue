<template>
  <div :class="['layout-container', darkMode ? 'dark' : '']">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-particles">
        <div
          class="particle"
          v-for="i in 20"
          :key="i"
          :style="getParticleStyle()"
        ></div>
      </div>
    </div>

    <!-- 顶部导航 -->
    <header class="header">
      <div class="header-backdrop"></div>
      <div class="header-content">
        <div class="logo-section">
          <div class="logo-container">
            <div class="logo-wrapper">
              <img alt="EchoNote Logo" src="@/assets/logo.png" class="logo" />
            </div>
            <router-link to="/" class="app-name-link">
              <h1 class="app-name">
                <span class="app-name-echo">Echo</span
                ><span class="app-name-note">Note</span>
                <div class="app-name-subtitle">思维回响平台</div>
              </h1>
            </router-link>
          </div>
        </div>

        <div class="nav-section">
          <nav class="main-nav">
            <router-link to="/about" class="nav-item">
              <span class="nav-icon">📖</span>
              <span class="nav-text">关于</span>
              <div class="nav-indicator"></div>
            </router-link>
          </nav>
        </div>

        <div class="control-section">
          <div class="auth-controls">
            <template v-if="userInfo">
              <div class="compact-user-info">
                <div class="user-avatar">
                  <span class="avatar-text">{{
                    userInfo.name?.charAt(0) || "U"
                  }}</span>
                </div>
                <span class="compact-username">{{ userInfo.name }}</span>
                <button
                  @click="handleLogout"
                  class="compact-logout-btn"
                  title="注销"
                >
                  <span class="logout-icon">🚪</span>
                </button>
              </div>
            </template>
            <template v-else>
              <router-link to="/login" class="compact-auth-link">
                <span class="auth-icon">👤</span>
                <span>登录</span>
              </router-link>
            </template>
          </div>

          <div class="compact-theme-toggle">
            <el-switch
              v-model="darkMode"
              size="small"
              @change="toggleDarkMode"
              class="theme-switch"
            ></el-switch>
            <span class="theme-indicator">{{ darkMode ? "🌙" : "☀️" }}</span>
          </div>
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
import { ElSwitch, ElMessageBox } from "element-plus";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

// 定义状态
const darkMode = ref(false);
const userInfo = ref(null);
const router = useRouter();

// 生成粒子样式
function getParticleStyle(index) {
  const size = Math.random() * 4 + 2;
  const duration = Math.random() * 20 + 10;
  const delay = Math.random() * 5;
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDuration: `${duration}s`,
    animationDelay: `${delay}s`,
  };
}

// 主题切换函数
function toggleDarkMode() {
  document.documentElement.classList.toggle("dark", darkMode.value);
  localStorage.setItem("darkMode", darkMode.value);
}

// 处理注销功能
function handleLogout() {
  ElMessageBox.confirm("确定要注销当前账户吗？", "注销确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      localStorage.removeItem("userInfo");
      localStorage.removeItem("token");
      userInfo.value = null;
      router.push("/login");
    })
    .catch(() => {});
}

// 初始化
onMounted(() => {
  // 加载用户信息
  const storedUserInfo = localStorage.getItem("userInfo");
  if (storedUserInfo) {
    try {
      userInfo.value = JSON.parse(storedUserInfo);
    } catch (error) {
      localStorage.removeItem("userInfo");
    }
  }

  // 加载主题设置
  const savedDarkMode = localStorage.getItem("darkMode");
  darkMode.value = savedDarkMode === "true";
  document.documentElement.classList.toggle("dark", darkMode.value);
});
</script>

<style scoped>
/* 基础样式 */
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: "Avenir", "Helvetica", "Arial", sans-serif;
  color: #333;
  transition: all 0.3s ease;
  position: relative;
  overflow-x: hidden;
}

/* 背景装饰 */
.background-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;
}

.bg-gradient-1 {
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(
    circle,
    rgba(66, 185, 131, 0.1) 0%,
    transparent 70%
  );
  animation: float1 20s ease-in-out infinite;
}

.bg-gradient-2 {
  position: absolute;
  bottom: -50%;
  left: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(
    circle,
    rgba(102, 126, 234, 0.08) 0%,
    transparent 70%
  );
  animation: float2 25s ease-in-out infinite;
}

@keyframes float1 {
  0%,
  100% {
    transform: translate(0, 0) rotate(0deg) scale(1);
  }
  33% {
    transform: translate(30px, -30px) rotate(1deg) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) rotate(-1deg) scale(0.9);
  }
}

@keyframes float2 {
  0%,
  100% {
    transform: translate(0, 0) rotate(0deg) scale(1);
  }
  33% {
    transform: translate(-30px, 30px) rotate(-1deg) scale(1.1);
  }
  66% {
    transform: translate(20px, -20px) rotate(1deg) scale(0.9);
  }
}

.bg-particles {
  position: absolute;
  width: 100%;
  height: 100%;
}

.particle {
  position: absolute;
  background: linear-gradient(
    45deg,
    rgba(66, 185, 131, 0.6),
    rgba(102, 126, 234, 0.6)
  );
  border-radius: 50%;
  animation: particle-float infinite linear;
}

@keyframes particle-float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

/* 暗黑模式样式 */
.dark {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #334155 100%);
  color: #e0e0e0;
}

.dark .bg-gradient-1 {
  background: radial-gradient(
    circle,
    rgba(76, 217, 100, 0.15) 0%,
    transparent 70%
  );
}

.dark .bg-gradient-2 {
  background: radial-gradient(
    circle,
    rgba(168, 85, 247, 0.12) 0%,
    transparent 70%
  );
}

.dark .particle {
  background: linear-gradient(
    45deg,
    rgba(76, 217, 100, 0.4),
    rgba(168, 85, 247, 0.4)
  );
}

/* 头部样式 */
.header {
  position: relative;
  padding: 0;
  height: 80px;
  display: flex;
  align-items: center;
  background: transparent;
  z-index: 100;
}

.header-backdrop {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 1;
}

.dark .header-backdrop {
  background: rgba(15, 23, 42, 0.8);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-content {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

/* Logo区域 */
.logo-section {
  display: flex;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo {
  height: 3rem;
  width: 3rem;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.app-name-link {
  text-decoration: none;
  display: flex;
  flex-direction: column;
}

.app-name {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 700;
  display: flex;
  align-items: baseline;
  gap: 2px;
  transition: all 0.3s ease;
}

.app-name-echo {
  background: linear-gradient(135deg, #42b983, #369870);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 2px 10px rgba(66, 185, 131, 0.3);
}

.app-name-note {
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 2px 10px rgba(102, 126, 234, 0.3);
}

.app-name-subtitle {
  font-size: 0.7rem;
  font-weight: 400;
  color: #666;
  margin-top: -5px;
  letter-spacing: 2px;
  opacity: 0.8;
}

.dark .app-name-echo {
  background: linear-gradient(135deg, #4cd964, #40c057);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .app-name-note {
  background: linear-gradient(135deg, #a855f7, #8b5cf6);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .app-name-subtitle {
  color: #9ca3af;
}

/* 导航区域 */
.nav-section {
  flex: 1;
  display: flex;
  justify-content: center;
}

.main-nav {
  display: flex;
  gap: 2rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 25px;
  text-decoration: none;
  color: #555;
  font-weight: 500;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.nav-item:hover {
  background: rgba(66, 185, 131, 0.1);
  color: #42b983;
  box-shadow: 0 4px 15px rgba(66, 185, 131, 0.2);
}

.dark .nav-item {
  background: rgba(15, 23, 42, 0.6);
  color: #cbd5e1;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .nav-item:hover {
  background: rgba(76, 217, 100, 0.1);
  color: #4cd964;
  box-shadow: 0 8px 25px rgba(76, 217, 100, 0.2);
}

/* 用户认证控件样式 */
.auth-controls {
  display: flex;
  align-items: center;
}

/* 用户头像样式 */
.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #42b983, #667eea);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 0.8rem;
  flex-shrink: 0;
}

.dark .user-avatar {
  background: linear-gradient(135deg, #4cd964, #a855f7);
}

/* 紧凑用户信息区域 */
.compact-user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(15px);
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-height: 40px;
}

.compact-user-info:hover {
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-1px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.compact-username {
  font-weight: 500;
  color: #333;
  font-size: 0.9rem;
  margin: 0 4px;
  transition: color 0.3s ease;
}

.compact-logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 50%;
  background: linear-gradient(135deg, #f56c6c, #e53935);
  color: white;
  cursor: pointer;
  font-size: 0.75rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-left: 4px;
}

.compact-logout-btn:hover {
  background: linear-gradient(135deg, #e53935, #d32f2f);
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
}

.compact-auth-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(66, 185, 131, 0.1);
  color: #42b983;
  text-decoration: none;
  border-radius: 25px;
  font-weight: 500;
  font-size: 0.9rem;
  border: 1px solid rgba(66, 185, 131, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-height: 40px;
}

.compact-auth-link:hover {
  background: rgba(66, 185, 131, 0.2);
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(66, 185, 131, 0.3);
  text-decoration: none;
}

/* 紧凑主题切换 */
.compact-theme-toggle {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(15px);
  border-radius: 25px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-height: 40px;
}

.compact-theme-toggle:hover {
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-1px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.theme-indicator {
  font-size: 1rem;
  transition: transform 0.3s ease;
}

.compact-theme-toggle:hover .theme-indicator {
  transform: scale(1.1);
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

/* 控制区域 */
.control-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 暗黑模式适配增强 */
.dark .compact-user-info {
  background: rgba(15, 23, 42, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .compact-user-info:hover {
  background: rgba(15, 23, 42, 0.95);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.dark .compact-username {
  color: #e2e8f0;
}

.dark .compact-auth-link {
  background: rgba(76, 217, 100, 0.1);
  color: #4cd964;
  border: 1px solid rgba(76, 217, 100, 0.3);
}

.dark .compact-auth-link:hover {
  background: rgba(76, 217, 100, 0.2);
  box-shadow: 0 4px 15px rgba(76, 217, 100, 0.3);
}

.dark .compact-theme-toggle {
  background: rgba(15, 23, 42, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .compact-theme-toggle:hover {
  background: rgba(15, 23, 42, 0.95);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

/* 响应式设计优化 */
@media (max-width: 768px) {
  .control-section {
    gap: 0.5rem;
  }

  .compact-user-info {
    padding: 6px 8px;
    gap: 6px;
  }

  .compact-username {
    font-size: 0.8rem;
    margin: 0 2px;
  }

  .compact-logout-btn {
    width: 24px;
    height: 24px;
    font-size: 0.7rem;
  }

  .compact-auth-link {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .compact-theme-toggle {
    padding: 6px 8px;
    gap: 6px;
  }

  .user-avatar {
    width: 24px;
    height: 24px;
    font-size: 0.75rem;
  }
}

@media (max-width: 480px) {
  .control-section {
    gap: 0.25rem;
  }

  .compact-username {
    max-width: 80px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
