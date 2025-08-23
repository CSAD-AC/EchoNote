package uno.zhuchen.echonotejava.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uno.zhuchen.echonotejava.Mapper.UserMapper;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.User.LoginRequest;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Repository.UserRepository;
import uno.zhuchen.echonotejava.Utils.JwtUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    @Autowired
    public void setUserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/login"); // 设置登录路径
    }
    /**
     * 尝试认证
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 认证结果
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            // 从请求体中获取用户名和密码
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 认证成功处理
     * @param request HTTP请求
     * @param response HTTP响应
     * @param chain 过滤器链
     * @param authResult 认证结果
     * @throws IOException IO异常
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 认证成功，生成JWT令牌
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        log.info("用户 {} 登录成功", user.getUsername());

        Map<String, Object> claims = new HashMap<>(Map.of("userName", user.getUsername()));
        claims.put("userId", userRepository.findUserAndRolesByUsername(user.getUsername()).getId());
        String token = JwtUtil.generateToken(claims);
        // 将令牌添加到响应头
        response.addHeader("Authorization", "Bearer " + token);

        // 返回JSON响应
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userRepository.findUserAndRolesByUsername(user.getUsername()).getId());
        userInfo.put("userName", user.getUsername());
        userInfo.put("name", userRepository.findUserAndRolesByUsername(user.getUsername()).getName());
        userInfo.put("role", userRepository.findUserAndRolesByUsername(user.getUsername()).getRoles().stream().map(Role::getName).toList());
        data.put("userInfo", userInfo);
        Result result = Result.success("登录成功", data);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), result);

    }
    /**
     * 认证失败处理
     * @param request HTTP请求
     * @param response HTTP响应
     * @param failed 认证失败异常
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 认证失败，返回错误信息
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "无效的用户名或者密码");
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}
