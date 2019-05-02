package xyz.yuelai.rederer;

import xyz.yuelai.util.RegexUtil;

import java.util.regex.Matcher;

/**
 * a链接渲染器
 */
public class LinkRenderer implements MDRenderer {
    private StringBuffer result = new StringBuffer();
    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){

            String group = matcher.group();

            // 跳过图片链接
            if(group.contains("![")){
                continue;
            }

            String text = RegexUtil.firstMatch("\\[.*\\]", group);
            String href = RegexUtil.firstMatch("(?>\\() *[^\\(\\) ]+", group);
            String title = RegexUtil.firstMatch(" \".*\"", group);

            text = text == null ? "" : text.substring(1, text.length() - 1).trim();
            title = title == null ? "" : title.replace("\"", "").trim();
            href = href == null ? "" : href.substring(1).trim();

            String link = String.format("<a href=\"%s\" title=\"%s\">%s</a>", href, title, text);

            matcher.appendReplacement(result,link);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
