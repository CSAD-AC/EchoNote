package uno.zhuchen.echonotejava.Utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uno.zhuchen.echonotejava.Project.User.User;
import uno.zhuchen.echonotejava.Service.Impl.UserQueryService;

@Component
public class AuthUtil {
    private final UserQueryService userQueryService;

    public AuthUtil(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                User user = userQueryService.findUserAndRolesByUsername(username);
                return user != null ? user.getId() : null;
            }
        }
        return null;
    }
}
