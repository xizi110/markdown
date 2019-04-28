package xyz.yuelai.rederer;

import java.util.regex.Matcher;

public class InlineCodeRenderer implements MDRenderer {

    private static final String FORMAT = "<code>%s</code>";

    @Override
    public String render(Matcher matcher) {
        String result = null;
        while (matcher.find()) {
            String group = matcher.group();
            // 逐对首尾删除 `
            while (group.startsWith("`") && group.endsWith("`")){
                group = group.substring(1, group.length() - 1);
            }
            String s = String.format(FORMAT, group.trim());
            // 转换为html标签
            result = matcher.replaceFirst(s);
            // 重置解析文本
            matcher.reset(result);
        }
        return result;
    }
}
