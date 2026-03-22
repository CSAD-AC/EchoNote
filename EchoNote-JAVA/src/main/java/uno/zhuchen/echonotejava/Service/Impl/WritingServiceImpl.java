package uno.zhuchen.echonotejava.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uno.zhuchen.echonotejava.Mapper.CategoryMapper;
import uno.zhuchen.echonotejava.Mapper.TextMapper;
import uno.zhuchen.echonotejava.Mapper.TextVersionHistoryMapper;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;
import uno.zhuchen.echonotejava.Project.Writing.TextVersionHistory;
import uno.zhuchen.echonotejava.Service.WritingService;
import uno.zhuchen.echonotejava.Utils.AuthUtil;
import uno.zhuchen.echonotejava.Utils.DiffUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class WritingServiceImpl implements WritingService {
    private final TextMapper textMapper;
    private final CategoryMapper categoryMapper;
    private final TextVersionHistoryMapper textVersionHistoryMapper;
    private final AuthUtil authUtil;
    private final DiffUtil diffUtil;

    @Autowired
    public WritingServiceImpl(TextMapper textMapper, CategoryMapper categoryMapper,
                              TextVersionHistoryMapper textVersionHistoryMapper,
                              AuthUtil authUtil, DiffUtil diffUtil) {
        this.textMapper = textMapper;
        this.categoryMapper = categoryMapper;
        this.textVersionHistoryMapper = textVersionHistoryMapper;
        this.authUtil = authUtil;
        this.diffUtil = diffUtil;
    }

    @Override
    public Result getAllCategory() {
        Integer userId = authUtil.getCurrentUserId();
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderByAsc("sort_order");
        return Result.success(categoryMapper.selectList(queryWrapper));
    }

    @Override
    public Result getAllTexts() {
        Integer userId = authUtil.getCurrentUserId();
        QueryWrapper<Text> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderByDesc("update_time");
        return Result.success(textMapper.selectList(queryWrapper));
    }

    @Override
    public Result getTextHistory(Integer textId) {
        Integer userId = authUtil.getCurrentUserId();
        Text text = textMapper.selectById(textId.longValue());
        if (text == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.selectById(text.getCategoryId().intValue());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 获取文章历史失败");
            return Result.error("权限校验未通过, 获取文章历史失败");
        }

        List<TextVersionHistory> historyList = textVersionHistoryMapper.getVersionListByTextId(textId.longValue());

        String currentTitle = text.getTitle();
        String currentContent = text.getContent();

        for (TextVersionHistory history : historyList) {
            if (history.getVersion() == text.getCurrentVersion()) {
                history.setTitle(currentTitle);
                history.setContent(currentContent);
            } else {
                String baseTitle = "";
                String baseContent = "";
                for (TextVersionHistory older : historyList) {
                    if (older.getVersion() < history.getVersion()) {
                        if (older.getTitle() != null) {
                            baseTitle = older.getTitle();
                        }
                        if (older.getContent() != null) {
                            baseContent = older.getContent();
                        }
                        break;
                    }
                }
                history.setTitle(diffUtil.applyTitleDiff(baseTitle, history.getDiffTitle()));
                history.setContent(diffUtil.applyContentDiff(baseContent, history.getDiffContent()));
            }
            history.setCurrentVersion(text.getCurrentVersion());
        }

        return Result.success(historyList);
    }

    @Override
    public Result addCategory(Category category) {
        Integer userId = authUtil.getCurrentUserId();
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 添加分类失败");
            return Result.error("权限校验未通过, 添加分类失败");
        }
        categoryMapper.insert(category);
        return Result.success("添加分类成功");
    }

    @Override
    public Result addText(Text text) {
        Integer userId = authUtil.getCurrentUserId();
        Category category = categoryMapper.selectById(text.getCategoryId().intValue());
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 新增文章失败");
            return Result.error("权限校验未通过, 新增文章失败");
        }
        text.setUserId(userId.longValue());
        if (text.getTitle() == null || text.getTitle().isBlank()) {
            text.setTitle("未命名");
        }
        if (text.getContent() == null) {
            text.setContent("");
        }
        text.setCurrentVersion(1);
        text.setIsDeleted(false);
        textMapper.insert(text);
        Map<String, Object> map = new HashMap<>();
        map.put("textId", text.getId());
        return Result.success(map);
    }

    @Override
    public Result deleteCategoryById(Integer id) {
        Integer userId = authUtil.getCurrentUserId();
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 删除分类失败");
            return Result.error("权限校验未通过, 删除分类失败");
        }
        QueryWrapper<Text> textQueryWrapper = new QueryWrapper<>();
        textQueryWrapper.eq("category_id", id.longValue());
        textQueryWrapper.eq("is_deleted", false);
        List<Text> texts = textMapper.selectList(textQueryWrapper);
        if (!texts.isEmpty()) {
            log.error("该分类下有文章, 请先删除文章");
            return Result.error("该分类下有文章, 请先删除文章");
        }
        Category updateCategory = new Category();
        updateCategory.setId(id);
        updateCategory.setIsDeleted(true);
        categoryMapper.updateById(updateCategory);
        return Result.success("删除分类成功");
    }

    @Override
    public Result softDeleteTextById(Integer id) {
        Integer userId = authUtil.getCurrentUserId();
        Text text = textMapper.selectById(id.longValue());
        if (text == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.selectById(text.getCategoryId().intValue());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 删除文章失败");
            return Result.error("权限校验未通过, 删除文章失败");
        }
        text.setIsDeleted(true);
        textMapper.updateById(text);
        return Result.success("删除文章成功");
    }

    @Override
    public Result updateCategory(Category category) {
        String name = category.getName();
        Integer userId = authUtil.getCurrentUserId();
        category = categoryMapper.selectById(category.getId());
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 修改分类失败");
            return Result.error("权限校验未通过, 修改分类失败");
        }
        category.setName(name);
        categoryMapper.updateById(category);
        return Result.success("修改分类成功");
    }

    @Override
    public Result updateText(Text text) {
        Integer userId = authUtil.getCurrentUserId();
        Text theText = textMapper.selectById(text.getId());
        if (theText == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.selectById(theText.getCategoryId().intValue());
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 修改文章失败");
            return Result.error("权限校验未通过, 修改文章失败");
        }

        Integer newVersion = theText.getCurrentVersion() + 1;

        String diffTitle = diffUtil.computeTitleDiff(theText.getTitle(), text.getTitle());
        String diffContent = diffUtil.computeContentDiff(theText.getContent(), text.getContent());

        textVersionHistoryMapper.insertVersionHistory(
                text.getId(),
                newVersion,
                diffTitle,
                diffContent,
                null
        );

        theText.setTitle(text.getTitle());
        theText.setContent(text.getContent());
        theText.setCurrentVersion(newVersion);
        theText.setIsDeleted(false);
        textMapper.updateById(theText);

        return Result.success("修改文章成功");
    }

    @Override
    public Result resetTextVersion(Integer textId, Integer version) {
        Integer userId = authUtil.getCurrentUserId();
        Text currentText = textMapper.selectById(textId.longValue());
        if (currentText == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.selectById(currentText.getCategoryId().intValue());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 重置文章版本失败");
            return Result.error("权限校验未通过, 重置文章版本失败");
        }

        TextVersionHistory targetVersion = textVersionHistoryMapper.getVersionByVersionNumber(
                textId.longValue(), version);

        if (targetVersion == null) {
            log.error("未找到对应的历史版本");
            return Result.error("未找到对应的历史版本");
        }

        String restoredTitle = diffUtil.applyTitleDiff("", targetVersion.getDiffTitle());
        String restoredContent = diffUtil.applyContentDiff("", targetVersion.getDiffContent());

        Integer newVersion = currentText.getCurrentVersion() + 1;

        textVersionHistoryMapper.insertVersionHistory(
                textId.longValue(),
                newVersion,
                diffUtil.computeTitleDiff(currentText.getTitle(), restoredTitle),
                diffUtil.computeContentDiff(currentText.getContent(), restoredContent),
                "从版本" + version + "还原"
        );

        currentText.setTitle(restoredTitle);
        currentText.setContent(restoredContent);
        currentText.setCurrentVersion(newVersion);
        textMapper.updateById(currentText);

        return Result.success("重置文章版本成功");
    }

    @Override
    @Transactional
    public Result softDeleteTextVersion(Integer textId, Integer version) {
        Integer userId = authUtil.getCurrentUserId();
        Text currentText = textMapper.selectById(textId.longValue());
        if (currentText == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }

        Category category = categoryMapper.selectById(currentText.getCategoryId().intValue());
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 删除历史版本失败");
            return Result.error("权限校验未通过, 删除历史版本失败");
        }
        if (version <= 1) {
            log.error("基础版本不支持删除");
            return Result.error("基础版本不支持删除");
        }
        if (version >= currentText.getCurrentVersion()) {
            log.error("当前版本不支持删除");
            return Result.error("当前版本不支持删除");
        }

        TextVersionHistory targetVersion = textVersionHistoryMapper.getVersionByVersionNumber(
                textId.longValue(), version);
        if (targetVersion == null) {
            log.error("未找到对应的历史版本");
            return Result.error("未找到对应的历史版本");
        }

        textVersionHistoryMapper.softDeleteVersion(textId.longValue(), version);
        textVersionHistoryMapper.decrementVersionsAfter(textId.longValue(), version);

        currentText.setCurrentVersion(currentText.getCurrentVersion() - 1);
        textMapper.updateById(currentText);

        return Result.success("删除历史版本成功");
    }

}
