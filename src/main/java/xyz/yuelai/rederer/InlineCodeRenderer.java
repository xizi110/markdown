package xyz.yuelai.rederer;

import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Matcher;

/**
 * 行内代码渲染器
 */
public class InlineCodeRenderer implements MDRenderer {

    private static final String FORMAT = "<code>%s</code>";
    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()) {
            String group = matcher.group();
            // 逐对首尾删除 `
            while (group.startsWith("`") && group.endsWith("`")){
                group = group.substring(1, group.length() - 1);
            }
            String s = String.format(FORMAT, StringEscapeUtils.escapeHtml4(group.trim()));
           matcher.appendReplacement(result, s);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
