package uno.zhuchen.echonotejava.Repository;

import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;

import java.util.List;

public interface WritingRepository {
    List<Category> getAllCategoriesByUserId(Integer userId);
    List<Text> getAllTextsByUserId(Integer userId);

    void addCategory(Category category);
    void addText(Text text);
    void updateCategory(Category category);
    void deleteCategoryById(Integer id);
    void sortDeleteTextById(Integer id);

    void updateText(Text text);
    List<Text> getTextHistory(Integer textId);
    void resetVersion(Integer textId, Integer version);
}
