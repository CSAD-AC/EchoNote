package uno.zhuchen.echonotejava.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uno.zhuchen.echonotejava.Project.Writing.TextVersionHistory;

import java.util.List;

@Mapper
public interface TextVersionHistoryMapper extends BaseMapper<TextVersionHistory> {

    void insertVersionHistory(@Param("textId") Long textId,
                               @Param("version") Integer version,
                               @Param("diffTitle") String diffTitle,
                               @Param("diffContent") String diffContent,
                               @Param("changeSummary") String changeSummary);

    List<TextVersionHistory> getVersionHistoryByTextId(@Param("textId") Long textId);

    TextVersionHistory getVersionByVersionNumber(@Param("textId") Long textId, @Param("version") Integer version);

    List<TextVersionHistory> getVersionListByTextId(@Param("textId") Long textId);

    int softDeleteVersion(@Param("textId") Long textId, @Param("version") Integer version);

    int decrementVersionsAfter(@Param("textId") Long textId, @Param("version") Integer version);
}
