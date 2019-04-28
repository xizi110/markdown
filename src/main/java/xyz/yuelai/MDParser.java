package xyz.yuelai;

import xyz.yuelai.rederer.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDParser {

    public static final Map<Class<? extends MDRenderer>, String> regex = new HashMap<Class<? extends MDRenderer>, String>(){{

        /**
         * H1~H6标题，"### 标题3"或"### 标题3 ###"
         */
        put(HeadingRenderer.class, "^ *(#{1,6}) +\\S*( +)*\\1?$");

        /**
         * 空白行，一次回车一个空白行
         */
        put(BlankLineRenderer.class, "^\\n");

        /**
         * 行内代码块，嵌入文本中
         * 这是行内的代码`hello`或者是这样```hello```,都算是行内代码块
         */
        put(InlineCodeRenderer.class, "(`{1,}).*([^\\n]+?)([^\\n])\\1");

        /**
         *  块状代码块，需开新行
         *  ```
         *     public static void main(){
         *         System.out.println("Hello World !);
         *    }
         *  ```
         */
        put(BlockCodeRenderer.class, "(^`{3,})([\\s\\S]*?)\\1");

        /**
         * a链接，暂不支持指定跳转方式
         */
        put(LinkRenderer.class, "(\\[.+]\\([^)]+\\))");

        /**
         * 普通图片<img src=""/> =====> ![]()
         * 链接图片<a href=""><img src=""/></a>  =====> [![]()]()
         */
        put(ImageLinkRenderer.class, "(\\[? *!\\[([^\\]])*\\]\\(([^\\)])*\\) *\\]?(\\([^\\)]*\\))?)");
    }};

    /**
     * 对markdown文本进行解析，并调用适当的渲染器进行渲染
     * @param mdText    初始markdown文本
     * @return  转换问html的文本
     */
    public static String conventHTML(String mdText){

        Set<Map.Entry<Class<? extends MDRenderer>, String>> entries = regex.entrySet();

        for (Map.Entry<Class<? extends MDRenderer>, String> entry : entries) {

            if(mdText == null || mdText.isEmpty()){
                return mdText;
            }

            Pattern pattern = Pattern.compile(entry.getValue(),Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(mdText);
            try {
                MDRenderer renderer = entry.getKey().newInstance();
                String rendered = renderer.render(matcher);
                if(rendered == null) continue;
                mdText = rendered;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return paragraphRender(mdText);
    }

    /**
     * 在所有markdown标记渲染为html后,不属于markdown标记且不属于html语句的的语句使用<p></p>进行包括
     * @param html  经过所有渲染器渲染后的html语句
     * @return  用<p></p>包括之后的html语句
     */
    private static String paragraphRender(String html){
        String regex = "^^(?!^<\\w+ *[^<>]*>).*";
        Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(html);
        String rendered = new ParagraphRenderer().render(matcher);
        return rendered == null ? html : rendered;
    }


}
