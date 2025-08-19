package uno.zhuchen.echonotejava.Mapper;

import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.Capsules.Mood;

import java.util.List;
import java.util.Set;

@Mapper
public interface MoodsMapper {

    void addMood(Mood mood);  // 主键返回id
    void updateMood(Mood mood);
    void deleteMoodById(Integer id);
    List<Mood> findAllMoodByCapsulesId(Integer id);
    Mood findMoodById(Integer id);
    Set<Mood> getPreMoods();
}
