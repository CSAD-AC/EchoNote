package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uno.zhuchen.echonotejava.Project.User.Role;

import java.util.Set;

/**
 * 角色Mapper接口
 * 继承BaseMapper后可使用Mybatis-Plus提供的常用CRUD方法
 */
@Mapper
public interface RolesMapper extends BaseMapper<Role> {
    
    /**
     * 根据用户ID查询角色集合
     */
    Set<Role> findRolesByUserId(@Param("id") Integer id);
    
    /**
     * 为用户添加角色
     */
    void addRoleForUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
