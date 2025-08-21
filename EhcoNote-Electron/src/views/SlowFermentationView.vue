<template>
  <Layout>
    <!-- 功能内容区域 -->
    <section class="feature-content">
      <div class="feature-header">
        <h2>思维慢发酵</h2>
        <p>24小时延迟回看+AI挑衅提问，促进认知迭代</p>
      </div>

      <div class="feature-details">
        <div class="feature-image-container">
          <div class="icon-container coffee">
            <ChatDotRound size="64" class="feature-icon" />
          </div>
        </div>

        <div class="feature-description">
          <h3>功能介绍</h3>
          <p>
            思维慢发酵帮助你深入思考和发展你的想法。通过24小时延迟回看机制，让你的思维有时间沉淀，再结合AI的挑衅式提问，激发你从不同角度重新审视自己的观点。
          </p>
          <h3>核心特性</h3>
          <ul>
            <li>24小时延迟回看，避免过早评判想法</li>
            <li>AI生成挑衅式提问，挑战你的思维局限</li>
            <li>想法发展历程追踪，记录思维演变过程</li>
            <li>多角度思考框架，帮助你全面分析问题</li>
            <li>定期思维回顾提醒，培养深度思考习惯</li>
          </ul>
        </div>
      </div>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 思维慢发酵功能页面
 * 帮助用户深入思考和发展想法
 */
// 导入所需图标
import { ChatDotRound } from "@element-plus/icons-vue";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
</script>

<style scoped>
/* 功能内容区域样式 */
.feature-content {
  padding: 3rem 2rem;
  max-width: 1000px;
  margin: 0 auto;
  flex: 1;
}

.feature-header {
  text-align: center;
  margin-bottom: 2rem;
}

.feature-header h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  color: #42b983;
}

.feature-details {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.feature-image-container {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.icon-container {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
}

.coffee {
  background-color: #ffebee;
  box-shadow: 0 0 20px rgba(244, 67, 54, 0.3);
}

.feature-icon {
  color: #f44336;
}

.feature-description {
  line-height: 1.6;
}

.feature-description h3 {
  color: #35495e;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
}

.feature-description ul {
  padding-left: 1.5rem;
  margin-bottom: 1.5rem;
}

.feature-description li {
  margin-bottom: 0.5rem;
}

/* 暗黑模式适配 */
.dark .feature-header h2 {
  color: #42b983;
}

.dark .feature-description h3 {
  color: #42b983;
}

.dark .coffee {
  background-color: #3d2121;
  box-shadow: 0 0 20px rgba(244, 67, 54, 0.5);
}

.dark .feature-icon {
  color: #ff5252;
}
</style>
