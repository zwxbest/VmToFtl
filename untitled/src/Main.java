import com.revusky.util.USCavalry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * todo:
 * 1.整理成log4j日志写到控制台
 * 2.patter.txt在外头可配置
 * 3.打包成jar文件
 */
public class Main {

    /**
     * -d 表示转换文件夹內全部带有特定后缀的文件
     * -f表示转换单个文件
     *
     * @param args
     */

    static String cmd, from, to, fromFolder;

    public static void main(String[] args) throws IOException {

        if (args == null || args.length <= 1) {
            usage();
            System.exit(-1);
        }
        cmd = args[0];
        if (cmd.equals("-f")) {
            from = args[1];
            if (args.length == 3) {
                to = args[2];
            } else
                to = null;
            convertOneFile(from, to);

        }
        if (cmd.equals("-d")) {
            convertFolders(args[1], args[2]);
        }

//       convert(from,);
    }

    static void usage() {
        System.err.println("Velocity->FreeMarker conversion tool, version 0.3");
        System.err.println("This is a utility to convert files from Velocity to FreeMarker syntax.");
        System.err.println("Usage:\n");
        System.err.println("convert one file:java -jar vmtoftl.jar -f <fromfile> [<tofile>]\n");
        System.err.println("convert files in folder:java -jar -d vmtoftl.jar <fromfolder> \n");
        System.err.println("This product includes software developed by the");
        System.err.println("Apache Software Foundation (http://www.apache.org/).");
    }

    static void convert(String from, String to) throws FileNotFoundException, IOException {

        InputStreamReader in = new InputStreamReader(new FileInputStream(from));
        USCavalry.main(new String[]{from, to});

        //读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(to), "UTF-8")); //这里可以控制编码
        sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        List<String> replace = new ArrayList<String>();
        List<String> find = new ArrayList<String>();
        readPatternFromTxt(find, replace);
        if (find.size() != replace.size()) {
            System.out.println("the number of regex pattern for repalce is not equal,the \"\" should use thisisnull instead");
        }
        String result = sb.toString();
        for (int i = 0; i < find.size(); i++) {
            result = result.replaceAll(find.get(i), replace.get(i));
        }
        FileWriter fw = new FileWriter(to);
        fw.write(result);
        fw.close();
    }

    //读取正则表达式的替换规则
    static void readPatternFromTxt(List<String> find, List<String> replace) throws FileNotFoundException, IOException {

        BufferedReader br = null;
        StringBuffer sb = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream("pattern.txt"), "UTF-8")); //这里可以控制编码
        sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("###"))
                continue;
            String[] pattern = line.split(",");
            if (pattern.length == 2) {
                find.add(pattern[0]);
                if (pattern[1].equals("thisisnull"))
                    replace.add("");
                else
                    replace.add(pattern[1]);
            }
        }
        System.out.println();

    }

    static void convertFolders(String dirname, String suffix) throws IOException {
        File dir = new File(dirname);
        if (!dir.isDirectory()) {
            System.out.println("the name is not a directory");
            System.exit(-1);
        }
        File[] nornalFile = dir.listFiles(file -> file.isFile() && file.getName().endsWith(suffix));
        File[] dirFile = dir.listFiles(file -> file.isDirectory());
        for (File file : nornalFile) {
            String to = file.getPath().replaceAll("\\." + suffix + "$", "").concat(".ftl");
            convert(file.getPath(), to);
        }
        //递归子目录
        for (File ff : dirFile) {
            convertFolders(ff.getPath(), suffix);
        }
    }

    static void convertOneFile(String from, String to) {

        if (to == null) {

            //去掉后缀
            String noSuffix = from.replaceAll("\\.[a-z0-9A-Z]+$", "");
            to = noSuffix.concat(".ftl");
            try {
                convert(from, to);
            } catch (Exception e) {

            }

        }


    }
}
