package xyz.yuelai.rederer;

import java.util.regex.Matcher;

public class ParagraphRenderer implements MDRenderer {

    private static final String FORMAT = "<p>%s</p>";

    @Override
    public String render(Matcher matcher) {
        String result = null;
        while (matcher.find()){
            String group = matcher.group();
            String format = String.format(FORMAT, group);
            result = matcher.replaceFirst(format);
            matcher.reset(result);
        }
        return result;
    }
}
