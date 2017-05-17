package DBHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import params.Img;
import Jama.Matrix;
import function.DetectFaceDemo;
import function.Pca;
import function.Pretreatment;

public class Test {
	public static void main(String args[]) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Vector<double[]> modelOFace = new Vector<double[]>();
		List<Img> imageurls = new ArrayList<Img>();
		File file = new File("./modal_url.txt");
		Img imgfirst = null;
		int line = 1;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			String url = null;

			String ids;
			while ((temp = reader.readLine()) != null && line <= 100) {
				String[] tokens = temp.split(" ");
				ids = tokens[0];

				Pretreatment pre = new Pretreatment();
				url = "./ForTrainImage/" + tokens[1];

				Mat image = Imgcodecs.imread(url);
				if (image.empty())
					continue;

				// DetectFaceDemo face = new DetectFaceDemo();
				// face.detectface(url);
				Img img = pre.prepareTest(url, ids);
				// if (tokens[2].equals("AverageMaleFace.jpg")) {
				// System.out.println("this is the first");
				// imgfirst = img;
				// }
				modelOFace.add(img.getPiexl());
				imageurls.add(img);
				line++;
			}
			// System.out.println(line);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}

		double[][] faces = (double[][]) modelOFace.toArray(new double[0][0]);
		int n = faces.length;
		int p = faces[0].length;

		Pca pca = new Pca();
		double[][] stand = pca.Standardlizer(faces);
		double[][] assosiation = pca.CoefficientOfAssociation(stand);
		double[][] flagValue = pca.FlagValue(assosiation);

		double[][] flagVector = pca.FlagVector(assosiation);

		int[] xuan = pca.SelectPrincipalComponent(flagValue);

		System.out.println();
		System.out.println();
		double[][] result = pca.PrincipalComponent(flagVector, xuan);
		Matrix A = new Matrix(faces);
		Matrix B = new Matrix(result);
		Matrix C = A.times(B);// Í¶Ó°¾ØÕó
		double[] ans = new double[faces.length];
		double[][] D = C.getArray();
		String console = "./modal.txt";
		BufferedWriter cons = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(console, true)));
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[0].length; j++) {
				cons.append(D[i][j] + " ");
			}
			cons.append("\n");
		}
		cons.close();
		System.exit(0);
	}
}
