package xyz.yuelai.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    /**
     * 返回第一个匹配指定正则表达式的内容
     * @param regex 正则表达式
     * @param text  待匹配的文本
     * @return  匹配到的文本，如果未匹配，则返回null
     */
    public static String firstMatch(String regex, String text){

        if(regex == null || text == null){
            return null;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : null;
    }
}
