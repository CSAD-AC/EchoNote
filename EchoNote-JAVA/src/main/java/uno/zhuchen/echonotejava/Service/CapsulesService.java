package uno.zhuchen.echonotejava.Service;

import jakarta.servlet.http.HttpServletRequest;
import uno.zhuchen.echonotejava.Project.Capsules.Capsules;
import uno.zhuchen.echonotejava.Project.Result;

public interface CapsulesService {
    // 添加胶囊
    Result addCapsules(Capsules capsules, HttpServletRequest request);
    // 修改胶囊
    Result updateCapsulesById(Capsules capsules, HttpServletRequest request);
    // 删除胶囊
    Result changeCapsulesStatusById(Integer id, HttpServletRequest request);
    // 获取指定胶囊
    Result getCapsule(Integer id, HttpServletRequest request);
    // 获取指定用户的胶囊
    Result getCapsulesForUser(HttpServletRequest request);
    // 获取预设心情
    Result getPreMood();
    // 删除指定id的胶囊
    Result deleteCapsuleById(Integer id, HttpServletRequest request);
}
