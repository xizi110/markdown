package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * 空白行渲染器
 */
public class BlankLineRenderer implements MDRenderer {

    private final String REPLACEMENT = "<p>&nbsp;</p>\n";
    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()) {
            matcher.appendReplacement(result,REPLACEMENT);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
