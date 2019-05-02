package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * 粗体强调渲染器
 */
public class StrongRenderer implements MDRenderer {

    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            System.out.println(group);
            String s = String.format("<Strong>%s</Strong>", group.substring(2, group.length() - 2));
            matcher.appendReplacement(result, s);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
