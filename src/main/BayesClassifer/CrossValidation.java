package main.BayesClassifer;

import main.util.FileService;
import main.util.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class CrossValidation {
	
    private static int K_cross; // user specified k cross validation
    
    private static List<String> dataset = new ArrayList<String>();
    private static List<String> dataSet0 = new ArrayList<String>();
    private static List<String> dataSet1 = new ArrayList<String>();
    
    // Program entry
	public static void crossValidation(int K, String path) throws IOException {
		K_cross = K;
		CrossValidation cv = new CrossValidation();
		dataset = FileService.readFile(path);
		cv.split_0_1();
		cv.makeSubsets();
	}
	
	// split the two class of data set into dataSet0 and dataSet1
	private void split_0_1() {
		for (String s: dataset) {
			if (s.split(Options.separator)[0].equals("0")) {
				dataSet0.add(s);
			} else {
				dataSet1.add(s);
			}
		}
	}

	// make subsets and write them into files
	private void makeSubsets() throws IOException {
		int[] index0 = getRandomIndex(dataSet0);
		int[] index1 = getRandomIndex(dataSet1);
		
		int subset_size_0 = index0.length / K_cross;
		int subset_size_1 = index1.length / K_cross;
		
		List<String> temp_dataset = new ArrayList<String>();
		
		for (int i = 0; i < K_cross - 1; i++) {
			int begin0 = 0;
			int end0 = subset_size_0;
			int begin1 = 0;
			int end1 = subset_size_1;
			
			for (int j = begin0; j < end0; j++) {
				temp_dataset.add(dataSet0.get(index0[j]));
			}
			for (int j = begin1; j < end1; j++) {
				temp_dataset.add(dataSet1.get(index1[j]));
			}
			
			// write subset into file
			String path_subset_n = Options.path_subset + (i + 1) + Options.suffix;
			FileService.writeFile(temp_dataset, path_subset_n);
			
			temp_dataset.clear();
			
			begin0 += subset_size_0;
			end0 += subset_size_0;
			begin1 += subset_size_1;
			end1 += subset_size_1;
		}
		
		// handle the last subset
		for (int j = subset_size_0 * (K_cross - 1); j < index0.length; j++) {
			temp_dataset.add(dataSet0.get(index0[j]));
		}
		for (int j = subset_size_1 * (K_cross - 1); j < index1.length; j++) {
			temp_dataset.add(dataSet1.get(index1[j]));
		}
		// write subset into file
		FileService.writeFile(temp_dataset, Options.path_subset + K_cross + Options.suffix);
	}
	
	// random numbers
	private int[] getRandomIndex(List<String> dataSet) {
		Random random = new Random();
		Set<Integer> hs = new HashSet<Integer>();
		int size = dataSet.size();
		int[] index = new int[size];
		int temp;
		int i = 0;
		while (hs.size() < size) {
			temp = random.nextInt(size);
			if (!hs.contains(temp)) {
				hs.add(temp);
				index[i++] = temp;
			}
		}
		return index;
	}

}
