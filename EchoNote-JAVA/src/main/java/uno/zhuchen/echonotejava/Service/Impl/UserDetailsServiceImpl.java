package uno.zhuchen.echonotejava.Service.Impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uno.zhuchen.echonotejava.Project.User.User;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserQueryService userQueryService;

    @Autowired
    public UserDetailsServiceImpl(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[登录] 查询用户信息，用户名: {}", username);
        User user = userQueryService.findUserAndRolesByUsername(username);
        if(user == null) {
            log.warn("[登录] 用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        log.info("[登录] 用户查询成功，用户名: {}，角色: {}", username, user.getRoles());

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        log.debug("[登录] 账户状态 - enabled: {}, 密码哈希前缀: {}",
                user.getEnabled(),
                user.getPassword() != null ? user.getPassword().substring(0, Math.min(10, user.getPassword().length())) : "null");
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );

    }

}
