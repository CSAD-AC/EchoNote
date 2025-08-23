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
