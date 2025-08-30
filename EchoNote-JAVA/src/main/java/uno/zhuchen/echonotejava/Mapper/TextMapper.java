package uno.zhuchen.echonotejava.Mapper;

import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.Writing.Text;

import java.util.List;

@Mapper
public interface TextMapper {
    void addText(Text text);
    void updateText(Text text);
    void addBackupText(Text text);
    List<Text> getAllTextsByUserId(Integer userId);
    Text getTextById(Integer id);

    Text getTextForUpdateById(Integer id);
    List<Text> getAllTextByCategoryId(Integer categoryId);


    void softDeleteTextById(Integer id);
    void deleteBackupTextById(Integer id);

    List<Text> getTextHistory(Integer textId);
    Text getBackupTextByVersion(Integer textId, Integer version);
}
