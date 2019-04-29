package xyz.yuelai.rederer;

import xyz.yuelai.util.RegexUtil;

import java.util.regex.Matcher;

/**
 * 图片链接渲染器
 */
public class ImageLinkRenderer implements MDRenderer {

    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group().trim();

            String alt;
            String src;
            String href;
            String replacement = "";

            src = RegexUtil.firstMatch("(?>\\() *[^\\(\\) ]+", group).substring(1).trim();
            alt = RegexUtil.firstMatch("(?<=\\!\\[)[^\\[\\]]+", group).trim();

            if(group.startsWith("!")){ // 普通图片

                replacement = String.format("<img alt = \"%s\" src = \"%s\"/>", alt, src);

            }else if (group.startsWith("[")){ // 链接图片

                href = group.substring(group.lastIndexOf("]") + 1).trim();
                replacement = String.format("<a href = \"%s\"><img alt = \"%s\" src = \"%s\"/></a>",
                        href.substring(1,href.length() - 1).trim(), alt, src);

            }

            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
