package xyz.yuelai.rederer;

import java.util.regex.Matcher;

public class LinkRenderer implements MDRenderer {
    String result = null;
    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            String title = group.substring(group.indexOf("[") + 1, group.indexOf("]"));
            String href = group.substring(group.indexOf("(") + 1, group.indexOf(")"));
            String link = String.format("<a href=\"%s\" title=\"%s\">%s</a>", href, title, title);
            result = matcher.replaceFirst(link);
            matcher.reset(result);
        }
        return result;
    }
}
