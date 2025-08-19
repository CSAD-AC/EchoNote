package uno.zhuchen.echonotejava.Project.User;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;     // 用户id
    private String userName;// 用户名
    private String password;// 密码
    private String email;   // 邮箱
    private String image;   // 头像
    private String name;    // 昵称
    private Set<Role> roles;// 角色
    private boolean enabled = true;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

