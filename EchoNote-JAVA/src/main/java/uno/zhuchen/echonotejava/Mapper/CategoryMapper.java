package uno.zhuchen.echonotejava.Mapper;

import org.apache.ibatis.annotations.Mapper;
import uno.zhuchen.echonotejava.Project.Writing.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    void addCategory(Category category);
    void updateCategory(Category category);
    List<Category> getAllCategoriesByUserId(Integer userId);
    Category getCategoryById(Integer id);
    void deleteCategoryById(Integer id);
}
