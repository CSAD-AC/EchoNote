package uno.zhuchen.echonotejava.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Mapper.CapsulesMapper;
import uno.zhuchen.echonotejava.Mapper.MoodsMapper;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Capsules.Mood;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Service.CapsulesService;
import uno.zhuchen.echonotejava.Utils.AuthUtil;

import java.util.List;


@Service
@Slf4j
public class CapsulesServiceImpl implements CapsulesService {
    private final CapsulesMapper capsulesMapper;
    private final MoodsMapper moodsMapper;
    private final AuthUtil authUtil;

    @Autowired
    public CapsulesServiceImpl(CapsulesMapper capsulesMapper, MoodsMapper moodsMapper, AuthUtil authUtil) {
        this.capsulesMapper = capsulesMapper;
        this.moodsMapper = moodsMapper;
        this.authUtil = authUtil;
    }


    @Override
    public Result addCapsules(Capsules capsules) {
        try {
            Integer currentUserId = authUtil.getCurrentUserId();
            if (!capsules.getUserId().equals(currentUserId)) {
                log.error("身份验证失败, 添加失败");
                return Result.error("身份验证失败, 添加失败");
            }

            capsulesMapper.insert(capsules);
            bindCapsuleMoods(capsules);
            capsules = getCapsuleWithMoods(capsules.getId());
            return Result.success("添加成功", capsules);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("添加失败");
        }
    }

    @Override
    public Result getCapsule(Integer id) {
        Capsules capsule = getCapsuleWithMoods(id);
        checkCapsule(capsule, "查询失败");
        return Result.success("查询成功", capsule);
    }

    @Override
    public Result getCapsulesForUser() {
        Integer userId = authUtil.getCurrentUserId();
        QueryWrapper<Capsules> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        List<Capsules> capsulesList = capsulesMapper.selectList(queryWrapper);
        capsulesList.forEach(capsule -> capsule.setMoods(moodsMapper.findAllMoodByCapsulesId(capsule.getId())));
        return Result.success("查询成功", capsulesList);
    }

    @Override
    public Result getPreMood() {
        QueryWrapper<Mood> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 40);
        queryWrapper.orderByAsc("id");
        return Result.success("查询成功", moodsMapper.selectList(queryWrapper));
    }

    @Override
    @Transactional
    public Result deleteCapsuleById(Integer id) {
        Capsules capsule = getCapsuleWithMoods(id);
        checkCapsule(capsule, "删除失败");
        capsulesMapper.deleteMoodByCapsulesId(id);
        capsulesMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result updateCapsulesById(Capsules capsules) {
        Capsules capsule = getCapsuleWithMoods(capsules.getId());
        checkCapsule(capsule, "更新失败");

        capsulesMapper.updateById(capsules);
        capsulesMapper.deleteMoodByCapsules(capsules.getId());
        bindCapsuleMoods(capsules);
        return Result.success("修改成功", getCapsuleWithMoods(capsules.getId()));

    }

    @Override
    @Transactional
    public Result changeCapsulesStatusById(Integer id) {
        Capsules capsule = getCapsuleWithMoods(id);
        checkCapsule(capsule, "更新失败");
        capsule.setStatus(!capsule.getStatus());
        capsulesMapper.updateById(capsule);
        return Result.success("修改成功", capsule);
    }

    private Capsules getCapsuleWithMoods(Integer id) {
        Capsules capsule = capsulesMapper.selectById(id);
        if (capsule != null) {
            capsule.setMoods(moodsMapper.findAllMoodByCapsulesId(capsule.getId()));
        }
        return capsule;
    }

    private void bindCapsuleMoods(Capsules capsule) {
        if (capsule.getMoods() != null && !capsule.getMoods().isEmpty()) {
            capsulesMapper.addMoodForCapsule(capsule);
        }
        if (capsule.getNewMood() != null && capsule.getNewMood().getName() != null && !capsule.getNewMood().getName().isBlank()) {
            Mood newMood = new Mood();
            newMood.setName(capsule.getNewMood().getName());
            moodsMapper.insert(newMood);
            capsulesMapper.addMoodForCapsules(capsule.getId(), newMood.getId());
        }
    }

    private void checkCapsule(Capsules capsule, String msg) {
        if(capsule == null) {
            log.error("胶囊不存在");
            throw new RuntimeException("胶囊不存在");
        }
        Integer currentUserId = authUtil.getCurrentUserId();
        if(!capsule.getUserId().equals(currentUserId)) {
            log.error("身份验证失败, " + msg);
            throw new RuntimeException("身份验证失败, " + msg);
        }
    }
}