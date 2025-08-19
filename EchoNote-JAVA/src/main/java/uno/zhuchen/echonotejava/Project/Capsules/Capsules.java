package uno.zhuchen.echonotejava.Project.Capsules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capsules {
    private Integer id;
    private Integer userId;
    private String title;
    private String location;
    private String description;
    private List<Mood> moods;
    private Mood newMood;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean status;         // 状态：1-正常，0-归档
}
