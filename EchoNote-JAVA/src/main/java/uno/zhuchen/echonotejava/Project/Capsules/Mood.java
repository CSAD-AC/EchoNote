package uno.zhuchen.echonotejava.Project.Capsules;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("moods")
public class Mood {
    private Integer id;
    private String name;
    private LocalDateTime createTime;
}
