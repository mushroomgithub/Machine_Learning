package main.BayesClassifer;

import main.util.FileUtil;
import main.util.Options;
import main.util.Transpose;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by MoGu on 2016/6/5.
 */

public class Bayes {
    private static BufferedReader reader=null;
    private static List<String> subDataSet=new ArrayList<String>();
    private static List<String> subDataSet0 = new ArrayList<String>();
    private static List<String> subDataSet1 = new ArrayList<String>();
    private static List<Set<String>> set=new ArrayList<Set<String>>();
    private static Map<String,Integer> featurePrab=new HashMap<String, Integer>();
    private static final String path_dataset = "F:/JavaProgram/Bayes/file/dataset.txt";
    private static final String[] path_subset = {
            "./src/main/file/subset_1.txt",
            "./src/main/file/subset_2.txt",
            "./src/main/file/subset_3.txt",
            "./src/main/file/subset_4.txt",
            "./src/main/file/subset_5.txt",
            "./src/main/file/subset_6.txt",
            "./src/main/file/subset_7.txt",
            "./src/main/file/subset_8.txt",
            "./src/main/file/subset_9.txt",
            "./src/main/file/subset_10.txt"
    };
    public static void main(String[] args) throws Exception {
        String filePath="./src/main/file/subset_1.txt";
        subDataSet=getDataSet(filePath);
        split_0_1();
        System.out.println("size of subDataSet=" + subDataSet.size());
        System.out.println("size of subDataSet0=" + subDataSet0.size());
        System.out.println("size of subDataSet1="+subDataSet1.size());
        List<String>transposeList=Transpose.transpose(subDataSet);
        List<String>transposeList0=Transpose.transpose(subDataSet0);
        List<String>transposeList1=Transpose.transpose(subDataSet1);
        for(String temp:transposeList){
            String line[]=temp.split(Options.separator);
            Set<String> t=new HashSet<String>();
            for(int i=0;i<line.length;i++){
                t.add(line[i]);
            }
            set.add(t);
        }

        for(Set<String> t:set){
            System.out.println(t);
        }
        System.out.println("<------------------------------->");
        for(String temp:transposeList0){
            System.out.println(temp);
        }
        System.out.println("<------------------------------->");
        for(String temp:transposeList1){
            System.out.println(temp);
        }
    }

    private static void split_0_1() {
        for (String s: subDataSet) {
            if (s.split(Options.separator)[0].equals("0")) {
                subDataSet0.add(s);
            } else {
                subDataSet1.add(s);
            }
        }
    }
    private static List<String> getDataSet(String filePath) throws Exception {
        reader= FileUtil.getReader(filePath,"UTF-8");
        List<String> subDataSet=new ArrayList<String>();
        String line="";
        while ((line=reader.readLine())!=null){
            subDataSet.add(line);
        }
        return subDataSet;
    }

    private static List<String> transation(List<String> subSampleSet){
        int len1=subSampleSet.size();
        int len2=subSampleSet.get(0).split("\t").length;
        System.out.println("len1="+len1+" len2="+len2);
        String martix[][]=new String[len1][len2];
        for(int i=0;i<len1;i++){
            String temp=subSampleSet.get(i);
            String[] re=temp.split("\t");
            for(int j=0;j<len2;j++){
                martix[i][j]=re[j];
            }
        }
        String result[][]=new String[len2][len1];
        for(int i=0;i<len1;i++){
            for(int j=0;j<len2;j++){
                result[j][i]=martix[i][j];
            }
        }
//        for(int i=0;i<len2;i++){
//            System.out.println(result[i].length+":"+Arrays.asList(result[i]));
//        }
        return null;
    }
    private static void statisticFeature(){

    }
    private static String[] getFeatureName(String path) throws Exception {
        String [] fName=new String[431];
        reader=FileUtil.getReader(path,"utf-8");
        String line="";
        while((line=reader.readLine())!=null){
            fName=line.split("\t");
            break;
        }
        return fName;
    }
}
