package main.bayes;

import main.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mashuai on 16/6/6.
 */
public class Bayes {

    private static final String filePath="/Users/mashuai/fetureChooseDataSet/dataSet3.csv";
    private static  BufferedReader br=null;
    //十折交叉验证
    private static final int iter=10;
    private static List<String>fName=new ArrayList<String>();
    private static List<ArrayList<Integer>> dataSet=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> dataSet0=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> dataSet1=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet1=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet2=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet3=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet4=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet5=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet6=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet7=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet8=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet9=new ArrayList<ArrayList<Integer>>();
    private static List<ArrayList<Integer>> sampleSet10=new ArrayList<ArrayList<Integer>>();

    public static void main(String[] args) throws Exception {
        br= FileUtil.getReader(filePath,"utf-8");
        getDataSet();
        classifySample(dataSet);
//        System.out.println("DataSet:");
//        for(ArrayList<Integer> d:dataSet){
//            System.out.println(d);
//        }

//        crossalidation(iter);
//        for(int i=1;i<=iter;i++){
            sampleSet1=classifySubSampleSet();
//        }
        System.out.println(sampleSet1.size());
    }

    private static List<ArrayList<Integer>> classifySubSampleSet() {
        int count=1;
        ArrayList<Integer>sample=null;
        List<ArrayList<Integer>>sampleSet=new ArrayList<ArrayList<Integer>>();
        while (count<=129){
            if(count%4!=0){
                sample=getRandomSample(dataSet0);
            }else{
                sample=getRandomSample(dataSet1);
            }
            sampleSet.add(sample);
            System.out.println(sample);
            count++;
        }
        return sampleSet;
    }

    private static void classifySample(List<ArrayList<Integer>> dataSet) {
        for(ArrayList<Integer>d:dataSet){
            if(d.get(0)==0){
                dataSet0.add(d);
            }else{
                dataSet1.add(d);
            }
        }
        System.out.println("dataSet:"+dataSet.size());
        System.out.println("dataSet0:"+dataSet0.size());
        System.out.println("dataSet1:"+dataSet1.size());
    }

    private static void getDataSet() {
        String line="";
        int index=0;
        try {
            while((line=br.readLine())!=null){
                if(index==0){
                    String featureName[]=line.split(",");
                    for(int i=0;i<featureName.length;i++){
                        fName.add(featureName[i]);
                    }
                    System.out.println("feature Name:");
                    System.out.println(fName);
                    index++;
                }else{
                    ArrayList<Integer>feature=dealLine(line);
                    dataSet.add(feature);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer> dealLine(String line) {
        ArrayList<Integer>feature=new ArrayList<Integer>();
        String re[]=line.split(",");
        for(int i=0;i<re.length;i++){
            feature.add(Integer.parseInt(re[i]));
        }
//        System.out.println(feature);
        return feature;
    }

    private static ArrayList<Integer> getRandomSample(List<ArrayList<Integer>> dataSet) {
        Random rand=new Random(System.currentTimeMillis());
        ArrayList<Integer>sample=new ArrayList<Integer>();
        int index=rand.nextInt(dataSet.size());
        sample=dataSet.get(index);
        System.out.println("sample: " + sample.get(0).intValue());
        return sample;
    }
    private static boolean isZeroClassfy(ArrayList<Integer> sample) {
        boolean isZero=false;
        if(sample.get(0).intValue() == 0){
            isZero=true;
        }
        return isZero;
    }
}
