package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * 斜体强调渲染器
 */
public class EmphasisRenderer implements MDRenderer {

    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            String s = String.format("<em>%s</em>", group.substring(1, group.length() - 1));
            matcher.appendReplacement(result, s);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
