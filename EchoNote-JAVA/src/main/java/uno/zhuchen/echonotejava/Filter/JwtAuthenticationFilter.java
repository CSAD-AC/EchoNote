package uno.zhuchen.echonotejava.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.User.LoginRequest;
import uno.zhuchen.echonotejava.Project.User.Role;
import uno.zhuchen.echonotejava.Project.User.User;
import uno.zhuchen.echonotejava.Service.Impl.UserQueryService;
import uno.zhuchen.echonotejava.Utils.JwtUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserQueryService userQueryService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserQueryService userQueryService) {
        this.authenticationManager = authenticationManager;
        this.userQueryService = userQueryService;
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

            log.info("[登录] 收到登录请求，用户名: {}", loginRequest.getUserName());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
        } catch (IOException e) {
            log.error("[登录] 解析请求体失败", e);
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

        User currentUser = userQueryService.findUserAndRolesByUsername(user.getUsername());
        if (currentUser == null) {
            throw new ServletException("用户不存在");
        }

        Map<String, Object> claims = new HashMap<>(Map.of("userName", user.getUsername()));
        claims.put("userId", currentUser.getId());
        String token = JwtUtil.generateToken(claims);
        // 将令牌添加到响应头
        response.addHeader("Authorization", "Bearer " + token);

        // 返回JSON响应
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", currentUser.getId());
        userInfo.put("userName", user.getUsername());
        userInfo.put("name", currentUser.getName());
        userInfo.put("role", currentUser.getRoles().stream().map(Role::getName).toList());
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
        // 认证失败，记录原因并返回错误信息
        log.warn("[登录] 认证失败 - 异常类型: {}, 原因: {}", failed.getClass().getSimpleName(), failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "无效的用户名或者密码");
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}
