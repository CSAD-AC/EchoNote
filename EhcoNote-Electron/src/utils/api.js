import request from "./request";

/**
 * API接口集合
 * 封装项目中所有的API请求
 */

/**
 * 示例: 登录
 * @param {object} data - 登录数据
 * @param {string} data.userName - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise}
 */
export const login = (data) => {
  return request.post("/login", data);
};

/**
 * 注册
 * @param {object} data - 注册数据
 * @param {string} data.userName - 用户名
 * @param {string} data.password - 密码
 * @param {string} data.email - 电子邮箱
 * @returns {Promise}
 */
export const register = (data) => {
  return request.post("/register", data);
};

/**
 * 查询指定id的胶囊信息
 * @param {number} id - 胶囊ID
 * @returns {Promise} - 返回胶囊详细信息
 */
export const getCapsuleById = (id) => {
  return request.get("/capsule", { id });
};

/**
 * 查询指定用户id对应的所有胶囊信息
 * @returns {Promise} - 返回胶囊列表
 */
export const getCapsulesByUserId = () => {
  return request.get("/capsules");
};

/**
 * 查询预设心情列表
 * @returns {Promise} - 返回预设心情列表
 */
export const getPreMoods = () => {
  return request.get("/capsules/mood");
};
/**
 * 改变胶囊状态
 * @param {Object} data - 胶囊数据
 * @param {number} data.id - 胶囊ID
 * @returns {Promise} - 返回响应数据
 */
export const changeCapsulesStatusById = (data) => {
  return request.put("/capsules/status", data);
};

/**
 * 创建新胶囊
 * @param {Object} data - 胶囊数据
 * @param {number} data.userId - 用户ID
 * @param {string} data.title - 标题
 * @param {string} data.location - 地点
 * @param {string} data.description - 描述
 * @param {boolean} data.status - 状态
 * @param {Array} data.moods - 心情列表
 * @param {number} data.moods[].id - 心情ID
 * @param {string} data.moods[].name - 心情名称
 * @param {Object} data.newMood - 新心情（可选）
 * @param {string} data.newMood.name - 新心情名称
 * @returns {Promise} - 返回创建的胶囊信息
 */
export const createCapsule = (data) => {
  return request.post("/capsules", data);
};

/**
 * 更新胶囊信息
 * @param {Object} data - 胶囊数据
 * @param {number} data.id - 胶囊ID
 * @param {string} data.title - 标题
 * @param {string} data.location - 地点
 * @param {string} data.description - 描述
 * @param {Object} data.newMood - 新心情（可选）
 * @param {string} data.newMood.name - 新心情名称
 * @param {Array} data.moods - 心情列表
 * @param {number} data.moods[].id - 心情ID
 * @param {string} data.moods[].name - 心情名称
 * @returns {Promise} - 返回更新后的胶囊信息
 */
export const updateCapsule = (data) => {
  return request.put("/capsules", data);
};

/**
 * 删除指定id的胶囊
 * @param {number} id - 胶囊ID
 * @returns {Promise} - 返回删除结果
 */
export const deleteCapsuleById = (id) => {
  return request.delete("/capsule", { id });
};

/**
 * 获取全部分类
 * @returns {Promise} - 返回分类列表
 */
export const getCategories = () => {
  return request.get("/writing/category");
};

/**
 * 获取全部文章
 * @returns {Promise} - 返回文章列表
 */
export const getTexts = () => {
  return request.get("/writing/text");
};

/**
 * 获取文章历史修改记录
 * @param {number} textId - 文章ID
 * @returns {Promise} - 返回历史记录列表
 */
export const getTextHistory = (textId) => {
  return request.get("/writing/text/history", { textId });
};

/**
 * 文章版本回退
 * @param {Object} data - 回退数据
 * @param {number} data.textId - 文章ID
 * @param {number} data.version - 目标版本号
 * @returns {Promise} - 返回回退结果
 */
export const resetTextVersion = (data) => {
  return request.put("/writing/text/reset", data);
};

/**
 * 添加分类
 * @param {Object} data - 分类数据
 * @param {number} data.userId - 用户ID
 * @param {string} data.name - 分类名称
 * @returns {Promise} - 返回添加结果
 */
export const addCategory = (data) => {
  return request.post("/writing/category", data);
};

/**
 * 新增文章
 * @param {Object} data - 文章数据
 * @param {number} data.categoryId - 分类ID
 * @returns {Promise} - 返回添加结果
 */
export const addText = (data) => {
  return request.post("/writing/text", data);
};

/**
 * 删除分类
 * @param {number} id - 分类ID
 * @returns {Promise} - 返回删除结果
 */
export const deleteCategory = (id) => {
  return request.delete("/writing/category", { id });
};

/**
 * 删除文章
 * @param {number} id - 文章ID
 * @returns {Promise} - 返回删除结果
 */
export const deleteText = (id) => {
  return request.delete("/writing/text", { id });
};

/**
 * 重命名分类
 * @param {Object} data - 分类数据
 * @param {number} data.id - 分类ID
 * @param {string} data.name - 新分类名称
 * @returns {Promise} - 返回修改结果
 */
export const renameCategory = (data) => {
  return request.put("/writing/category", data);
};

/**
 * 更新文章
 * @param {Object} data - 文章数据
 * @param {number} data.textId - 文章ID
 * @param {string} data.title - 文章标题
 * @param {string} data.content - 文章内容
 * @returns {Promise} - 返回更新结果
 */
export const updateText = (data) => {
  return request.put("/writing/text", data);
};
