package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uno.zhuchen.echonotejava.Project.Capsules.Mood;

import java.util.List;

/**
 * 心情Mapper接口
 * 继承BaseMapper后可使用Mybatis-Plus提供的常用CRUD方法
 */
@Mapper
public interface MoodsMapper extends BaseMapper<Mood> {
    
    /**
     * 查询胶囊关联的所有心情
     */
    List<Mood> findAllMoodByCapsulesId(@Param("id") Integer id);
}
