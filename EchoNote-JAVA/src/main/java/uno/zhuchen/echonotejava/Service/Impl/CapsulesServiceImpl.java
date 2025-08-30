package uno.zhuchen.echonotejava.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Repository.CapsulesRepository;
import uno.zhuchen.echonotejava.Repository.UserRepository;
import uno.zhuchen.echonotejava.Service.CapsulesService;
import uno.zhuchen.echonotejava.Project.User.User;
import uno.zhuchen.echonotejava.Utils.AuthUtil;


@Service
@Slf4j
public class CapsulesServiceImpl implements CapsulesService {
    private final CapsulesRepository capsulesRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Autowired
    public CapsulesServiceImpl(CapsulesRepository capsulesRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.capsulesRepository = capsulesRepository;
        this.userRepository = userRepository;
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

            capsulesRepository.addCapsule(capsules);
            capsules = capsulesRepository.getCapsuleById(capsules.getId());
            return Result.success("添加成功", capsules);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("添加失败");
        }
    }

    @Override
    public Result getCapsule(Integer id) {
        Capsules capsule = capsulesRepository.getCapsuleById(id);
        checkCapsule(capsule, "查询失败");
        return Result.success("查询成功", capsule);
    }

    @Override
    public Result getCapsulesForUser() {
        Integer userId = authUtil.getCurrentUserId();
        return Result.success("查询成功", capsulesRepository.getCapsulesByUserId(userId));
    }

    @Override
    public Result getPreMood() {
        return Result.success("查询成功", capsulesRepository.getPreMoods());
    }

    @Override
    @Transactional
    public Result deleteCapsuleById(Integer id) {
        Capsules capsule = capsulesRepository.getCapsuleById(id);
        checkCapsule(capsule, "删除失败");
        capsulesRepository.deleteCapsuleById(id);
        return Result.success("删除成功");
    }

    @Override
    @Transactional
    public Result updateCapsulesById(Capsules capsules) {
        Capsules capsule = capsulesRepository.getCapsuleById(capsules.getId());
        checkCapsule(capsule, "更新失败");
        capsulesRepository.updateCapsule(capsules);
        return Result.success("修改成功", capsulesRepository.getCapsuleById(capsules.getId()));

    }

    @Override
    @Transactional
    public Result changeCapsulesStatusById(Integer id) {
        Capsules capsule = capsulesRepository.getCapsuleById(id);
        checkCapsule(capsule, "更新失败");
        capsule.setStatus(!capsule.isStatus());
        capsulesRepository.updateCapsule(capsule);
        return Result.success("修改成功", capsule);
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