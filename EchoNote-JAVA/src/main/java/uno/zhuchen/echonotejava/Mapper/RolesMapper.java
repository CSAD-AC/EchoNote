package uno.zhuchen.echonotejava.Mapper;

import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.User.Role;

import java.util.Set;

@Mapper
public interface RolesMapper {
    Set<Role> findRolesByUserId(Integer id);

    void addRoleForUser(Integer userId, Integer roleId);

}
