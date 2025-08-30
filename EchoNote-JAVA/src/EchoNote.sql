CREATE DATABASE IF NOT EXISTS echonote;
USE echonote;


-- 用户认证模块
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    userName  VARCHAR(20) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(80) NOT NULL COMMENT 'BCrypt加密密码(60位)',
    email VARCHAR(50) NOT NULL COMMENT '邮箱',
    name VARCHAR(20)  COMMENT '用户昵称',
    image VARCHAR(200) COMMENT '用户头像',
    enabled BOOLEAN NOT NULL DEFAULT TRUE COMMENT '用户是否可用',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';

INSERT INTO user (id, userName, password, name,email, enabled, updateTime)
VALUES
    (1, '3029489598', '$2a$10$8Jzu4r3XO1IezN8aMJ.ZGeI0QPg3DY/ed9KNKnU9djkmjtDLcY9Iy', '逐辰','3029489598@qq.com', true, '2025-06-22 11:02:35'),
    (2, '123456', '$2a$10$HJusN5zCTdWrz3kq5sz4ROSlyi7WVZJyerwv7./3yNQmUviov7Dlm', '测试', '123456@qq.com', true, '2025-06-22 11:47:20'),
    (3, 'ADMIN', '$2a$10$8Jzu4r3XO1IezN8aMJ.ZGeI0QPg3DY/ed9KNKnU9djkmjtDLcY9Iy', '管理员', 'admin@qq.com', true, '2025-06-21 13:30:25');


CREATE TABLE IF NOT EXISTS roles (
                                     id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
                                     name VARCHAR(20) NOT NULL COMMENT '角色名称'
    );

INSERT INTO roles (name) VALUES
                             ('ROLE_USER'),
                             ('ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS user_roles (
                                          userId INT NOT NULL,
                                          roleId INT NOT NULL,
                                          FOREIGN KEY (userId) REFERENCES user(id),
    FOREIGN KEY (roleId) REFERENCES roles(id)
    );

INSERT INTO user_roles (userId, roleId) VALUES
                                            (1, 1),
                                            (2, 1),
                                            (3, 1),
                                            (3, 2);


-- 灵光胶囊模块
CREATE TABLE IF NOT EXISTS capsules (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '胶囊ID（自增主键）',
                                        userId BIGINT NOT NULL COMMENT '用户ID（外键）',
                                        title VARCHAR(20) NOT NULL COMMENT '胶囊标题',
    location VARCHAR(50) NOT NULL COMMENT '位置',
    description VARCHAR(400) COMMENT '事件描述（可空）',
    createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status BOOLEAN DEFAULT 0 COMMENT '状态：0-未处理 1-已处理'
    ) COMMENT '灵光胶囊主表';

INSERT INTO capsules (userId, title, location, description,createTime ,updateTime) VALUES
                                                                                       (2, '上升贬低价值探讨', '饭桌', '饭桌上，父亲要求分完没吃完的饭，并说不能浪费食物，这么一点饭怎么也得把它吃完。彼时我的想法是，我确实不想吃了，这种价值的抬高是否是对我的凌驾呢？互联网上也会有提高价值亦或是贬低价值的行为，我该怎么思考这种价值标准的合理性呢？', '2025-08-24 21:02:35', '2025-08-24 21:02:35'),
                                                                                       (2, '言语的场合、分寸性', '抖音评论区', '在抖音评论区上看到了一个养猫人说自己的猫猫"回咪星找麻麻了"，底下有人评论"没有咪星哦 死了就是死了 不是去找妈妈了 就是死了 嘎巴一下就死那了"', '2025-08-29 20:51:35', '2025-08-29 21:51:35');
-- 心情表 (moods)
CREATE TABLE moods (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT '心情表';

INSERT INTO moods  (name) VALUES
-- 1. **基础情绪**（核心情感）
('开心'), ('愤怒'), ('悲伤'), ('恐惧'), ('厌恶'), ('惊讶'),
-- 2. **复合情绪**（常见心理状态）
('焦虑'), ('愧疚'), ('委屈'), ('感动'), ('孤独'), ('迷茫'),
('压抑'), ('兴奋'), ('平静'), ('厌倦'), ('期待'), ('释然'),
-- 3. **行为意愿**（动作倾向）
('想哭'), ('想睡'), ('想吃'), ('想独处'), ('想倾诉'), ('想旅行'),
('想学习'), ('想工作'), ('想玩耍'), ('想放弃'), ('想改变'), ('我很混乱'),
-- 4. **内在状态**（深层心理）
('自卑'), ('自信'), ('空虚'), ('充实'), ('纠结'), ('通透'),
('无力感'), ('掌控感'), ('疏离感'), ('归属感'),
('思考'),('事实如此，却不该如此吗');

-- 胶囊-心情关联表 (capsules_moods)
CREATE TABLE IF NOT EXISTS capsules_moods (
                                              id INT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
                                              capsuleId INT NOT NULL COMMENT '胶囊ID',
                                              moodId INT NOT NULL COMMENT '心情ID'
) COMMENT '胶囊-心情关联表';

INSERT INTO capsules_moods (capsuleId, moodId) VALUES
                                                   (1, 15), (1, 20), (1, 41),
                                                   (2, 12),(2, 42);

-- 心流写作模块
CREATE TABLE category (
                          id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                          userId INT UNSIGNED NOT NULL,
                          name VARCHAR(50) NOT NULL,
                          status BOOLEAN DEFAULT 1 comment '状态：0-回收站 1-可用',
                          createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

INSERT INTO category (userId, name) VALUES
    (2, '随笔');

CREATE TABLE text (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文本ID',
                      categoryId INT UNSIGNED NOT NULL COMMENT '分类ID',
                      currentVersion INT UNSIGNED NOT NULL COMMENT '当前版本号',
                      status BOOLEAN DEFAULT 1 COMMENT '状态：0-回收站 1-可用',
                      createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_text_category
                          FOREIGN KEY (categoryId)
                              REFERENCES category(id)
) COMMENT='文章表';

CREATE TABLE backup_text (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文本ID',
                             textId BIGINT NOT NULL COMMENT '关联的文本ID',
                             version INT UNSIGNED NOT NULL COMMENT '版本号',
                             title VARCHAR(50) NOT NULL COMMENT '标题',
                             content MEDIUMTEXT NOT NULL COMMENT '内容',
                             createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO text (categoryId, currentVersion) VALUES
    (1, 2);

INSERT INTO backup_text (textId,version,  title, content) VALUES
                                                              (1, 1, '爱情杂谈', '爱情的定义于我，始终是流动的追问而非凝固的答案，每一次思考都在修正认知的坐标逐渐勾勒出一个大体认识。
　　首先第一个问题就是，爱情对我来说意味着什么？在没有遇到爱情的当下，我似乎并不缺少爱情的慰籍，情绪价值的支撑、分享欲的安放、存在感的确认，我都能在现有的关系网络中得到，似乎已然达到自洽的局面。但我仍难掩内心深处对爱情的悸动。这是为什么呢？我想这恰恰说明了爱情拥有不可替代的独特价值，那么这种价值是什么？仔细思考爱情的深度和意义，我觉得爱情的唯一性、这种精神上的深度联结，双方共同规划未来，将彼此规划到自己的未来里携手共进的这种独特价值可能才是爱情不可替代的原因，也是爱情能够打动人们的关键。所以我渴望的爱情是什么样的呢？我们是彼此最亲密的朋友，无需伪装地笑泪，共情最幽微的思绪，能接住彼此的幼稚，能倾诉彼此的心事；我们是彼此最坚定的战友，能够在面对各自课题时相互鼓励，彼此依靠，能够在共同面对挑战时携手共进，各展其能；我们是彼此最信任的亲人，有着相濡以沫，细水长流的温柔，有着“与君同舟渡，达岸各自归”后依然相视一笑的笃定。我们深度联结，而又各自独立。我们先是完整独立的自我，才是一个彼此爱慕的伴侣。我想这就是爱情对我的意味，这就是我理解中理想爱情的模样。'),
                                                              (1, 2, '爱情', '爱情的定义于我，始终是流动的追问而非凝固的答案，每一次思考都在修正认知的坐标逐渐勾勒出一个大体认识。
　　首先第一个问题就是，爱情对我来说意味着什么？在没有遇到爱情的当下，我似乎并不缺少爱情的慰籍，情绪价值的支撑、分享欲的安放、存在感的确认，我都能在现有的关系网络中得到，似乎已然达到自洽的局面。但我仍难掩内心深处对爱情的悸动。这是为什么呢？我想这恰恰说明了爱情拥有不可替代的独特价值，那么这种价值是什么？仔细思考爱情的深度和意义，我觉得爱情的唯一性、这种精神上的深度联结，双方共同规划未来，将彼此规划到自己的未来里携手共进的这种独特价值可能才是爱情不可替代的原因，也是爱情能够打动人们的关键。所以我渴望的爱情是什么样的呢？我们是彼此最亲密的朋友，无需伪装地笑泪，共情最幽微的思绪，能接住彼此的幼稚，能倾诉彼此的心事；我们是彼此最坚定的战友，能够在面对各自课题时相互鼓励，彼此依靠，能够在共同面对挑战时携手共进，各展其能；我们是彼此最信任的亲人，有着相濡以沫，细水长流的温柔，有着“与君同舟渡，达岸各自归”后依然相视一笑的笃定。我们深度联结，而又各自独立。我们先是完整独立的自我，才是一个彼此爱慕的伴侣。我想这就是爱情对我的意味，这就是我理解中理想爱情的模样。');


