package uno.zhuchen.echonotejava.Repository.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Mapper.CapsulesMapper;
import uno.zhuchen.echonotejava.Mapper.MoodsMapper;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Capsules.Mood;
import uno.zhuchen.echonotejava.Repository.CapsulesRepository;

import java.util.List;

@Slf4j
@Component
public class CapsulesRepositoryImpl implements CapsulesRepository {
    private final CapsulesMapper capsulesMapper;
    private final MoodsMapper moodsMapper;
    public CapsulesRepositoryImpl(CapsulesMapper capsulesMapper, MoodsMapper moodsMapper) {
        this.capsulesMapper = capsulesMapper;
        this.moodsMapper = moodsMapper;
    }
    @Override
    @Transactional
    public void addCapsule(Capsules capsule) {
        // 添加胶囊数据
        capsulesMapper.addCapsules(capsule);
        // 添加胶囊-心情关联表数据
        capsule.getMoods().forEach(mood -> capsulesMapper.addMoodForCapsules(capsule.getId(), mood.getId()));
        // 添加新心情数据
        if(capsule.getNewMood() != null) {
            Mood newMood = new Mood(null, capsule.getNewMood().getName(), null);
            moodsMapper.addMood(newMood);
            capsulesMapper.addMoodForCapsules(capsule.getId(), newMood.getId());
        }
    }

    @Override
    @Transactional
    public List<Capsules> getCapsulesByUserId(Integer userId) {
        List<Capsules> capsules = capsulesMapper.findCapsulesByUserId(userId);
        capsules.forEach(capsule -> {
            capsule.setMoods(
                    moodsMapper.findAllMoodByCapsulesId(capsule.getId())
            );
        });
        return capsules;
    }



    @Override
    // 用于数据回显
    public Capsules getCapsuleById(Integer id) {
        Capsules capsule = capsulesMapper.findCapsulesById(id);
        if (capsule != null) {
            capsule.setMoods(
                    moodsMapper.findAllMoodByCapsulesId(capsule.getId())
            );
        }
        return capsule;
    }

    @Override
    @Transactional
    public void updateCapsule(Capsules capsule) {
        capsulesMapper.updateCapsulesById(capsule);
        capsulesMapper.deleteMoodByCapsules(capsule);
        if(capsule.getNewMood() != null) {
            Mood newMood = new Mood(null, capsule.getNewMood().getName(), null);
            moodsMapper.addMood(newMood);
            capsulesMapper.addMoodForCapsules(capsule.getId(), newMood.getId());
        }
        if (capsule.getMoods() != null && !capsule.getMoods().isEmpty()) {
            capsulesMapper.addMoodForCapsule(capsule);
        }
    }

    @Override
    public List<Mood> getPreMoods() {
        return moodsMapper.getPreMoods();
    }

    @Override
    @Transactional
    public void deleteCapsuleById(Integer id) {
        capsulesMapper.deleteMoodByCapsulesId(id);
        capsulesMapper.deleteCapsulesById(id);
    }
}
