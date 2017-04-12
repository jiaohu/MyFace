package function;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class getHaar {

	/*
	 * 提取特征矩阵，采用积分图法 1.得到各点的像素值ii(x,y),递推公式s(x,y)=s(x,y-1)+i(x,y) ii(x,y)=
	 * ii(x-1,y)+s(x,y) 2.假设r=(x,y,w,h)(x,y为像素点，w,h为区域宽度跟高度)
	 * 那么，在此矩形内部所有元素之和等价于Sum(r) =
	 * ii(x+w,y+h)+ii(x-1,y-1)-ii(x+w,y-1)-ii(x-1,y+h)
	 */

	public int[][] getPiexll(int x, int y) throws IOException {
		File file = new File("d://tupian/IMG_20160726_140328_1.jpg");
		BufferedImage image = ImageIO.read(file);

		int width = image.getWidth();
		int height = image.getHeight();

		int[][] sizel = new int[width + 1][height + 1];
		int[][] piexl = new int[width + 1][height + 1];
		int[][] piexll = new int[width][height];

		for (int i = 0, j = 0; j <= height; j++)
			piexll[i][j] = 0;
		for (int j = 0, i = 0; i <= width; i++)
			piexll[i][j] = 0;

		for (int i = 1; i <= width; i++) {
			for (int j = 1; j <= height; j++) {
				piexl[i][j] = image.getRGB(i, j);
			}
		}

		for (int i = 1; i <= width; i++) {
			for (int j = 1; j <= height; j++) {
				int sum = 0;
				for (int k = 1; k <= i; k++) {
					sum += piexl[k][j];
				}
				if (j - 1 <= 0) {
					piexll[i][j] = sum;
				} else {
					piexll[i][j] = sum + piexll[i][j - 1];
				}
			}
		}
		return piexll;
	}

	public int[][] getResult(int[][] piexll) {
		int minw = 1;
		int minh = 2;
		int aw = 1;
		int ah = 1;
		int w = minw;
		int h = minh;

		int width = piexll.length;
		int height = piexll[0].length;

		int[][] result = new int[width][height];

		while (minh * ah <= height) {
			while (minw * aw <= width) {
				for (int i = 1; i <= height - h; i++) {
					for (int j = 1; j <= width - w; j++) {
						int white = piexll[i][j] + piexll[i + h / 2][j]
								- piexll[i][j + w];
						int black = piexll[i + h / 2][j] + piexll[i + h][j + w]
								- piexll[i + h][j] - piexll[i + h / 2][j + w];
						

					}
				}
			}
		}
		return result;
	}
}
