package xyz.yuelai;

import java.io.*;

public class App {


    public static void main( String[] args ){
        String mdText = new App().readMDText();
        long begin = System.currentTimeMillis();
        String parse = MDParser.conventHTML(mdText);
        System.out.println(System.currentTimeMillis() - begin);
        System.out.println(parse);
    }

    private String readMDText(){
        StringBuilder sb = new StringBuilder();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getResource("/test.md").getFile())));
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
