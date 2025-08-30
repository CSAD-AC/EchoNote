package uno.zhuchen.echonotejava.Repository.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Mapper.CategoryMapper;
import uno.zhuchen.echonotejava.Mapper.TextMapper;
import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;
import uno.zhuchen.echonotejava.Repository.WritingRepository;

import java.util.List;

@Component
public class WritingRepositoryImpl implements WritingRepository {
    private final TextMapper textMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public WritingRepositoryImpl(TextMapper textMapper, CategoryMapper categoryMapper) {
        this.textMapper = textMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getAllCategoriesByUserId(Integer userId) {
        return categoryMapper.getAllCategoriesByUserId(userId);
    }

    @Override
    public List<Text> getAllTextsByUserId(Integer userId) {
        return textMapper.getAllTextsByUserId(userId);
    }

    @Override
    public void addCategory(Category category) {
        categoryMapper.addCategory(category);
    }

    @Override
    public void addText(Text text) {
        textMapper.addText(text);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategoryById(Integer id) {
        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public void sortDeleteTextById(Integer id) {
        textMapper.softDeleteTextById(id);
    }

    @Override
    @Transactional
    public void updateText(Text text) {
        // 需要填充version, textId, title, content属性
        // 添加当前文本作为新的备份版本
        textMapper.addBackupText(text);

        // 更新文章至最新的备份版本
        Text theText = textMapper.getTextById(text.getTextId());
        theText.setCurrentVersion(text.getVersion());
        textMapper.updateText(theText);
    }

    @Override
    public List<Text> getTextHistory(Integer textId) {
        return textMapper.getTextHistory(textId);
    }

    @Override
    @Transactional
    public void resetVersion(Integer textId, Integer version) {
        // 通过textId获取当前文本对象
        Text text = textMapper.getTextById(textId);
        // 根据textId和指定版本号获取历史备份文本
        Text theText = textMapper.getBackupTextByVersion(textId, version);
        // 设置历史文本的版本号为当前版本号+1
        theText.setVersion(text.getCurrentVersion() + 1);
        // 将修改后的文本作为新的备份版本添加到数据库
        textMapper.addBackupText(theText);
        // 设置原文本的当前版本为新创建的版本号
        text.setCurrentVersion(theText.getVersion());
        // 更新数据库中的文本记录
        textMapper.updateText(text);
    }

}