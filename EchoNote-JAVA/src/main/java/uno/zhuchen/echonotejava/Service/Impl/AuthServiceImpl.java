package uno.zhuchen.echonotejava.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Project.User.User;
import uno.zhuchen.echonotejava.Repository.UserRepository;
import uno.zhuchen.echonotejava.Service.AuthService;

import java.util.HashMap;


@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Result register(User user) {
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.addUser(user);
        } catch (Exception e) {
            log.warn("用户名已存在");
            return Result.error("用户名已存在");
        }

        user = userRepository.findUserAndRolesByUsername(user.getUserName());

        HashMap<String, String> response = new HashMap<>();
        response.put("id", user.getId().toString());
        response.put("userName", user.getUserName());
        response.put("email", user.getEmail());
        response.put("role", user.getRoles().stream().map(Role::getName).toList().get(0));
        log.info("成功注册用户{}, ID为{}", user.getUserName(), user.getId());
        return Result.success("注册成功", response);
    }
}
