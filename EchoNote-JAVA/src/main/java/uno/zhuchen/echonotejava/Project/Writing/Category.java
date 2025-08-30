package uno.zhuchen.echonotejava.Project.Writing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private Integer userId;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
