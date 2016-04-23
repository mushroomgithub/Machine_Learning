package main.FeatureChoose;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FeatureThermostabiliy {
    int thermostabiliy;
    int record;
    FeatureThermostabiliy(int record, int thermostabiliy) {
        this.record = record;
        this.thermostabiliy = thermostabiliy;
    }
}

class FeatureWeight {
    String feature;
    double weight;
    FeatureWeight(String feature, double weight) {
        this.feature = feature;
        this.weight = weight;
    }
}

public class InformationGain {

    /**
     *  ˝æ›∏Ò Ω£® ˝æ›ø‚±Ì∏Ò Ωµƒ◊™÷√∏Ò Ω£©
     * ther 1 0 1 0 1
     * A    0 0 0 0 0
     * B    0 0 0 0 0
     */
    public static void main(String[] args) throws IOException {
        InformationGain ig = new InformationGain();
        List<String> list = ig.getIG(ig.readFile("/Users/mashuai/fetureChooseDataSet/feature.txt"));
        for (String e : list) {
            System.out.println(e);
        }
    }

    private List<String> readFile(String fileRoadreader) throws IOException {
        FileReader fr = new FileReader(fileRoadreader);
        BufferedReader br = new BufferedReader(fr);
        List<String> list = new ArrayList<String>();
        String temp = new String();
        while ((temp = br.readLine()) != null) {
            list.add(temp);
        }
        br.close();
        fr.close();
        return list;
    }

    private List<String> getIG(List<String> list) {
        List<String> featureSelection = new ArrayList<String>();
        int[] thermostabiliy = getThrmostbility(list.get(0));
        list.remove(0);
        String[] featureName = getFeatureName(list);
        List<ArrayList<Integer>> feature = StringToList(list);
        double[] informationGain = new double[featureName.length];
        double info = getInfo(thermostabiliy);
        int index = 0;
        for (ArrayList<Integer> e : feature) {
            informationGain[index++] = info - getInfoFeature(e, thermostabiliy);
        }
        FeatureWeight[] fw = new FeatureWeight[featureName.length];
        for (int i = 0; i < featureName.length; i++) {
//			test
//			System.out.println(featureName[i] + "-" + informationGain[i]);
            fw[i] = new FeatureWeight(featureName[i], informationGain[i]);
        }
        fw = sort(fw);
        for (int i = 0; i < 35; i++) {
//			test
			System.out.println(fw[i].feature + "-" + fw[i].weight);
            featureSelection.add(fw[i].feature);
        }
        return featureSelection;
    }

    private FeatureWeight[] sort(FeatureWeight[] fw) {
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

    private double getInfoFeature(ArrayList<Integer> list, int[] ther) {
        List<FeatureThermostabiliy> ft = new ArrayList<FeatureThermostabiliy>();
        for (int i = 0; i < list.size(); i++) {
            ft.add(new FeatureThermostabiliy(list.get(i), ther[i]));
        }
        Collections.sort(list);
        int len = list.get(list.size() - 1);
        int N = ft.size();
        double infoFeature = 0.0;
        for (int i = 1; i <= len; i++) {
            int thermophilic = 0;
            int mesophilic = 0;
            for (int j = 0; j < ft.size(); j++) {
                if (ft.get(j).record == i) {
                    if (ft.get(j).thermostabiliy == 1) {
                        thermophilic++;
                    } else {
                        mesophilic++;
                    }
                }
            }
            int num = thermophilic + mesophilic;
//			test
//			System.out.println(num);
            infoFeature += ((double)num/(double)N *
                    (-1 * (double)thermophilic/(double)num * Math.log((double)(thermophilic == 0 ? 1 : thermophilic)/(double)num)
                            + -1 *(double)mesophilic/(double)num * Math.log((double)(mesophilic == 0 ? 1 : mesophilic)/(double)num)));
//			test
//			System.out.println(infoFeature);
        }
        return infoFeature;
    }

    private int[] getThrmostbility(String s) {
        String[] temp = s.split("\t");
        int[] array = new int[temp.length - 1];
        int index = 0;
        while(index < array.length) {
            array[index] = Integer.parseInt(temp[index + 1]);
            index++;
        }
        return array;
    }

    private String[] getFeatureName(List<String> list) {
        String[] s = new String[list.size()];
        int index = 0;
        for (String e : list) {
            s[index++] = e.split("\t")[0].trim();
        }
        return s;
    }

    private List<ArrayList<Integer>> StringToList(List<String> list) {
        List<ArrayList<Integer>> feature = new ArrayList<ArrayList<Integer>>();
        for (String e : list) {
            String[] tempString = e.split("\t");
            ArrayList<Integer> tempList = new ArrayList<Integer>();
            for (int i = 1; i < tempString.length; i++) {
                tempList.add(Integer.parseInt(tempString[i].trim()));
            }
            feature.add(tempList);
        }
        return feature;
    }

    private double getInfo(int[] Thermostabiliy) {
        int thermophilic = 0;
        int mesophilic = 0;
        int len = Thermostabiliy.length;
        for (int i = 0; i < len; i++) {
            if(Thermostabiliy[i] == 1) {
                thermophilic++;
            } else {
                mesophilic++;
            }
        }
//		test
//		System.out.println(thermophilic + " " + mesophilic + " " + len);
        double info = ((double)thermophilic/(double)len) * Math.log((double)(thermophilic == 0 ? 1 : thermophilic)/(double)len)
                + ((double)mesophilic/(double)len) * Math.log((double)(mesophilic == 0 ? 1 : mesophilic)/(double)len);
//		test
//		System.out.println(-1 * info);
        return -1 * info;
    }

}
