package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.Writing.Category;

/**
 * 分类Mapper接口
 * 继承BaseMapper后可使用Mybatis-Plus提供的常用CRUD方法
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
