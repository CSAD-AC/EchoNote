const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  publicPath: "./",
  transpileDependencies: true,
  lintOnSave: false,
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = "EchoNote"; // 这里设置你的应用标题
      return args;
    });
  },
  pluginOptions: {
    electronBuilder: {
      nodeIntegration: true,
      // 确保在生产环境中正确加载资源
      builderOptions: {
        appId: "com.echonote.app",
        productName: "EchoNote",
        directories: {
          output: "dist_electron",
        },
      },
    },
  },
});
