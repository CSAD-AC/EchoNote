package uno.zhuchen.echonotejava.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Service.CapsulesService;

import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
public class CapsulesController {
    private final CapsulesService capsulesService;
    public CapsulesController(CapsulesService capsulesService)
    {
        this.capsulesService = capsulesService;
    }

    @PostMapping("/capsules")
    public Result addCapsules(@RequestBody Capsules capsules)
    {
        return capsulesService.addCapsules(capsules);
    }

    @GetMapping("/capsule")
    public Result getCapsule(@RequestParam Integer id)
    {
        if(id == null) {
            log.error("查询胶囊参数错误");
            return Result.error("查询参数错误");
        }
        return capsulesService.getCapsule(id);
    }
    @GetMapping("/capsules/mood")
    public Result getCapsulesByMood()
    {
        return capsulesService.getPreMood();
    }

    @DeleteMapping("/capsule")
    public Result deleteCapsuleById(@RequestParam Integer id) {
        return capsulesService.deleteCapsuleById(id);
    }
    // token中存储了用户id
    @GetMapping("/capsules")
    public Result getCapsulesForUser()
    {
        return capsulesService.getCapsulesForUser();
    }
    @PutMapping("/capsules")
    public Result updateCapsulesById(@RequestBody Capsules capsules)
    {
        return capsulesService.updateCapsulesById(capsules);
    }
    @PutMapping("/capsules/status")
    public Result changeCapsulesStatusById(@RequestBody Map<String, Integer> parameter) {
        Integer id = Objects.requireNonNull(parameter.get("id"));
        return capsulesService.changeCapsulesStatusById(id);
    }

}