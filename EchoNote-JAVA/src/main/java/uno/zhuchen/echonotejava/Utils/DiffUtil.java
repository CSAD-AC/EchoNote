package uno.zhuchen.echonotejava.Utils;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DiffUtil {

    public String computeDiff(String oldText, String newText) {
        if (oldText == null) oldText = "";
        if (newText == null) newText = "";

        String[] oldLines = oldText.split("\n", -1);
        String[] newLines = newText.split("\n", -1);

        Patch<String> patch = DiffUtils.diff(Arrays.asList(oldLines), Arrays.asList(newLines));

        StringBuilder sb = new StringBuilder();
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            sb.append(delta.getType().name())
              .append("|")
              .append(delta.getSource().getPosition())
              .append("|")
              .append(String.join("\n", delta.getSource().getLines()))
              .append("|")
              .append(String.join("\n", delta.getTarget().getLines()))
              .append("\n");
        }
        return sb.toString();
    }

    public String applyDiff(String baseText, String diffText) {
        if (baseText == null) baseText = "";
        if (diffText == null || diffText.isEmpty()) {
            return baseText;
        }

        try {
            String[] baseLines = baseText.split("\n", -1);
            List<String> baseList = Arrays.asList(baseLines);

            Patch<String> patch = new Patch<>();

            String[] deltas = diffText.split("\n");
            for (String deltaStr : deltas) {
                if (deltaStr == null || deltaStr.isEmpty()) continue;

                String[] parts = deltaStr.split("\\|", 4);
                if (parts.length < 4) continue;

                String type = parts[0];
                int position = Integer.parseInt(parts[1]);
                List<String> source = Arrays.asList(parts[2].split("\n"));
                List<String> target = Arrays.asList(parts[3].split("\n"));

                AbstractDelta<String> delta;
                switch (type) {
                    case "CHANGE":
                        delta = new com.github.difflib.patch.ChangeDelta<>(
                                new com.github.difflib.patch.Chunk<>(position, source),
                                new com.github.difflib.patch.Chunk<>(position, target));
                        break;
                    case "DELETE":
                        delta = new com.github.difflib.patch.DeleteDelta<>(
                                new com.github.difflib.patch.Chunk<>(position, source),
                                null);
                        break;
                    case "INSERT":
                        delta = new com.github.difflib.patch.InsertDelta<>(
                                null,
                                new com.github.difflib.patch.Chunk<>(position, target));
                        break;
                    default:
                        continue;
                }
                patch.addDelta(delta);
            }

            List<String> result = DiffUtils.patch(baseList, patch);
            return String.join("\n", result);
        } catch (PatchFailedException e) {
            log.error("应用diff失败: {}", e.getMessage());
            return baseText;
        } catch (Exception e) {
            log.error("解析diff文本失败: {}", e.getMessage());
            return baseText;
        }
    }

    public String computeTitleDiff(String oldTitle, String newTitle) {
        return computeDiff(oldTitle, newTitle);
    }

    public String computeContentDiff(String oldContent, String newContent) {
        return computeDiff(oldContent, newContent);
    }

    public String applyTitleDiff(String baseTitle, String diffTitle) {
        return applyDiff(baseTitle, diffTitle);
    }

    public String applyContentDiff(String baseContent, String diffContent) {
        return applyDiff(baseContent, diffContent);
    }
}
