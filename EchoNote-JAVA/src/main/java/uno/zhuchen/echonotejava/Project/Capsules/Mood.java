package uno.zhuchen.echonotejava.Project.Capsules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mood {
    private Integer id;
    private String name;
    private LocalDateTime createTime;
}
