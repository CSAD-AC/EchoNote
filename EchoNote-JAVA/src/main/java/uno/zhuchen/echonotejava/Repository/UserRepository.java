package uno.zhuchen.echonotejava.Repository;

import uno.zhuchen.echonotejava.Project.User.User;

public interface UserRepository {
    User findUserAndRolesByUsername(String username);

    void addUser(User user);
    void updateUser(User user); // 依据ID更新对象，需要提前将id设置好
    void changeEnabledUser(Integer id); // 改变用户状态(禁用/可用)


}
