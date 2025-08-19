package uno.zhuchen.echonotejava.Mapper;


import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Project.User.User;

import java.util.Set;


@Mapper
public interface UserMapper {
    User findByUsername(String username);

    void addUser(User user);

    void updateUser(User user);  // 依据ID更新对象，需要提前将id设置好

    void changeEnabledUser(Integer id);
}
