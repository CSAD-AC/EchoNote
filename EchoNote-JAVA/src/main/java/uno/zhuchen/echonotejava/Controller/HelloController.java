package uno.zhuchen.echonotejava.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class HelloController {
    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin(Authentication authentication){
        // 添加日志输出当前认证用户信息
        log.info("访问/admin接口，当前认证用户: {}, 权限: {}",
                authentication.getName(),
                authentication.getAuthorities());

        return "Admin";
    }
}
