package xyz.yuelai;

import xyz.yuelai.rederer.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * markdown文本解析器，使用正则表达式对markdown文本进行解析
 * 将解析后得到的markdown标签传递给对应标签的渲染器进行渲染
 *
 */
public class MDParser {

    private List<String> block = new LinkedList<>();

    public MDParser(File file) throws IOException {

        // 空指针
        if(file == null){
            throw new NullPointerException();
        }

        // 文件不存在
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        FileReader fileReader = new FileReader(file);
        readMDText(fileReader);
    }

    public MDParser(FileReader reader){
        // 空指针
        if(reader == null){
            throw new NullPointerException();
        }
        readMDText(reader);
    }

    /**
     * 读取markdown文本
     * 它会返回一个list，里面包含被空行分隔文本段，由markdown文本的特性，我么们对markdown文本进行分段处理
     * 分段原则，是根据回车也就是空白行(不包含空格或制表符)进行分割的。被分割的每一块，是一个整体，对这些文本块
     * 进行下一步的具体标签解析
     * @return  markdown文本各个文本块
     * @param reader  markdown文件
     */
    private void readMDText(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            boolean isInCode = false;
            while ((line = bufferedReader.readLine()) != null){
                // 判断是否进入了代码段
                if(line.startsWith("```") && !line.substring(3).endsWith("```")){
                    isInCode = !isInCode;
                }
                // 如果是空白行并且不在代码段内，说明就该在此进行分割
                // 如果是空白行但在代码段内，则按正常文本处理
                if(line.length() == 0 && !isInCode){
                    if(sb.length() > 0){
                        block.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    continue;
                }
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 各种标签的(渲染器-正则表达式)键值对。
     *
     * 个别标签的渲染顺序可能会影响最终的渲染结果
     * 所以选择LinkedHashMap来保证渲染顺序
     */
    private static final Map<Class<? extends MDRenderer>, String> regex = new LinkedHashMap<Class<? extends MDRenderer>, String>(){{

        /*
         * H1~H6标题，"### 标题3"或"### 标题3 ###"
         */
        put(HeadingRenderer.class, "^ *(#{1,6}) +\\S*( +)*\\1?$");

        /*
         * 空白行，一次回车一个空白行
         */
//        put(BlankLineRenderer.class, "^\\n");

        /*
         * 行内代码块，嵌入文本中
         * 这是行内的代码`hello`或者是这样```hello```,都算是行内代码块
         */
        put(InlineCodeRenderer.class, "(`{1,}).+([^\\n]+?)([^\\n])\\1");

        /*
         *  块状代码块，需开新行
         *  ```
         *     public static void main(){
         *         System.out.println("Hello World !);
         *    }
         *  ```
         */
        put(BlockCodeRenderer.class, "^(`{3,})([\\s\\S]*?)\\1$");

        /*
         * a链接，暂不支持指定跳转方式
         * 由于和图片链接很相似，所以多向前包含一位，作为区分位
         */
        put(LinkRenderer.class, ".?(\\[.+\\]\\([^)]+\\))");

        /*
         * 普通图片<img src=""/> =====> ![]()
         * 链接图片<a href=""><img src=""/></a>  =====> [![]()]()
         */
        put(ImageLinkRenderer.class, "(\\[? *!\\[([^\\]])*\\]\\(([^\\)])*\\) *\\]?(\\([^\\)]*\\))?)");

        /*
         * 水平线，简陋匹配，在renderer中需要详细检查
         * *** 或 --- 或 * * * 或 - - -
         */
        put(HorizontalRenderer.class, "^(\\*|\\-)[\\* *\\-]{2,}$");

        /*
         * 粗体<Strong></Strong>被包括
         * **粗体**或者__粗体__
         */
        put(StrongRenderer.class, "(\\*{2}|_{2})[^\\*]{1}.+?\\1");

        /*
         * 斜体强调,被<em></em>包括
         * *斜体*或者_斜体_
         */
        put(EmphasisRenderer.class, "(\\*|_)[^\\*]{1}.+?\\1");

        /*
         * 无序列表,
         * 匹配以下标记：
         * * list
         * - list
         * + list
         */
        put(UnOrderListsRenderer.class, "[\\*\\-\\+] +.+[\\s\\S]+");
    }};

    public String conventHTML(){
        StringBuilder sb = new StringBuilder();
        for (String s : block) {
            String html = conventHTML(s);
            sb.append(html);
        }
        return sb.toString();
    }

    /**
     * 对markdown文本进行解析，并调用适当的渲染器进行渲染
     * @param mdText    初始markdown文本
     * @return  转换问html的文本
     */
    private String conventHTML(String mdText){

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
            } catch (InstantiationException | IllegalAccessException e) {
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
        String regex = "^(?!^<\\w+ *[^<>]*>).*";
        Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(html);
        String rendered = new ParagraphRenderer().render(matcher);
        return rendered == null ? html : rendered;
    }


}
