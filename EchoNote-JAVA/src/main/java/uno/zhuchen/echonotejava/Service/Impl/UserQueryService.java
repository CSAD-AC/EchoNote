package uno.zhuchen.echonotejava.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import uno.zhuchen.echonotejava.Mapper.RolesMapper;
import uno.zhuchen.echonotejava.Mapper.UserMapper;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Project.User.User;

import java.util.Set;

@Service
public class UserQueryService {
    private final UserMapper userMapper;
    private final RolesMapper rolesMapper;

    public UserQueryService(UserMapper userMapper, RolesMapper rolesMapper) {
        this.userMapper = userMapper;
        this.rolesMapper = rolesMapper;
    }

    public User findUserAndRolesByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            Set<Role> roles = rolesMapper.findRolesByUserId(user.getId());
            user.setRoles(roles);
        }
        return user;
    }
}
