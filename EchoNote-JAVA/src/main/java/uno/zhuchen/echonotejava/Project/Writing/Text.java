package uno.zhuchen.echonotejava.Project.Writing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Text {
    private Integer id;
    private Integer categoryId;
    private Integer currentVersion;
    private Integer version;
    private String title;
    private String content;
    private Boolean status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Integer TextId; // 用于兼容备份数据读取
}
