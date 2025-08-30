package uno.zhuchen.echonotejava.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uno.zhuchen.echonotejava.Mapper.CategoryMapper;
import uno.zhuchen.echonotejava.Mapper.TextMapper;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;
import uno.zhuchen.echonotejava.Repository.WritingRepository;
import uno.zhuchen.echonotejava.Service.WritingService;
import uno.zhuchen.echonotejava.Utils.AuthUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class WritingServiceImpl implements WritingService {
    private final WritingRepository writingRepository;
    private final TextMapper textMapper;
    private final CategoryMapper categoryMapper;
    private final AuthUtil authUtil;
    @Autowired
    public WritingServiceImpl(WritingRepository writingRepository, AuthUtil authUtil,
                              TextMapper textMapper, CategoryMapper categoryMapper) {
        this.writingRepository = writingRepository;
        this.authUtil = authUtil;
        this.textMapper = textMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Result getAllCategory() {
        Integer userId = authUtil.getCurrentUserId();
        return Result.success(writingRepository.getAllCategoriesByUserId(userId));
    }

    @Override
    public Result getAllTexts() {
        Integer userId = authUtil.getCurrentUserId();
        return Result.success(writingRepository.getAllTextsByUserId(userId));
    }

    @Override
    public Result getTextHistory(Integer textId) {
        Integer userId = authUtil.getCurrentUserId();
        Text text = textMapper.getTextById(textId);
        if (text == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.getCategoryById(text.getCategoryId());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 获取文章历史失败");
            return Result.error("权限校验未通过, 获取文章历史失败");
        }
        return Result.success(writingRepository.getTextHistory(textId));
    }

    @Override
    public Result addCategory(Category category) {
        Integer userId = authUtil.getCurrentUserId();
        if(!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 添加分类失败");
            return Result.error("权限校验未通过, 添加分类失败");
        }
        writingRepository.addCategory(category);
        return Result.success("添加分类成功");
    }

    @Override
    public Result addText(Text text) {
        Integer userId = authUtil.getCurrentUserId();
        Category category = categoryMapper.getCategoryById(text.getCategoryId());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 新增文章失败");
            return Result.error("权限校验未通过, 新增文章失败");
        }
        writingRepository.addText(text);
        Map<String, Object> map = new HashMap<>();
        map.put("textId", text.getId());
        return Result.success(map);
    }

    @Override
    public Result deleteCategoryById(Integer id) {
        Integer userId = authUtil.getCurrentUserId();
        Category category = categoryMapper.getCategoryById(id);
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 删除分类失败");
            return Result.error("权限校验未通过, 删除分类失败");
        }
        List<Text> texts = textMapper.getAllTextByCategoryId(id);
        if(!texts.isEmpty()) {
            log.error("该分类下有文章, 请先删除文章");
            return Result.error("该分类下有文章, 请先删除文章");
        }
        writingRepository.deleteCategoryById(id);
        return Result.success("删除分类成功");


    }

    @Override
    public Result softDeleteTextById(Integer id) {
        Integer userId = authUtil.getCurrentUserId();
        Text text = textMapper.getTextById(id);
        if (text == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.getCategoryById(text.getCategoryId());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 删除文章失败");
            return Result.error("权限校验未通过, 删除文章失败");
        }
        writingRepository.sortDeleteTextById(id);
        return Result.success("删除文章成功");
    }

    @Override
    public Result updateCategory(Category category) {
        String name = category.getName();
        Integer userId = authUtil.getCurrentUserId();
        category = categoryMapper.getCategoryById(category.getId());
        if (category == null) {
            log.error("未找到对应的分类");
            return Result.error("未找到对应的分类");
        }
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 修改分类失败");
            return Result.error("权限校验未通过, 修改分类失败");
        }
        category.setName(name);
        writingRepository.updateCategory(category);
        return Result.success("修改分类成功");
    }

    @Override
    public Result updateText(Text text) {
        // 核验身份
        Integer userId = authUtil.getCurrentUserId();
        Text theText = textMapper.getTextForUpdateById(text.getTextId());
        if (theText == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.getCategoryById(theText.getCategoryId());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 修改文章失败");
            return Result.error("权限校验未通过, 修改文章失败");
        }
        // 需要依据最新版本填充version
        text.setVersion(theText.getCurrentVersion() + 1);

        // 保存文章版本前进行历史记录管理
        manageTextHistory(text.getTextId());

        writingRepository.updateText(text);
        return Result.success("修改文章成功");

    }
    /**
     * 管理文章历史记录
     * 保持历史记录数量上限为50份
     * 超过上限时，通过间隔删除的方式减少历史记录数量
     * @param textId 文章ID
     */
    private void manageTextHistory(Integer textId) {
        List<Text> historyList = writingRepository.getTextHistory(textId);

        // 如果历史记录数量未达到上限，直接返回
        if (historyList.size() <= 50) {
            return;
        }

        // 当历史记录超过上限时，执行间隔删除策略
        // 从第一个版本开始，每隔一个版本删除一个版本（即删除版本2、4、6、8...）
        int deleteCount = historyList.size() - 50;
        int deleted = 0;

        // 从索引1开始（即第二个版本），每隔一个删除一个
        for (int i = 1; i < historyList.size() && deleted < deleteCount; i++) {
            Text textToDelete = historyList.get(i);
            // 删除该版本的历史记录
            textMapper.deleteBackupTextById(textToDelete.getId());
            deleted++;

            // 跳过下一个版本（实现每隔一个删除一个的效果）
            i++;
        }
    }

    @Override
    public Result resetTextVersion(Integer textId, Integer version) {
        Integer userId = authUtil.getCurrentUserId();
        Text theText = textMapper.getTextById(textId);
        if (theText == null) {
            log.error("未找到对应的文章");
            return Result.error("未找到对应的文章");
        }
        Category category = categoryMapper.getCategoryById(theText.getCategoryId());
        if (!category.getUserId().equals(userId)) {
            log.error("权限校验未通过, 重置文章版本失败");
            return Result.error("权限校验未通过, 重置文章版本失败");
        }
        writingRepository.resetVersion(textId, version);
        return Result.success("重置文章版本成功");
    }

}
