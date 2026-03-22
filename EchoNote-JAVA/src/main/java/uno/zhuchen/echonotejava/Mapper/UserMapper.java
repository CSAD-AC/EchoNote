package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.User.User;

/**
 * 用户Mapper接口
 * 继承BaseMapper后可使用Mybatis-Plus提供的常用CRUD方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
