package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * 水平线渲染器<hr/>
 */
public class HorizontalRenderer implements MDRenderer {

    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            // 除去空格，包含的*少于3个则不渲染
            if(group.replace(" ","").length() < 3){
                   continue;
            }
            matcher.appendReplacement(result, "<hr/>");
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
