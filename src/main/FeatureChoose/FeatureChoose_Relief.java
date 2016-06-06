package main.FeatureChoose;

import java.io.*;
import java.util.*;

/**
 * Created by mashuai on 16/4/21.
 */

class ClassfySample {
    private List<Double[]> dataSet=null;
    public ClassfySample(List<Double[]> dataSet) {
        this.dataSet=dataSet;
    }

    public  void classfyDataSet(List<Double[]> dataSet0,List<Double[]> dataSet1){/*数据集分类*/
        for(Double[] data:dataSet){
            for(int i=0;i<data.length;i++){
                int classfy=data[i].intValue();
                if(classfy == 0){
                    dataSet0.add(data);
                    break;
                }else {
                    dataSet1.add(data);
                    break;
                }
            }
        }
    }

}

public class FeatureChoose_Relief {

    private static BufferedReader reader=null;
    private static List<String> lines=new ArrayList<String>();
    private static List<Double[]> dataSet=new ArrayList<Double[]>();
    private static List<Double[]> dataSet0=new ArrayList<Double[]>();
    private static List<Double[]> dataSet1=new ArrayList<Double[]>();

    private static int m=200;/*抽样次数*/
    private static int N=430;
    private static int select=35;

    private static String [] featureName=new String[N];/*样本430个特征名字集合*/
    private static Double [] feature_weight =new Double[N];/*样本430个特征权重集合*/
    private static Double[] R=new Double[N];
    private static Double[] H=new Double[N];
    private static Double[] M=new Double[N];

    private static ClassfySample classfySample=null;

    public static void main(String[] args) throws Exception {
        File file=new File("/Users/mashuai/fetureChooseDataSet/dataSet1.csv");
        FileInputStream fis=new FileInputStream(file);
        reader=new BufferedReader(new InputStreamReader(fis,"utf-8"));
        Arrays.fill(feature_weight,0.0);
        getDataSet();
        getTop35Feture_Weight(dataSet);
    }

    private static void getTop35Feture_Weight(List<Double[]> dataSet) {
        getClassfySample();/*对数据集进行划分样本0-1空间*/
        for(int i=0;i<m;i++){
//            System.out.println("第"+(i+1)+"次抽样结果:");
            R=getRandomSample(dataSet);
            if(isZeroClassfy(R)){
                H=getNNSample(dataSet0,R);
//                System.out.println("H: " + H[0].intValue());
                M=getNNSample(dataSet1,R);
//                System.out.println("M: " + M[0].intValue());
//                System.out.println("<----------->");
            }else {
                H=getNNSample(dataSet1,R);
//                System.out.println("H: "+ H[0].intValue());
                M=getNNSample(dataSet0,R);
//                System.out.println("M: " + M[0].intValue());
//                System.out.println("<----------->");
            }
            for(int j=0;j<N;j++){
                double temp_RH=diff(R,H,j+1);
                double temp_RM=diff(R,M,j+1);
                feature_weight[j]= feature_weight[j]-temp_RH/m+temp_RM/m;
            }
        }

        FeatureWeight fw[]=new FeatureWeight[N];
        getTop35(fw);

    }

    private static void getTop35(FeatureWeight[] fw) {
        for(int k=0;k<N;k++){
            fw[k]=new FeatureWeight(featureName[k], feature_weight[k]);
        }
        sort(fw);
        for(int j=0;j<select;j++){
            System.out.println("top"+(j+1)+":"+fw[j].feature+" :-----> "+fw[j].weight);
        }
    }

    private static FeatureWeight[] sort(FeatureWeight[] fw) {
        boolean flag = true;
        for (int i = 0; i < fw.length && flag == true; i++) {
            flag = false;
            for (int j = i + 1; j < fw.length; j++) {
                if (fw[i].weight < fw[j].weight) {
                    FeatureWeight temp = fw[i];
                    fw[i] = fw[j];
                    fw[j] = temp;
                    flag = true;
                }
            }
        }
        return fw;
    }

    private static void getClassfySample() {
        classfySample=new ClassfySample(dataSet);
        classfySample.classfyDataSet(dataSet0,dataSet1);
//        int count1=0,count2=0;
//        for(Double[] d1:dataSet0) {
//            count1++;
//        }
//        System.out.println("数据集中有"+count1+"个样本类别为0的样本");
//
//        for(Double[] d2:dataSet1) {
//            count2++;
//        }
//        System.out.println("数据集中有"+count2+"个样本类别为1的样本");
//        System.out.println();
    }

    private static void getDataSet() throws IOException {
        String line=null;
        int i=0;
        try {
            while ((line=reader.readLine())!=null){
                lines.add(line);
                if(i != 0){
                    stringToDouble(line);
                }else {
                    featureName=getFeatureName(line);
                    i++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
    }

    private static String[] getFeatureName(String line) {
        String [] fName=new String[N];
        for(int i=0;i<N;i++){
            fName[i]=line.split(",")[i+1].trim();
        }
        return fName;
    }

    private static double diff(Double[] R, Double[] H,int index) {/*两个特征之间的距离关系*/
        return (R[index]-H[index]);
    }

    private static Double[] getRandomSample(List<Double[]> dataSet) {
        Random rand=new Random(System.currentTimeMillis());
        R=new Double[N+1];
        int index=rand.nextInt(dataSet.size());
        R=dataSet.get(index);
//        System.out.println("R: " + R[0].intValue());
        return R;
    }

    private static boolean isZeroClassfy(Double[] R) {
        boolean isZero=false;
        if(R[0].intValue() == 0){
            isZero=true;
        }
        return isZero;
    }

    private static Double[] getNNSample(List<Double[]> dataSet,Double[] R){
        Double[] sample=new Double[R.length];
        Arrays.fill(sample,0.0);
        double nnDistance=Double.MAX_VALUE;
        int index=-1;
        for(Double[] temp:dataSet){
            double sum=0;
            for(int i=1;i<R.length;i++){
                sum+=Math.pow((R[i]-temp[i]),2);
            }
            if(nnDistance>sum){
                nnDistance=Math.min(nnDistance,Math.sqrt(sum));
                index=dataSet.indexOf(temp);
            }
        }
        sample=dataSet.get(index);
        return sample;
    }
    private static void stringToDouble(String line){
        String re[]=line.split(",");
        Double data[]=new Double[re.length];
        for(int i=0;i<re.length;i++){
            data[i]=Double.parseDouble(re[i]);
        }
        dataSet.add(data);
    }

}
