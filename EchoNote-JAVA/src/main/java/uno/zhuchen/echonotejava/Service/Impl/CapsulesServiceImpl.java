package uno.zhuchen.echonotejava.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Repository.CapsulesRepository;
import uno.zhuchen.echonotejava.Service.CapsulesService;
import uno.zhuchen.echonotejava.Utils.JwtUtil;

@Service
@Slf4j
public class CapsulesServiceImpl implements CapsulesService {
    private final CapsulesRepository capsulesRepository;
    public CapsulesServiceImpl(CapsulesRepository capsulesRepository) {
        this.capsulesRepository = capsulesRepository;
    }

    @Override
    public Result addCapsules(Capsules capsules, HttpServletRequest request) {
        try {
            // 检验目标token是否符合目标身份
            if(!capsules.getUserId().toString().equals(JwtUtil.getUserIdFromRequest(request))) {
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
    public Result getCapsules(Capsules capsules, HttpServletRequest request) {
        if(capsules.getUserId() != null && capsules.getId() != null) {
            log.error("查询胶囊参数错误");
            return Result.error("查询参数错误");
        }
        if(capsules.getUserId() != null) {
            if(!capsules.getUserId().toString().equals(JwtUtil.getUserIdFromRequest(request))) {
                log.error("身份验证失败, 查询失败");
                return Result.error("身份验证失败, 查询失败");
            }
            return Result.success("查询成功", capsulesRepository.getCapsulesByUserId(capsules.getUserId()));
        }

        Capsules capsule = capsulesRepository.getCapsuleById(capsules.getId());
        if(capsule == null) {
            log.error("查询胶囊不存在");
            return Result.error("查询胶囊不存在");
        }
        if(!capsule.getUserId().toString().equals(JwtUtil.getUserIdFromRequest(request))) {
            log.error("身份验证失败, 查询失败");
            return Result.error("身份验证失败, 查询失败");
        }
        return Result.success("查询成功", capsule);
    }

    @Override
    @Transactional
    public Result updateCapsulesById(Capsules capsules, HttpServletRequest request) {
        Capsules capsule = capsulesRepository.getCapsuleById(capsules.getId());
        if(capsule == null) {
            log.error("胶囊不存在");
            return Result.error("胶囊不存在");
        }
        if(!capsule.getUserId().toString().equals(JwtUtil.getUserIdFromRequest(request))) {
            log.error("身份验证失败, 删除失败");
            return Result.error("身份验证失败, 删除失败");
        }
        capsulesRepository.updateCapsule(capsules);
        return Result.success("修改成功", capsulesRepository.getCapsuleById(capsules.getId()));

    }

    @Override
    @Transactional
    public Result changeCapsulesStatusById(Integer id, HttpServletRequest request) {
        Capsules capsule = capsulesRepository.getCapsuleById(id);
        if(capsule == null) {
            log.error("胶囊不存在");
            return Result.error("胶囊不存在");
        }
        if(!capsule.getUserId().toString().equals(JwtUtil.getUserIdFromRequest(request))) {
            log.error("身份验证失败, 删除失败");
            return Result.error("身份验证失败, 删除失败");
        }
        capsule.setStatus(!capsule.isStatus());
        capsulesRepository.updateCapsule(capsule);
        return Result.success("修改成功", capsule);
    }






}
