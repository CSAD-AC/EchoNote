<template>
  <Layout>
    <!-- 功能卡片区域 -->
    <section class="features-container">
      <el-row :gutter="20">
        <!-- 灵光胶囊 -->
        <el-col :xs="24" :sm="12" :md="12" class="feature-col">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
            @click="navigateToFeature('capture')"
            style="
              cursor: pointer;
              transition: transform 0.3s ease, box-shadow 0.3s ease;
            "
            @mouseenter="handleMouseEnter($event.currentTarget)"
            @mouseleave="handleMouseLeave($event.currentTarget)"
          >
            <div class="card-content">
              <div class="icon-container lightbulb">
                <Plus size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">灵光胶囊</h3>
                <p class="feature-goal">瞬时捕捉</p>
                <el-button type="primary" class="feature-button" size="large"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 心流写作 -->
        <el-col :xs="24" :sm="12" :md="12" class="feature-col">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
            @click="navigateToFeature('writing')"
            style="
              cursor: pointer;
              transition: transform 0.3s ease, box-shadow 0.3s ease;
            "
            @mouseenter="handleMouseEnter($event.currentTarget)"
            @mouseleave="handleMouseLeave($event.currentTarget)"
          >
            <div class="card-content">
              <div class="icon-container edit">
                <Edit size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">心流写作</h3>
                <p class="feature-goal">高效输出</p>
                <el-button type="primary" class="feature-button" size="large"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 思维慢发酵 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
            @click="navigateToFeature('slow-fermentation')"
            style="
              cursor: pointer;
              transition: transform 0.3s ease, box-shadow 0.3s ease;
            "
            @mouseenter="handleMouseEnter($event.currentTarget)"
            @mouseleave="handleMouseLeave($event.currentTarget)"
          >
            <div class="card-content">
              <div class="icon-container coffee">
                <ChatDotRound size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">思维慢发酵</h3>
                <p class="feature-goal">沉淀反思</p>
                <el-button type="primary" class="feature-button" size="large"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 思维迷宫 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
            @click="navigateToFeature('maze')"
            style="
              cursor: pointer;
              transition: transform 0.3s ease, box-shadow 0.3s ease;
            "
            @mouseenter="handleMouseEnter($event.currentTarget)"
            @mouseleave="handleMouseLeave($event.currentTarget)"
          >
            <div class="card-content">
              <div class="icon-container puzzle">
                <Guide size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">思维迷宫</h3>
                <p class="feature-goal">逻辑谜题</p>
                <el-button type="primary" class="feature-button" size="large"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 首页组件
 * 展示四大功能矩阵，提供简洁美观的用户界面
 */
// 导入公共布局组件
import Layout from "@/components/Layout.vue";
// 导入所需图标
import { ChatDotRound, Edit, Plus, Guide } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

// 初始化路由
const router = useRouter();

// 卡片点击跳转函数
function navigateToFeature(feature) {
  // 根据不同功能跳转到不同页面
  switch (feature) {
    case "capture":
      router.push("/capture");
      break;
    case "writing":
      router.push("/writing");
      break;
    case "slow-fermentation":
      router.push("/slow-fermentation");
      break;
    case "maze":
      router.push("/maze");
      break;
    default:
      router.push("/");
  }
}

// 鼠标悬浮效果函数
function handleMouseEnter(el) {
  el.style.transform = "translateY(-5px)";
  el.style.boxShadow = "0 10px 25px rgba(0, 0, 0, 0.1)";
}

// 鼠标离开效果函数
function handleMouseLeave(el) {
  el.style.transform = "translateY(0)";
  el.style.boxShadow = "0 4px 10px rgba(0, 0, 0, 0.1)";
}
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
  gap: 2rem;
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
  transition: color 0.3s ease;
}

.dark .nav-item {
  color: #e0e0e0;
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

/* 功能卡片区域样式 */
.features-container {
  padding: 3rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

/* 确保卡片布局 */
.el-row {
  display: flex;
  justify-content: center;
}

.el-col {
  display: flex;
  justify-content: center;
}

.el-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: white;
  transition: background-color 0.3s ease;
}

.dark .el-card {
  background-color: #2a2a2a;
}
/* 卡片样式 */
.feature-card {
  height: 325px;
  width: 600px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

/* 列间距样式 */
.feature-col {
  margin-bottom: 30px;
  /* 下间距30px */
}

/* 卡片内容样式 */
.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 图标容器样式 */
.icon-container {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  color: white;
  overflow: hidden;
  border-radius: 8px;
}

/* 图标样式 */
.feature-icon {
  width: 100px;
  max-height: 60%;
}

/* 方块颜色 */
.icon-container.lightbulb {
  background-color: #ff9f1c;
  box-shadow: 0 4px 15px rgba(255, 159, 28, 0.3);
}

.icon-container.edit {
  background-color: #3a86ff;
  box-shadow: 0 4px 15px rgba(58, 134, 255, 0.3);
}

.icon-container.coffee {
  background-color: #9b5de5;
  box-shadow: 0 4px 15px rgba(155, 93, 229, 0.3);
}

.icon-container.puzzle {
  background-color: #f15bb5;
  box-shadow: 0 4px 15px rgba(241, 91, 181, 0.3);
}

/* 文本容器样式 */
.text-container {
  flex-grow: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.feature-title {
  font-size: 2.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  transition: color 0.3s ease;
  color: #2c3e50;
}
.dark .feature-title {
  color: #c7c0c0;
}

.feature-button {
  margin-top: auto;
}

/* 页脚样式 */
.footer {
  text-align: center;
  padding: 1.5rem;
  color: #666;
  background-color: white;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.dark-mode .footer {
  background-color: #2a2a2a;
  color: #b0b0b0;
}

/* 目标样式 */
.feature-goal {
  font-size: 1.5rem;
  font-weight: 500;
  color: #42b983;
  margin-bottom: 1rem;
}

/* 详情样式 */
.feature-details {
  margin-bottom: 1rem;
}

.feature-design,
.feature-scenario {
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: #666;
}

/* 页脚样式 */
.footer {
  text-align: center;
  padding: 2rem;
  background-color: #2c3e50;
  color: white;
  margin-top: auto;
}

/* 暗黑模式样式 */
.dark-mode {
  background-color: #1a1a1a;
  color: #e0e0e0;
}

.dark-mode .header {
  background-color: #252525;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.dark-mode .el-card {
  background-color: #252525;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.dark-mode .feature-goal {
  color: #63d471;
}

.dark-mode .el-button {
  background-color: #42b983;
  border-color: #42b983;
}

.dark-mode .footer {
  background-color: #1a1a1a;
  border-top: 1px solid #333;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .features-container {
    padding: 2rem 1rem;
  }
}
</style>
