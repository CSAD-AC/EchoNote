<template>
  <Layout>
    <!-- 关于页面头部区域 -->
    <section class="about-hero">
      <div class="hero-content">
        <div class="brand-showcase">
          <div class="brand-logo">
            <div class="logo-container">
              <div class="logo-circle">
                <span class="logo-text">Echo</span>
                <span class="logo-sub">Note</span>
              </div>
              <div class="logo-waves">
                <div class="wave wave-1"></div>
                <div class="wave wave-2"></div>
                <div class="wave wave-3"></div>
              </div>
            </div>
          </div>
          <div class="brand-info">
            <h1 class="brand-title">
              <span class="title-main">关于 EchoNote</span>
              <span class="title-subtitle">思维回响，灵感永存</span>
            </h1>
            <div class="brand-description">
              <p class="desc-main">
                EchoNote 是一个专注于思维记录与表达的创新平台，
                让每一个灵感都能找到属于它的回响。
              </p>
              <div class="stats-grid">
                <div class="stat-item">
                  <div class="stat-number">4</div>
                  <div class="stat-label">核心功能</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">∞</div>
                  <div class="stat-label">无限可能</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">AI</div>
                  <div class="stat-label">智能助手</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 核心功能区域 -->
    <section class="about-features">
      <div class="features-container">
        <div class="features-header">
          <h2 class="features-title">四大核心功能</h2>
          <p class="features-subtitle">专为不同思维场景精心设计的创作工具</p>
        </div>

        <div class="feature-grid">
          <!-- 灵光胶囊 -->
          <div class="feature-card lightbulb-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container lightbulb">
                <Plus :size="32" class="feature-icon" />
              </div>
              <div class="feature-badge">瞬时捕捉</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">💡 灵光胶囊</h3>
              <p class="feature-goal">灵感捕手</p>
              <p class="feature-description">
                当灵感闪现的瞬间，用胶囊将它们安全封存。支持多媒体锚点，让每个想法都有迹可循。
              </p>
              <div class="feature-highlights">
                <span class="highlight">📸 截图锚点</span>
                <span class="highlight">🎵 音频记录</span>
                <span class="highlight">🏷️ 心情标签</span>
              </div>
            </div>
          </div>

          <!-- 心流写作 -->
          <div class="feature-card writing-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container edit">
                <Edit :size="32" class="feature-icon" />
              </div>
              <div class="feature-badge">沉浸创作</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">✍️ 心流写作</h3>
              <p class="feature-goal">专注模式</p>
              <p class="feature-description">
                进入心流状态的写作环境，动态聚焦光束引导注意力，白噪音自适应调节，让创作更纯粹。
              </p>
              <div class="feature-highlights">
                <span class="highlight">🔦 聚焦光束</span>
                <span class="highlight">🌊 白噪音</span>
                <span class="highlight">⏱️ 番茄钟</span>
              </div>
            </div>
          </div>

          <!-- 思维慢发酵 -->
          <div class="feature-card fermentation-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container coffee">
                <ChatDotRound :size="32" class="feature-icon" />
              </div>
              <div class="feature-badge">深度思考</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">☕ 思维慢发酵</h3>
              <p class="feature-goal">认知迭代</p>
              <p class="feature-description">
                24小时延迟回看机制，配合AI挑衅性提问，让思想在时间中发酵，促进认知的深度迭代。
              </p>
              <div class="feature-highlights">
                <span class="highlight">⏳ 延迟回看</span>
                <span class="highlight">🤖 AI提问</span>
                <span class="highlight">🔄 认知迭代</span>
              </div>
            </div>
          </div>

          <!-- 思维迷宫 -->
          <div class="feature-card maze-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container puzzle">
                <Guide :size="32" class="feature-icon" />
              </div>
              <div class="feature-badge">逻辑训练</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">🧩 思维迷宫</h3>
              <p class="feature-goal">思维锻炼</p>
              <p class="feature-description">
                通过哲学闯关、隐喻生成和逻辑漏洞扫描，系统性地训练和提升你的思维能力。
              </p>
              <div class="feature-highlights">
                <span class="highlight">🏛️ 哲学闯关</span>
                <span class="highlight">🎭 隐喻生成</span>
                <span class="highlight">🔍 逻辑扫描</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 设计理念区域 -->
    <section class="about-philosophy">
      <div class="philosophy-container">
        <div class="philosophy-content">
          <h2 class="philosophy-title">设计理念</h2>
          <div class="philosophy-text">
            <div class="philosophy-item">
              <div class="philosophy-icon">🎯</div>
              <div class="philosophy-desc">
                <h3>专注核心价值</h3>
                <p>
                  我们相信每个人的思维都有独特价值，好的工具能够放大这种价值，让思想更有力量。
                </p>
              </div>
            </div>
            <div class="philosophy-item">
              <div class="philosophy-icon">🧬</div>
              <div class="philosophy-desc">
                <h3>科学理论支撑</h3>
                <p>
                  结合心理学、认知科学和人工智能，创造符合人类思维习惯又能促进思维发展的数字空间。
                </p>
              </div>
            </div>
            <div class="philosophy-item">
              <div class="philosophy-icon">🌱</div>
              <div class="philosophy-desc">
                <h3>思维成长伴侣</h3>
                <p>
                  不仅是记录工具，更是思维成长的伴侣，陪伴你在思考的道路上不断前行和突破。
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 团队信息区域 -->
    <section class="about-team">
      <div class="team-container">
        <h2 class="team-title">开发团队</h2>
        <div class="team-info">
          <div class="team-card">
            <div class="team-avatar">🚀</div>
            <h3>EchoNote Team</h3>
            <p>一群热爱思考、专注创新的开发者</p>
            <p class="team-mission">致力于打造最好用的思维工具</p>
          </div>
        </div>

        <div class="version-info">
          <p class="version-text">
            <span class="version-label">当前版本：</span>
            <span class="version-number">v1.0.0</span>
          </p>
          <p class="copyright">© 2025 EchoNote. 保留所有权利。</p>
        </div>
      </div>
    </section>
  </Layout>
</template>

<script setup>
// 导入组件
import Layout from "../components/Layout.vue";
import { Plus, Edit, ChatDotRound, Guide } from "@element-plus/icons-vue";
</script>

<style scoped>
/* 关于页面整体布局 */

/* 头部英雄区域 */
.about-hero {
  position: relative;
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.05) 0%,
    rgba(102, 126, 234, 0.05) 30%,
    rgba(168, 85, 247, 0.05) 60%,
    rgba(245, 101, 101, 0.05) 100%
  );
  overflow: hidden;
}

.hero-content {
  max-width: 1200px;
  width: 100%;
  padding: 0 2rem;
}

.brand-showcase {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4rem;
  flex-direction: column;
  text-align: center;
}

/* 品牌Logo区域 */
.brand-logo {
  position: relative;
  margin-bottom: 2rem;
}

.logo-container {
  position: relative;
  display: inline-block;
}

.logo-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 30px rgba(66, 185, 131, 0.3),
    0 0 0 4px rgba(255, 255, 255, 0.8), 0 0 0 8px rgba(66, 185, 131, 0.1);
  position: relative;
  z-index: 2;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  line-height: 1;
  margin-bottom: 2px;
}

.logo-sub {
  font-size: 0.9rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1;
}

.logo-waves {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
}

.wave {
  position: absolute;
  border: 2px solid;
  border-radius: 50%;
  opacity: 0;
  animation: wave-pulse 3s infinite;
}

.wave-1 {
  width: 140px;
  height: 140px;
  border-color: rgba(66, 185, 131, 0.4);
  animation-delay: 0s;
}

.wave-2 {
  width: 160px;
  height: 160px;
  border-color: rgba(102, 126, 234, 0.3);
  animation-delay: 1s;
}

.wave-3 {
  width: 180px;
  height: 180px;
  border-color: rgba(168, 85, 247, 0.2);
  animation-delay: 2s;
}

@keyframes wave-pulse {
  0% {
    transform: translate(-50%, -50%) scale(0.8);
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.2);
    opacity: 0;
  }
}

/* 品牌信息区域 */
.brand-info {
  max-width: 800px;
}

.brand-title {
  margin: 0 0 2rem 0;
  line-height: 1.2;
}

.title-main {
  display: block;
  font-size: 3rem;
  font-weight: 800;
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  margin-bottom: 0.5rem;
}

.title-subtitle {
  display: block;
  font-size: 1.1rem;
  font-weight: 400;
  color: #888;
  letter-spacing: 2px;
  opacity: 0.8;
}

.brand-description {
  margin-top: 2rem;
}

.desc-main {
  font-size: 1.2rem;
  line-height: 1.8;
  color: #555;
  margin: 0 0 3rem 0;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  max-width: 400px;
  margin: 0 auto;
}

.stat-item {
  text-align: center;
  padding: 1.5rem 1rem;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.9);
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  line-height: 1;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 0.9rem;
  color: #666;
  font-weight: 500;
}

/* 功能区域 */
.about-features {
  padding: 4rem 2rem;
  background: rgba(255, 255, 255, 0.5);
}

.features-container {
  max-width: 1400px;
  margin: 0 auto;
}

.features-header {
  text-align: center;
  margin-bottom: 4rem;
}

.features-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  background: linear-gradient(135deg, #42b983, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.features-subtitle {
  font-size: 1.1rem;
  color: #666;
  margin: 0;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  margin-top: 3rem;
}

/* 功能卡片 */
.feature-card {
  position: relative;
  height: 380px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 0;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
}

.card-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 24px;
  z-index: 1;
}

.lightbulb-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(255, 159, 28, 0.1) 0%,
    rgba(255, 193, 7, 0.1) 100%
  );
}

.writing-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(58, 134, 255, 0.1) 0%,
    rgba(102, 126, 234, 0.1) 100%
  );
}

.fermentation-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(155, 93, 229, 0.1) 0%,
    rgba(168, 85, 247, 0.1) 100%
  );
}

.maze-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(241, 91, 181, 0.1) 0%,
    rgba(245, 101, 101, 0.1) 100%
  );
}

.feature-card:hover .card-background {
  opacity: 1;
}

.card-header {
  position: relative;
  z-index: 2;
  padding: 2rem 2rem 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.icon-container {
  width: 64px;
  height: 64px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: transform 0.3s ease;
}

.icon-container.lightbulb {
  background: linear-gradient(135deg, #ff9f1c, #ffc107);
}

.icon-container.edit {
  background: linear-gradient(135deg, #3a86ff, #667eea);
}

.icon-container.coffee {
  background: linear-gradient(135deg, #9b5de5, #a855f7);
}

.icon-container.puzzle {
  background: linear-gradient(135deg, #f15bb5, #f56565);
}

.feature-card:hover .icon-container {
  transform: scale(1.05);
}

.feature-badge {
  background: linear-gradient(135deg, #42b983, #369870);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.card-content {
  position: relative;
  z-index: 2;
  padding: 1rem 2rem 2rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.feature-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.feature-goal {
  font-size: 1rem;
  font-weight: 600;
  color: #42b983;
  margin: 0 0 1rem 0;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.feature-description {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #555;
  margin: 0 0 1.5rem 0;
  flex: 1;
}

.feature-highlights {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.highlight {
  background: rgba(66, 185, 131, 0.1);
  color: #42b983;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 500;
  border: 1px solid rgba(66, 185, 131, 0.2);
}

/* 设计理念区域 */
.about-philosophy {
  padding: 4rem 2rem;
  background: linear-gradient(
    135deg,
    rgba(102, 126, 234, 0.05) 0%,
    rgba(168, 85, 247, 0.05) 100%
  );
}

.philosophy-container {
  max-width: 1200px;
  margin: 0 auto;
}

.philosophy-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 3rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.philosophy-text {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.philosophy-item {
  display: flex;
  align-items: flex-start;
  gap: 1.5rem;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.3s ease;
}

.philosophy-item:hover {
  transform: translateY(-5px);
}

.philosophy-icon {
  font-size: 2rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  flex-shrink: 0;
}

.philosophy-desc h3 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.2rem;
}

.philosophy-desc p {
  margin: 0;
  color: #555;
  line-height: 1.6;
}

/* 团队信息区域 */
.about-team {
  padding: 4rem 2rem;
  text-align: center;
  background: rgba(255, 255, 255, 0.3);
}

.team-container {
  max-width: 800px;
  margin: 0 auto;
}

.team-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 3rem;
  background: linear-gradient(135deg, #42b983, #667eea);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.team-info {
  margin-bottom: 3rem;
}

.team-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(15px);
  border-radius: 20px;
  padding: 3rem 2rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: transform 0.3s ease;
}

.team-card:hover {
  transform: translateY(-5px);
}

.team-avatar {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.team-card h3 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
  font-size: 1.5rem;
}

.team-card p {
  margin: 0 0 0.5rem 0;
  color: #555;
  line-height: 1.6;
}

.team-mission {
  color: #42b983 !important;
  font-weight: 600;
  font-style: italic;
}

.version-info {
  padding-top: 2rem;
  border-top: 1px solid rgba(66, 185, 131, 0.2);
}

.version-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.version-label {
  color: #666;
}

.version-number {
  background: linear-gradient(135deg, #42b983, #369870);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.9rem;
}

.copyright {
  color: #888;
  font-size: 0.9rem;
  margin: 0;
}

/* 暗黑模式 */
.dark .about-hero {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.08) 0%,
    rgba(168, 85, 247, 0.08) 30%,
    rgba(245, 101, 101, 0.08) 60%,
    rgba(58, 134, 255, 0.08) 100%
  );
}

.dark .logo-circle {
  background: linear-gradient(135deg, #4cd964 0%, #a855f7 100%);
  box-shadow: 0 10px 30px rgba(76, 217, 100, 0.4), 0 0 0 4px rgba(0, 0, 0, 0.5),
    0 0 0 8px rgba(76, 217, 100, 0.1);
}

.dark .wave-1 {
  border-color: rgba(76, 217, 100, 0.3);
}

.dark .wave-2 {
  border-color: rgba(168, 85, 247, 0.25);
}

.dark .wave-3 {
  border-color: rgba(245, 101, 101, 0.2);
}

.dark .title-main {
  background: linear-gradient(135deg, #4cd964 0%, #a855f7 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .title-subtitle {
  color: #9ca3af;
}

.dark .desc-main {
  color: #d1d5db;
}

.dark .stat-item {
  background: rgba(31, 41, 55, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .stat-item:hover {
  background: rgba(31, 41, 55, 0.9);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

.dark .stat-number {
  background: linear-gradient(135deg, #4cd964 0%, #a855f7 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .stat-label {
  color: #9ca3af;
}

.dark .title-line {
  color: #9ca3af;
}

.dark .title-subtitle {
  color: #6b7280;
}

.dark .hero-description {
  color: #d1d5db;
}

.dark .about-features {
  background: rgba(31, 41, 55, 0.3);
}

.dark .features-subtitle {
  color: #9ca3af;
}

.dark .feature-card {
  background: rgba(31, 41, 55, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .feature-card:hover {
  border-color: rgba(255, 255, 255, 0.2);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
}

.dark .feature-title {
  color: #f3f4f6;
}

.dark .feature-description {
  color: #d1d5db;
}

.dark .highlight {
  background: rgba(76, 217, 100, 0.1);
  color: #4cd964;
  border: 1px solid rgba(76, 217, 100, 0.2);
}

.dark .about-philosophy {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.05) 0%,
    rgba(168, 85, 247, 0.05) 100%
  );
}

.dark .philosophy-item {
  background: rgba(31, 41, 55, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .philosophy-desc h3 {
  color: #f3f4f6;
}

.dark .philosophy-desc p {
  color: #d1d5db;
}

.dark .about-team {
  background: rgba(31, 41, 55, 0.2);
}

.dark .team-card {
  background: rgba(31, 41, 55, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .team-card h3 {
  color: #f3f4f6;
}

.dark .team-card p {
  color: #d1d5db;
}

.dark .team-mission {
  color: #4cd964 !important;
}

.dark .version-label {
  color: #9ca3af;
}

.dark .copyright {
  color: #6b7280;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .brand-showcase {
    gap: 3rem;
  }

  .stats-grid {
    gap: 1.5rem;
    max-width: 350px;
  }

  .feature-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }

  .philosophy-text {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .about-hero {
    min-height: 50vh;
    padding: 2rem 1rem;
  }

  .hero-content {
    padding: 0 1rem;
  }

  .brand-showcase {
    gap: 2rem;
  }

  .logo-circle {
    width: 100px;
    height: 100px;
  }

  .logo-text {
    font-size: 1.3rem;
  }

  .logo-sub {
    font-size: 0.8rem;
  }

  .wave-1 {
    width: 120px;
    height: 120px;
  }

  .wave-2 {
    width: 140px;
    height: 140px;
  }

  .wave-3 {
    width: 160px;
    height: 160px;
  }

  .title-main {
    font-size: 2.5rem;
  }

  .desc-main {
    font-size: 1.1rem;
  }

  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 1rem;
    max-width: 300px;
  }

  .stat-item {
    padding: 1rem 0.5rem;
  }

  .stat-number {
    font-size: 1.5rem;
  }

  .stat-label {
    font-size: 0.8rem;
  }

  .about-features,
  .about-philosophy,
  .about-team {
    padding: 3rem 1rem;
  }

  .features-title,
  .philosophy-title,
  .team-title {
    font-size: 2rem;
  }

  .feature-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .feature-card {
    height: 350px;
  }

  .philosophy-item {
    flex-direction: column;
    text-align: center;
  }

  .philosophy-icon {
    font-size: 3rem;
    margin-bottom: 1rem;
  }
}

@media (max-width: 480px) {
  .logo-circle {
    width: 80px;
    height: 80px;
  }

  .logo-text {
    font-size: 1.1rem;
  }

  .logo-sub {
    font-size: 0.7rem;
  }

  .wave-1 {
    width: 100px;
    height: 100px;
  }

  .wave-2 {
    width: 120px;
    height: 120px;
  }

  .wave-3 {
    width: 140px;
    height: 140px;
  }

  .title-main {
    font-size: 2rem;
  }

  .title-subtitle {
    font-size: 1rem;
    letter-spacing: 1px;
  }

  .desc-main {
    font-size: 1rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
    max-width: 200px;
  }

  .stat-item {
    padding: 1rem;
  }

  .features-title,
  .philosophy-title,
  .team-title {
    font-size: 1.5rem;
  }

  .feature-card {
    height: 320px;
  }

  .card-header,
  .card-content {
    padding-left: 1.5rem;
    padding-right: 1.5rem;
  }
}
</style>
