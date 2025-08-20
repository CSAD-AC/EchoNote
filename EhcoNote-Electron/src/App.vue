<template>
  <router-view />
</template>

<style>
/* 重置默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}

/* App容器样式 */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
  width: 100%;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* 暗黑模式样式 */
.dark #app {
  background-color: #1a1a1a;
  color: #e0e0e0;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  transition: color 0.3s ease;
}

.dark nav a {
  color: #e0e0e0;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.dark nav a.router-link-exact-active {
  color: #63d471;
}
</style>
