package xyz.yuelai.rederer;

import java.util.regex.Matcher;

public class BlankLineRenderer implements MDRenderer {

    private static final String replacement = "<p>&nbsp;</p>\n";

    @Override
    public String render(Matcher matcher) {
        String result = null;
        while (matcher.find()) {
            String s = matcher.replaceFirst(replacement);
            matcher.reset(s);
            result = s;
        }
        return result;
    }
}
