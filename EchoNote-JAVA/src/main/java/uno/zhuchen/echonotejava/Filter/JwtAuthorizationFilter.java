package uno.zhuchen.echonotejava.Filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uno.zhuchen.echonotejava.Service.Impl.UserDetailsServiceImpl;
import uno.zhuchen.echonotejava.Utils.JwtUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT授权过滤器，验证JWT令牌并授权访问
 * 继承自OncePerRequestFilter，确保每个请求只被过滤一次
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    UserDetailsServiceImpl userDetailsServiceImpl;
    public JwtAuthorizationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsServiceImpl = userDetailsService;
    }
    /**
     * 过滤请求
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 放行OPTIONS预检请求和注册请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || "/register".equalsIgnoreCase(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring(7);
                String username = JwtUtil.extractUsername(token);
                log.info("从JWT令牌中提取用户名: {}", username);

                // 只有在当前没有认证信息时才进行认证
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (JwtUtil.validateToken(token, username)) {
                        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                        log.info("加载用户详情，用户: {}, 权限: {}", username, userDetails.getAuthorities());

                        // 构建 Authentication 对象并注入权限
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication); // 关键步骤: 设置认证信息加入安全上下文
                        log.info("用户已授权访问");
                    } else {
                        log.warn("JWT令牌验证失败");
                    }
                }
            } catch (Exception e) {
                log.error("JWT token解析或验证失败: ", e);
            }
        } else {
            log.debug("未找到有效的JWT令牌");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "用户未登录");
            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
        }

        filterChain.doFilter(request, response);
    }
}
