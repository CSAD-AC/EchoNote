<template>
  <Layout>
    <!-- 关于内容区域 -->
    <section class="about-content">
      <div class="about-header">
        <h2>关于 EchoNote</h2>
        <p>EchoNote 是一款专注于思维记录与表达的创新应用</p>
      </div>

      <div class="about-features">
        <h3>核心功能</h3>
        <div class="feature-list">
          <!-- 灵光胶囊 -->
          <div class="about-feature-item">
            <div class="icon-container lightbulb">
              <Plus size="32" class="feature-icon" />
            </div>
            <div class="feature-text">
              <h4>灵光胶囊</h4>
              <p>瞬时捕捉闪现的灵感，支持多媒体锚点（截图/音频）</p>
            </div>
          </div>

          <!-- 心流写作 -->
          <div class="about-feature-item">
            <div class="icon-container edit">
              <Edit size="32" class="feature-icon" />
            </div>
            <div class="feature-text">
              <h4>心流写作</h4>
              <p>沉浸式写作体验，动态聚焦光束+白噪音自适应</p>
            </div>
          </div>

          <!-- 思维慢发酵 -->
          <div class="about-feature-item">
            <div class="icon-container coffee">
              <ChatDotRound size="32" class="feature-icon" />
            </div>
            <div class="feature-text">
              <h4>思维慢发酵</h4>
              <p>24小时延迟回看+AI挑衅提问，促进认知迭代</p>
            </div>
          </div>

          <!-- 思维迷宫 -->
          <div class="about-feature-item">
            <div class="icon-container puzzle">
              <Guide size="32" class="feature-icon" />
            </div>
            <div class="feature-text">
              <h4>思维迷宫</h4>
              <p>哲学闯关+隐喻生成+逻辑漏洞扫描，训练思维能力</p>
            </div>
          </div>
        </div>
      </div>

      <div class="about-story">
        <h3>设计理念</h3>
        <p>
          EchoNote
          旨在帮助用户更好地捕捉、发展和表达自己的思想。我们相信，每个人的思维都有独特的价值，而好的工具能够放大这种价值。
        </p>
        <p>
          通过结合心理学、认知科学和人工智能，EchoNote
          创造了一个既符合人类思维习惯又能促进思维发展的数字空间。
        </p>
      </div>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 关于页面组件
 * 展示应用的功能和设计理念
 */
// 导入所需图标
import { Plus, Edit, ChatDotRound, Guide } from "@element-plus/icons-vue";
// 导入公共布局组件
import Layout from "../components/Layout.vue";
</script>

<style scoped>
/* 基础样式 */
/* 关于内容区域样式 */
.about-content {
  padding: 3rem 2rem;
  max-width: 1000px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.about-header {
  text-align: center;
  margin-bottom: 3rem;
}

.about-header h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.about-features {
  margin-bottom: 3rem;
}

.feature-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.about-feature-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.about-feature-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

/* 图标容器样式 */
.icon-container {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
  overflow: hidden;
}

/* 图标样式 */
.feature-icon {
  max-width: 80%;
  max-height: 80%;
}

/* 方块颜色 */
.icon-container.lightbulb {
  background-color: #ff9f1c;
}

.icon-container.edit {
  background-color: #3a86ff;
}

.icon-container.coffee {
  background-color: #9b5de5;
}

.icon-container.puzzle {
  background-color: #f15bb5;
}

.feature-text h4 {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
}

.feature-text p {
  margin: 0;
  color: #666;
}

.about-story p {
  line-height: 1.6;
  margin-bottom: 1rem;
}

/* 暗黑模式样式覆盖 */
.dark .about-feature-item {
  background-color: #252525;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.dark .feature-text p {
  color: #b0b0b0;
}
</style>
