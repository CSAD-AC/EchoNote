package uno.zhuchen.echonotejava.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uno.zhuchen.echonotejava.Project.Result;
import uno.zhuchen.echonotejava.Project.Writing.Category;
import uno.zhuchen.echonotejava.Project.Writing.Text;
import uno.zhuchen.echonotejava.Service.WritingService;

@RestController
@Slf4j
@RequestMapping("/writing")
public class WritingController {
    private final WritingService writingService;
    public WritingController(WritingService writingService) {
        this.writingService = writingService;
    }
    @GetMapping("/category")
    public Result getAllCategory() {
        return writingService.getAllCategory();
    }
    @GetMapping("/text")
    public Result getAllTexts() {
        return writingService.getAllTexts();
    }
    @GetMapping("/text/history")
    public Result getTextHistory(@RequestParam Integer textId) {
        if (textId == null) {
            log.error("getTextHistory方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.getTextHistory(textId);
    }

    @PostMapping("/category")
    public Result addCategory(@RequestBody Category category) {
        if (category.getUserId() == null || category.getName() == null) {
            log.error("addCategory方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.addCategory(category);
    }

    @PostMapping("/text")
    public Result addText(@RequestBody Text text) {
        if (text.getCategoryId() == null) {
            log.error("addText方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.addText(text);
    }

    @DeleteMapping("/category")
    public Result deleteCategoryById(@RequestParam Integer id) {
        if (id == null) {
            log.error("deleteCategoryById方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.deleteCategoryById(id);
    }
    @DeleteMapping("/text")
    public Result softDeleteTextById(@RequestParam Integer id) {
        if (id == null) {
            log.error("softDeleteTextById方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.softDeleteTextById(id);
    }

    @PutMapping("/category")
    public Result updateCategory(@RequestBody Category category) {
        if (category.getId() == null || category.getName() == null) {
            log.error("updateCategory方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.updateCategory(category);
    }

    @PutMapping("/text")
    public Result updateText(@RequestBody Text text) {
        if (text.getTextId() == null || text.getTitle() == null
                || text.getContent() == null) {
            log.error("updateText方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.updateText(text);
    }
    @PutMapping("/text/reset")
    public Result resetTextVersion(@RequestBody Text text) {
        if (text.getTextId() == null || text.getVersion() == null) {
            log.error("resetTextVersion方法参数错误");
            return Result.error("参数错误");
        }
        return writingService.resetTextVersion(text.getTextId(), text.getVersion());
    }
}
