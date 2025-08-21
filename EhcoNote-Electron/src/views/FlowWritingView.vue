<template>
  <Layout>
    <!-- 功能内容区域 -->
    <section class="feature-content">
      <div class="feature-header">
        <h2>心流写作</h2>
        <p>沉浸式写作体验，动态聚焦光束+白噪音自适应</p>
      </div>

      <div class="feature-details">
        <div class="feature-image-container">
          <div class="icon-container edit">
            <Edit size="64" class="feature-icon" />
          </div>
        </div>

        <div class="feature-description">
          <h3>功能介绍</h3>
          <p>
            心流写作帮助你进入深度专注的写作状态，排除外界干扰，让思绪自由流动。无论是写文章、记日记还是创作故事，都能获得沉浸式的写作体验。
          </p>
          <h3>核心特性</h3>
          <ul>
            <li>动态聚焦光束，跟随光标移动，减少视觉干扰</li>
            <li>自适应白噪音，根据写作节奏调整音量和频率</li>
            <li>打字声音反馈，增强写作沉浸感</li>
            <li>专注模式，隐藏所有非必要界面元素</li>
            <li>自动保存，防止内容丢失</li>
          </ul>
        </div>
      </div>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 心流写作功能页面
 * 提供沉浸式写作体验
 */
// 导入所需图标
import { Edit } from "@element-plus/icons-vue";
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

.edit {
  background-color: #e3f2fd;
  box-shadow: 0 0 20px rgba(33, 150, 243, 0.3);
}

.feature-icon {
  color: #2196f3;
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

.dark .edit {
  background-color: #0a3d62;
  box-shadow: 0 0 20px rgba(33, 150, 243, 0.5);
}

.dark .feature-icon {
  color: #2196f3;
}
</style>
