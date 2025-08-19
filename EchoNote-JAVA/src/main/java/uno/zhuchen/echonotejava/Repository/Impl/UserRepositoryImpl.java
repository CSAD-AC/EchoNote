package uno.zhuchen.echonotejava.Repository.Impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uno.zhuchen.echonotejava.Mapper.RolesMapper;
import uno.zhuchen.echonotejava.Mapper.UserMapper;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Project.User.User;
import uno.zhuchen.echonotejava.Repository.UserRepository;

import java.util.Set;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private final RolesMapper rolesMapper;
    public UserRepositoryImpl(UserMapper userMapper, RolesMapper rolesMapper) {
        this.userMapper = userMapper;
        this.rolesMapper = rolesMapper;
    }
    @Override
    public User findUserAndRolesByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if(user != null) {
            Set<Role> roles = rolesMapper.findRolesByUserId(user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
        rolesMapper.addRoleForUser(user.getId(), 1);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public void changeEnabledUser(Integer id) {
        userMapper.changeEnabledUser(id);
    }
}
