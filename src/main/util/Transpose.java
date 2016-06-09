package main.util;

import java.util.ArrayList;
import java.util.List;

public class Transpose {
	public static List<String> transpose(List<String> list) {
		int size = list.size();
		String[][] s = new String[size][];
		for (int i = 0; i < size; i++) {
			s[i] = list.get(i).split(Options.separator);
		}
		String[][] transpose_s = new String[s[0].length][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < s[i].length; j++) {
				transpose_s[j][i] = s[i][j];
			}
		}
		List<String> transpose_list = new ArrayList<String>();
		for (int i = 0; i < transpose_s.length; i++) {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < transpose_s[i].length; j++) {
				sb.append(transpose_s[i][j] + Options.separator);
			}
			sb.deleteCharAt(sb.length() - 1);
			transpose_list.add(sb.toString());
		}
		return transpose_list;
	}
}
