package main.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
	// read file
	public static List<String> readFile(String fileRoadreader) throws IOException {
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

	// write file
	public static void writeFile(List<String> list, String fileRoadWriter)	throws IOException {
		FileWriter fw = new FileWriter(fileRoadWriter);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < list.size(); i++) {
			bw.write(list.get(i) + "\n");
		}
		bw.flush();
		bw.close();
		fw.close();
	}
}
