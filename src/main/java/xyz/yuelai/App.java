package xyz.yuelai;

import java.io.*;

public class App {


    public static void main( String[] args ){
        App app = new App();
        // 读取markdown文件
        String mdText = app.readMDText();

        long begin = System.currentTimeMillis();
        // 开始解析
        String body = MDParser.conventHTML(mdText);
        System.out.println(System.currentTimeMillis() - begin);

        // 输出为html文件
        app.outPutHTML(body);
    }

    private void outPutHTML(String body){
        try(FileWriter fw = new FileWriter("text.html");BufferedWriter writer = new BufferedWriter(fw)) {
            String s = String.format("<!doctype html><html><head><meta charset=\"UTF-8\"></head><body>%s</body></html>", body);
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String readMDText(){
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getResource("/test.md").getFile())))) {

            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
