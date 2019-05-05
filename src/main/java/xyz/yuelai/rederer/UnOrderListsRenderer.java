package xyz.yuelai.rederer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 无序列表渲染器
 */
public class UnOrderListsRenderer implements MDRenderer {
    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        while (matcher.find()){
            String group = matcher.group();
            String s = split(group);
            matcher.appendReplacement(result, s);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 对*，-，+进行分割，适合以下情况
     * * list
     * * list
     * - list
     * - list
     * + list
     * + list
     * 会被分割为
     * <ul>
     *     <li>list</li>
     *     <li>list</li>
     * </ul>
     * <ul>
     *      <li>list</li>
     *      <li>list</li>
     * </ul>
     * <ul>
     *     <li>list</li>
     *     <li>list</li>
     * </ul>
     * @param group 检测到的包含无序列表的文本段
     * @return  渲染完成的html文本
     */
    private String split(String group) {
        StringBuilder sb = new StringBuilder();
        // 对*，-，+进行区分，分别对应ul
        Pattern pattern1 = Pattern.compile("(\\*|\\-|\\+) +([^\\-\\+\\*]+\\1)+ +.*",Pattern.MULTILINE);
        // 对上一步得到的ul提取li
        Pattern pattern2 = Pattern.compile("[\\*\\-\\+] +[^\\*\\-\\+]+",Pattern.MULTILINE);
        Matcher matcher = pattern1.matcher(group);
        while (matcher.find()){
            String group1 = matcher.group();
            sb.append("<ul>");
            Matcher matcher2 = pattern2.matcher(group1);
            while (matcher2.find()){
                String group2 = matcher2.group();
                String format = String.format("<li>%s</li>", group2.substring(2).replaceAll("\n","").trim());
                sb.append(format);
            }
            sb.append("</ul>").append("\n");
        }
        return sb.toString();
    }
}
