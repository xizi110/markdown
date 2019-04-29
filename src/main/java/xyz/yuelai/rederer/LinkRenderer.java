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


            // 不包含title
            if(title == null && href != null){
                href = href.substring(1, href.length() - 1).trim();
            }else if (title != null && href != null){   // 包含title
                href = href.substring(1, href.indexOf(title)).trim();
            }

            text = text == null ? "" : text.substring(1, text.length() - 1).trim();
            title = title == null ? "" : title.replace("\"", "").trim();
            href = href == null ? "" : href;

            String link = String.format("<a href=\"%s\" title=\"%s\">%s</a>", href, title, text);

            matcher.appendReplacement(result,link);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
