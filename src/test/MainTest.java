package test;

import main.BayesClassifer.CrossValidation;
import main.BayesClassifer.InformationGainRatio;
import main.util.FileService;
import main.util.Options;

import java.io.IOException;
public class MainTest {

	public static void main(String[] args) throws IOException {
		System.out.println("**********特征选择...***********");
		new InformationGainRatio().getIGR(FileService.readFile(Options.path_dataset));
		System.out.println("**********特征选择完成************");
		System.out.println("**********十折交叉...***********");
		new CrossValidation().crossValidation(10, Options.path_dataset_featureSelection);
		System.out.println("**********十折交叉完成************");

	}

}
