package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * 段落渲染器，在所有的markdown标签渲染为html标签后，
 * 剩下的非html标签被默认为段落，使用<p></p>标签包括
 */
public class ParagraphRenderer implements MDRenderer {

    private static final String FORMAT = "<p>%s</p>";
    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            String format = String.format(FORMAT, group);
           matcher.appendReplacement(result, format);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
