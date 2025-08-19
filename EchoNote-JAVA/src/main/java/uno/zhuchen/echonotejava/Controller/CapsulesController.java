package uno.zhuchen.echonotejava.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Service.CapsulesService;

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

    @GetMapping("/capsules")
    public Result getCapsules(@RequestBody Capsules capsules, HttpServletRequest request)
    {
        return capsulesService.getCapsules(capsules, request);
    }
    @PutMapping("/capsules")
    public Result updateCapsulesById(@RequestBody Capsules capsules, HttpServletRequest request)
    {
        return capsulesService.updateCapsulesById(capsules, request);
    }
    @PutMapping("/capsules/status")
    public Result changeCapsulesStatusById(@RequestParam Integer id, HttpServletRequest request) {
        return capsulesService.changeCapsulesStatusById(id, request);
    }

}
