package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import Jama.Matrix;
import params.Img;
import function.DetectFaceDemo;
import function.Pca;
import function.Pretreatment;
import function.compareFace;

public class main {
	
	public static void main(String args[]) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Vector<double[]> modelOFace = new Vector<double[]>();
		List<Img> imageurls = new ArrayList<Img>();
		File file = new File("./data/ForTraining.txt");
		Img imgfirst = null;
		int line = 1;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			String url = null;

			String id;
			while ((temp = reader.readLine()) != null) {
				String[] tokens = temp.split(" ");
				id = tokens[0];

				Pretreatment pre = new Pretreatment();
				url = "./ForTestImage/" + tokens[2];

				Mat image = Imgcodecs.imread(url);
				if (image.empty())
					continue;

				DetectFaceDemo face = new DetectFaceDemo();
				face.detectface(url);
				Img img = pre.prepare(url, id);
				if (tokens[2].equals("AverageMaleFace.jpg")) {
					System.out.println("this is the first");
					imgfirst = img;
				}
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
		// double[][] efaces = new double[p][n];
		// for (int i = 0; i < p; i++) {
		// for (int j = 0; j < n; j++)// 注意只能遍历一半，所以j<=i如果全部遍历，则数据交换了两次,相当于没有置换
		// {
		// efaces[i][j] = faces[j][i];
		// }
		// }
		Pca pca = new Pca();
		double[][] stand = pca.Standardlizer(faces);
		double[][] assosiation = pca.CoefficientOfAssociation(stand);
		double[][] flagValue = pca.FlagValue(assosiation);
		// for(int i=0;i<p;i++){
		// for(int j=0;j<p;j++){
		// System.out.print(flagValue[i][j]+" ");
		// }
		// System.out.println();
		// }
		double[][] flagVector = pca.FlagVector(assosiation);
		// for(int i=0;i<p;i++){
		// for(int j=0;j<p;j++){
		// System.out.print(flagVector[i][j]+" ");
		// }
		// System.out.println();
		// }
		// System.out.println();
		int[] xuan = pca.SelectPrincipalComponent(flagValue);
		// for (int i = 0; i < xuan.length; i++) {
		//
		// System.out.print(xuan[i] + " ");
		//
		// }
		System.out.println();
		System.out.println();
		double[][] result = pca.PrincipalComponent(flagVector, xuan);
		// System.out.println(result);
		// for (int i = 0; i < p; i++) {
		// for (int j = 0; j < xuan.length; j++) {
		// System.out.print(result[i][j] + " ");
		// }
		// System.out.println();
		// }

		Matrix A = new Matrix(faces);
		Matrix B = new Matrix(result);
		Matrix C = A.times(B);// 投影矩阵
		double[] ans = new double[faces.length];
		double[][] D = C.getArray();
		/*String console = "./modal.txt";
		BufferedWriter cons = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(console, true)));
		for(int i=0;i<D.length;i++){
			for(int j = 0;j<D[0].length;j++){
				cons.append(D[i][j]+" ");
			}
			cons.append("\n");
		}
		cons.close();
		System.exit(0);*/
		int k = 0;
		String urls = "./data.txt";

		BufferedWriter in = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(urls, true)));
		double temp = 0;
		double abs = 0;
		double max = 0;
		double cos = 0;
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[0].length; j++) {
				ans[i] += (D[k][j] - D[i][j]) * (D[k][j] - D[i][j]);
			}
			ans[i] = Math.sqrt(ans[i]);
			temp = ans[i] % 20 - 10;
			cos = 1 / (1 + Math.pow(Math.E, temp));
			// compareFace cpFace = new compareFace();
			// cos = cpFace.compare(cpFace.getData(imageurls.get(k).getPath()),
			// cpFace.getData(imageurls.get(i).getPath()));
			System.out.println("距离" + ans[i] + "相似度" + cos + "为第" + i + "张人脸"
					+ "路由" + imageurls.get(i).getPath());
			String contain = "距离" + ans[i] + "相似度" + cos + "路由" + "src"
					+ imageurls.get(k).getPath() + "tar"
					+ imageurls.get(i).getPath();
			in.write(contain + "\n");

		}
		in.close();
	}
}
