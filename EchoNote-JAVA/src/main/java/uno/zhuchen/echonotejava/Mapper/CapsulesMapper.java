package uno.zhuchen.echonotejava.Mapper;

import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;

import java.util.List;

@Mapper
public interface CapsulesMapper {
    void addCapsules(Capsules capsules);
    void updateCapsulesById(Capsules capsules);
    void deleteMoodByCapsulesId(Integer id);
    void deleteCapsulesById(Integer id);
    List<Capsules> findCapsulesByUserId(Integer id);
    Capsules findCapsulesById(Integer id);
    void addMoodForCapsules(Integer capsulesId, Integer moodId);

    // 更新胶囊的心情信息
    void deleteMoodByCapsules(Capsules capsules);
    void addMoodForCapsule(Capsules capsules);


}
