package main.BayesClassifer;

import main.util.FileService;
import main.util.Options;
import main.util.Transpose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InformationGainRatio {

	/**
	 * ���ݸ�ʽ�����ݿ���ʽ��ת�ø�ʽ��
	 * ther 1 0 1 0 1
	 * A    0 0 0 0 0
	 * B    0 0 0 0 0
	 * @throws java.io.IOException
	 */
	
	public void getIGR(List<String> _list) throws IOException {
		List<String> list = Transpose.transpose(_list);
		int[] thermostabiliy = getThrmostbility(list.get(0));
		list.remove(0);
		String[] featureName = getFeatureName(list);
		List<ArrayList<Integer>> feature = StringToList(list);
		double[] informationGainRatio = new double[featureName.length];
		double info = getInfo(thermostabiliy);
		int index = 0;
		for (ArrayList<Integer> e : feature) {
			double[] result = getInfoFeature(e, thermostabiliy);
			informationGainRatio[index++] = (info - result[0]) / result[1];
		}
		FeatureWeight[] fw = new FeatureWeight[featureName.length];
		for (int i = 0; i < featureName.length; i++) {
			fw[i] = new FeatureWeight(featureName[i], informationGainRatio[i]);
		}
		fw = sort(fw);
		List<FeatureWeight> result = new ArrayList<FeatureWeight>();
		System.out.println("Information Gain Ratio:");
		System.out.println("Feature\tweight");
		for (int i = 0; i < Options.num_selection; i++) {
			result.add(fw[i]);
			System.out.println(fw[i].getFeature() + "\t" + fw[i].getWeight());
		}
		List<String> write_data = function(result, Transpose.transpose(_list));
		write_data.remove(0);
		FileService.writeFile(write_data, Options.path_dataset_featureSelection);
	}

	private List<String> function(List<FeatureWeight> result, List<String> list) {
		//select features
		List<String> list_select = new ArrayList<String>();
		list_select.add(list.get(0));
		Set<String> set = new HashSet<String>();
		for (FeatureWeight fw: result) {
			set.add(fw.feature);
		}
		for (String e: list) {
			if (set.contains(e.split(Options.separator)[0])) {
				list_select.add(e);
			}
		}
		return Transpose.transpose(list_select);
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
		double info = ((double)thermophilic/(double)len) * Math.log((double)(thermophilic == 0 ? 1 : thermophilic)/(double)len)  / Math.log(2.0)
				+ ((double)mesophilic/(double)len) * Math.log((double)(mesophilic == 0 ? 1 : mesophilic)/(double)len)  / Math.log(2.0);
		return -1 * info;                                                                                                                                                                                                                     
	}

	private double[] getInfoFeature(ArrayList<Integer> list, int[] ther) {
		List<FeatureThermostabiliy> ft = new ArrayList<FeatureThermostabiliy>();
		for (int i = 0; i < list.size(); i++) {
			ft.add(new FeatureThermostabiliy(list.get(i), ther[i]));
		}
		Collections.sort(list);
		int len = list.get(list.size() - 1);
		int N = ft.size();
		double infoFeature = 0.0;
		double splitInfo = 0.0;
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
			infoFeature += ((double)num/(double)N * 
					(-1 * (double)thermophilic/(double)num * Math.log((double)(thermophilic == 0 ? 1 : thermophilic)/(double)num)   / Math.log(2.0)	
							+ -1 *(double)mesophilic/(double)num * Math.log((double)(mesophilic == 0 ? 1 : mesophilic)/(double)num)  / Math.log(2.0)));
			splitInfo += -1 * (
			((double)thermophilic/(double)num * Math.log((double)(thermophilic == 0 ? 1 : thermophilic)/(double)num)	
			+ (double)mesophilic/(double)num * Math.log((double)(mesophilic == 0 ? 1 : mesophilic)/(double)num)));
		}
		double[] result = new double[2];
		result[0] = infoFeature;
		result[1] = splitInfo;
		return result;
	}

	private int[] getThrmostbility(String s) {
		String[] temp = s.split(Options.separator);
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
			s[index++] = e.split(Options.separator)[0].trim();
		}
		return s;
	}

	private List<ArrayList<Integer>> StringToList(List<String> list) {
		List<ArrayList<Integer>> feature = new ArrayList<ArrayList<Integer>>();
		for (String e : list) {
			String[] tempString = e.split(Options.separator);
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			for (int i = 1; i < tempString.length; i++) {
				tempList.add(Integer.parseInt(tempString[i].trim()));
			}
			feature.add(tempList);
		}
		return feature;
	}
	
	private FeatureWeight[] sort(FeatureWeight[] fw) {
		boolean flag = true;
		for (int i = 0; i < fw.length && flag == true; i++) {
			flag = false;
			for (int j = i + 1; j < fw.length; j++) {
				if (fw[i].getWeight() < fw[j].getWeight()) {
					FeatureWeight temp = fw[i];
					fw[i] = fw[j];
					fw[j] = temp;
					flag = true;
				}
			}
		}
		return fw;
	}

}

class FeatureWeight {
	String feature;
	double weight;
	
	FeatureWeight() {}
	FeatureWeight(String feature, double weight) {
		this.feature = feature;
		this.weight = weight;
	}
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}

class FeatureThermostabiliy {
  int thermostabiliy;
  int record;

  FeatureThermostabiliy(int record, int thermostabiliy)
  {
    this.record = record;
    this.thermostabiliy = thermostabiliy;
  }
}
