package xyz.yuelai.rederer;

import java.util.regex.Matcher;

/**
 * markdown渲染器接口，实现此接口定制指定标签的渲染器
 */
public interface MDRenderer {

    String render(Matcher matcher);

}
