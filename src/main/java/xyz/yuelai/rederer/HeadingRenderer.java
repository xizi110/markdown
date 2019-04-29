package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * H1 ~ H6标题渲染器
 */
public class HeadingRenderer implements MDRenderer {

    private StringBuffer result = new StringBuffer();

    @Override
    public String render(Matcher matcher) {
        StringBuilder sb = new StringBuilder();
        while (matcher.find()){
            String group = matcher.group().trim();
            String level_ = group.substring(0, group.indexOf(" "));
            int level = level_.length();
            String heading;
            String heading_end;
            switch (level){
                case 1:{
                    heading = "<h1>";
                    heading_end = "</h1>";
                    break;
                }
                case 2:{
                    heading = "<h2>";
                    heading_end = "</h2>";
                    break;
                }
                case 3:{
                    heading = "<h3>";
                    heading_end = "</h3>";
                    break;
                }
                case 4:{
                    heading = "<h4>";
                    heading_end = "</h4>";
                    break;
                }
                case 5:{
                    heading = "<h5>";
                    heading_end = "</h5>";
                    break;
                }
                case 6:{
                    heading = "<h6>";
                    heading_end = "</h6>";
                    break;
                }
                default: {
                    sb.append("<p>").append(group).append("</p>").append("\r");
                    String s = matcher.replaceFirst(sb.toString());
                    matcher.reset(s);
                    sb.delete(0,sb.length());
                    continue;
                }
            }
            // 去掉开头#
            group = group.substring(level);
            // 如有结尾#，且可以去掉则也去掉
            group = group.replaceAll(" +#{1,6}$", "");
            sb.append(heading).append(group.trim()).append(heading_end).append("\r");

            matcher.appendReplacement(result, sb.toString());
            sb.delete(0,sb.length());
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
