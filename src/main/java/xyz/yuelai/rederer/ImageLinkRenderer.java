package xyz.yuelai.rederer;

import java.util.regex.Matcher;

public class ImageLinkRenderer implements MDRenderer {
    @Override
    public String render(Matcher matcher) {
        String result = null;
        while (matcher.find()){
            String group = matcher.group().trim();
            String alt;
            String src;
            String href;
            String replacement = null;
            src = group.substring(group.indexOf("(") + 1, group.indexOf(")")).trim();
            if(group.startsWith("!")){ // 普通图片
                alt = group.substring(group.indexOf("[") + 1, group.indexOf("]"));
                replacement = String.format("<img alt = \"%s\" title = \"%s\" src = \"%s\"/>", alt,alt,src);
            }else if (group.startsWith("[")){ // 链接图片
                alt = group.substring(group.indexOf("!") + 2, group.indexOf("]"));
                href = group.substring(group.lastIndexOf("(") + 1,group.lastIndexOf(")")).trim();
                replacement = String.format("<a href = \"%s\"><img alt = \"%s\" title = \"%s\" src = \"%s\"/></a>", href,alt,alt,src);
            }
            if(replacement != null) {
                result = matcher.replaceFirst(replacement);
                matcher.reset(result);
            }
        }
        return result;
    }
}
