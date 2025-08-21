<template>
  <Layout>
    <!-- 功能内容区域 -->
    <section class="feature-content">
      <div class="feature-header">
        <h2>思维迷宫</h2>
        <p>哲学闯关+隐喻生成+逻辑漏洞扫描，训练思维能力</p>
      </div>

      <div class="feature-details">
        <div class="feature-image-container">
          <div class="icon-container puzzle">
            <Guide size="64" class="feature-icon" />
          </div>
        </div>

        <div class="feature-description">
          <h3>功能介绍</h3>
          <p>
            思维迷宫是一个训练思维能力的互动系统，通过哲学闯关、隐喻生成和逻辑漏洞扫描等方式，帮助你提高思维的清晰度、逻辑性和创造性。
          </p>
          <h3>核心特性</h3>
          <ul>
            <li>哲学闯关游戏，挑战你的认知边界</li>
            <li>隐喻生成器，帮助你用新视角理解概念</li>
            <li>逻辑漏洞扫描，识别思维中的偏见和错误</li>
            <li>思维模式分析，了解你的思考习惯</li>
            <li>个性化思维训练计划，针对性提升思维能力</li>
          </ul>
        </div>
      </div>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 思维迷宫功能页面
 * 训练用户的思维能力
 */
// 导入所需图标
import { Guide } from "@element-plus/icons-vue";
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

.puzzle {
  background-color: #e0f7fa;
  box-shadow: 0 0 20px rgba(0, 188, 212, 0.3);
}

.feature-icon {
  color: #00bcd4;
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

.dark .puzzle {
  background-color: #0a3d62;
  box-shadow: 0 0 20px rgba(0, 188, 212, 0.5);
}

.dark .feature-icon {
  color: #00bcd4;
}
</style>
