package uno.zhuchen.echonotejava.Repository;

import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Capsules.Mood;

import java.util.List;
import java.util.Set;

public interface CapsulesRepository {

    void addCapsule(Capsules capsule);
    List<Capsules> getCapsulesByUserId(Integer userId);
    Capsules getCapsuleById(Integer id);
    void updateCapsule(Capsules capsule);
    List<Mood> getPreMoods();
    void deleteCapsuleById(Integer id);
}
