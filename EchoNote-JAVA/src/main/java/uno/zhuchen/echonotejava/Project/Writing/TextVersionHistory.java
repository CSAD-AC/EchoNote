package uno.zhuchen.echonotejava.Project.Writing;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("text_version_history")
public class TextVersionHistory {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("text_id")
    private Long textId;

    private Integer version;

    @TableField("diff_title")
    private String diffTitle;

    @TableField("diff_content")
    private String diffContent;

    @TableField("change_summary")
    private String changeSummary;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private String content;

    @TableField(exist = false)
    private Integer currentVersion;
}
