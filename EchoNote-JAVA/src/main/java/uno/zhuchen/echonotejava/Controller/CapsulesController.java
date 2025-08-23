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
    public Result addCapsules(@RequestBody Capsules capsules, HttpServletRequest request)
    {
        return capsulesService.addCapsules(capsules, request);
    }

    @GetMapping("/capsule")
    public Result getCapsule(@RequestParam Integer id, HttpServletRequest request)
    {
        return capsulesService.getCapsule(id, request);
    }
    @GetMapping("/capsules/mood")
    public Result getCapsulesByMood()
    {
        return capsulesService.getPreMood();
    }

    @DeleteMapping("/capsule")
    public Result deleteCapsuleById(@RequestParam Integer id, HttpServletRequest request) {
        return capsulesService.deleteCapsuleById(id,  request);
    }
    // token中存储了用户id
    @GetMapping("/capsules")
    public Result getCapsulesForUser(HttpServletRequest request)
    {
        return capsulesService.getCapsulesForUser(request);
    }
    @PutMapping("/capsules")
    public Result updateCapsulesById(@RequestBody Capsules capsules, HttpServletRequest request)
    {
        return capsulesService.updateCapsulesById(capsules, request);
    }
    @PutMapping("/capsules/status")
    public Result changeCapsulesStatusById(@RequestBody Map<String, Integer> parameter, HttpServletRequest request) {
        Integer id = Objects.requireNonNull(parameter.get("id"));
        return capsulesService.changeCapsulesStatusById(id, request);
    }

}
