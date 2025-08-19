package uno.zhuchen.echonotejava.Service;

import jakarta.servlet.http.HttpServletRequest;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;

public interface CapsulesService {
    Result addCapsules(Capsules capsules, HttpServletRequest request);
    Result updateCapsulesById(Capsules capsules, HttpServletRequest request);
    Result changeCapsulesStatusById(Integer id, HttpServletRequest request);
    Result getCapsules(Capsules capsules, HttpServletRequest request);
}
