package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;

/**
 * 灵光胶囊Mapper接口
 * 继承BaseMapper后可使用Mybatis-Plus提供的常用CRUD方法
 */
@Mapper
public interface CapsulesMapper extends BaseMapper<Capsules> {

    /**
     * 为胶囊添加心情关联
     */
    void addMoodForCapsules(@Param("capsulesId") Integer capsulesId, @Param("moodId") Integer moodId);
    
    /**
     * 删除胶囊的心情关联
     */
    void deleteMoodByCapsulesId(@Param("id") Integer id);
    
    /**
     * 删除胶囊的心情关联（根据胶囊对象）
     */
    void deleteMoodByCapsules(@Param("id") Integer id);
    
    /**
     * 批量添加胶囊心情关联
     */
    void addMoodForCapsule(Capsules capsules);
}
