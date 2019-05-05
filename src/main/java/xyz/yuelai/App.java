package xyz.yuelai;

import java.io.*;
import java.net.URISyntaxException;

public class App {

    public static void main( String[] args ){
        File file = null;
        try {
            file = new File(App.class.getResource("/test.md").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {

            MDParser parser = new MDParser(file);

            long begin = System.currentTimeMillis();
            String body = parser.conventHTML();
            System.out.println(System.currentTimeMillis() - begin);

            System.out.println(body);
//            outPutHTML(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 输出到html文件
     * @param body
     */
    private static void outPutHTML(String body){
        try(FileWriter fw = new FileWriter("text.html");BufferedWriter writer = new BufferedWriter(fw)) {
            String s = String.format("<!doctype html><html><head><meta charset=\"UTF-8\"></head><body>%s</body></html>", body);
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
