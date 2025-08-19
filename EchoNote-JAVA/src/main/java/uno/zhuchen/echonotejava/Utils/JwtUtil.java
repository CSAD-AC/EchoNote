package uno.zhuchen.echonotejava.Utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static String secretKey;
    private static long expiration;
    @Autowired
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expire
    ) {
        JwtUtil.secretKey = secret;
        JwtUtil.expiration = expire;
    }
    /**
     * 生成JWT令牌
     * @param claims 自定义声明内容
     * @return JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌字符串
     * @return 包含所有声明的Claims对象
     */
    public static Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 验证Token是否有效
    public static boolean validateToken(String token, String username) {
        Claims claims = parseToken(token);
        return claims.get("userName").toString().equals(username) && !isTokenExpired(claims);
    }
    // 判断Token是否过期
    private static boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
    /**
     * 获取Token中的用户名
     * @param token JWT令牌字符串
     * @return 用户名
     */
    public static String extractUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("userName").toString();
    }

    /**
     * 从请求头中获取用户ID
     * @param request HTTP请求
     * @return 用户ID
     */
    public static String getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            Claims claims = parseToken(token);
            return claims.get("userId").toString();
        }
        return null;
    }

    /**
     * 从请求头中获取用户名
     * @param request HTTP请求
     * @return 用户名
     */
    public static String getUserNameFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return extractUsername(token);
        }
        return null;
    }

    /**
     * 从请求头中提取Token
     * @param request HTTP请求
     * @return Token字符串
     */
    private static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
