package main.util;

import java.io.*;

/**
 * Created by mashuai on 16/4/21.
 */
public class FileUtil {

    public static BufferedReader getReader(String path,String charSet) throws Exception {
        File file=new File(path);
        FileInputStream fis=new FileInputStream(file);
        BufferedReader br=new BufferedReader(new InputStreamReader(fis,charSet));
        return br;
    }
}
