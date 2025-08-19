package uno.zhuchen.echonotejava.Project.User;

import lombok.Data;

@Data
public class Role {
    private Long id;
    private String name; // 如 `ROLE_USER`、`ROLE_ADMIN`、`ROLE_INSPECTOR`
}