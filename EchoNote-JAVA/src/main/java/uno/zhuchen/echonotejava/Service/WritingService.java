package uno.zhuchen.echonotejava.Service;

import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;

public interface WritingService {
    Result getAllCategory();
    Result getAllTexts();
    Result getTextHistory(Integer textId);

    Result addCategory(Category category);
    Result addText(Text text);

    Result deleteCategoryById(Integer id);
    Result softDeleteTextById(Integer id);

    Result updateCategory(Category category);
    Result updateText(Text text);
    Result resetTextVersion(Integer textId, Integer version);
}
