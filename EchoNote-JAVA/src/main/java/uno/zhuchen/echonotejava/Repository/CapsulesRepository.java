package uno.zhuchen.echonotejava.Repository;

import uno.zhuchen.echonotejava.Project.Capsules.Capsules;

import java.util.List;

public interface CapsulesRepository {
    void addCapsule(Capsules capsule);
    List<Capsules> getCapsulesByUserId(Integer userId);
    Capsules getCapsuleById(Integer id);
    void updateCapsule(Capsules capsule);
}
