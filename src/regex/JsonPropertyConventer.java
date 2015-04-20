package regex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author huhai
 * @date 2015/4/3 11:29
 */
public class JsonPropertyConventer {
    static void renameJsonProperty(String filePath) throws Exception
    {
        File file = new File(filePath);
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while (null != (line = reader.readLine())){
                if(line.matches(".*\"[0-9a-zA-Z\\_]+\".*")){
                    System.out.println(line.replaceAll("_", "-"));
                }else{
                    System.out.println(line);
                }
            }
            reader.close();
        }
    }

    static void setJsonProperty(String filePath) throws Exception{
        File file = new File(filePath);
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while (null != (line = reader.readLine())){
                String[] parts = line.trim().split("\\s+");
                int pos = line.indexOf("private");
                String blank = line.substring(0, pos);

                if(parts.length == 3){
                    String propertyName = parts[2].replaceAll(";", "");
                    if(propertyName.indexOf("_") >= 0){
                        propertyName = propertyName.replaceAll("_", "-");
                        System.out.println(String.format("%s@JsonProperty(\"%s\")", blank, propertyName));
                    }
                    System.out.println(line);
                }else{
                    throw new RuntimeException("此行不是定义成员变量的：" + line);
                }
            }
            reader.close();
        }
    }

    public static void main(String[] args) throws Exception {
//        renameJsonProperty("D:\\devtools\\idea\\java-exercises\\src\\json\\fields.txt");
        setJsonProperty("D:\\devtools\\idea\\java-exercises\\src\\regex\\setJsonProperties.txt");
    }
}
