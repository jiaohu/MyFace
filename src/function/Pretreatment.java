package function;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pretreatment {
	public int[][] piexl;

	// 二值化
	public Image getBinaryImage(BufferedImage image) throws IOException {

		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage binaryImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				binaryImage.setRGB(i, j, rgb);
			}
		}

		return binaryImage;
	}

	// 灰度处理
	public Image getGrayImage(BufferedImage image) throws IOException {

		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage grayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
			}
		}

		return grayImage;
	}

	// 色彩均衡，取消光照误差
	public Image getColorImage(BufferedImage image) throws IOException {

		int RedTotal = 0;
		int GreenTotal = 0;
		int BlueTotal = 0;
		int GrayTotal = 0;
		int NumTotal, RedAverage, GreenAverage, BlueAverage, GrayAverage;
		int RedTemp, GreenTemp, BlueTemp;

		float Kr, Kb, Kg;
		Color myWhite;
		int width = image.getWidth();
		int height = image.getHeight();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				RedTotal += (rgb & 0xff0000) >> 16;
				GreenTotal += (rgb & 0xff00) >> 8;
				BlueTotal += (rgb & 0xff);

			}
		}

		NumTotal = width * height;
		//System.out.println(NumTotal);

		RedAverage = RedTotal / NumTotal;
		GreenAverage = GreenTotal / NumTotal;
		BlueAverage = BlueTotal / NumTotal;
		//System.out.println(RedAverage + " " + GreenAverage + " " + BlueAverage);

		GrayAverage = (RedAverage + GreenAverage + BlueAverage) / 3;

		//System.out.println("hhh" + GrayAverage);
		Kr = (float) GrayAverage / RedAverage;
		Kg = (float) GrayAverage / GreenAverage;
		Kb = (float) GrayAverage / BlueAverage;

		//System.out.println(Kr + " " + Kg + " " + Kb);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);

				RedTemp = (int) (((rgb & 0xff0000) >> 16) * Kr);
				GreenTemp = (int) (((rgb & 0xff00) >> 8) * Kg);
				BlueTemp = (int) (((rgb & 0xff)) * Kb);

				float factor = Max(RedTemp, GreenTemp, BlueTemp);
				factor = (float) factor / 255;
				/*
				 * RedTemp = RedTemp > 255 ? 255 : RedTemp; GreenTemp =
				 * GreenTemp > 255 ? 255 : GreenTemp; BlueTemp = BlueTemp > 255
				 * ? 255 : BlueTemp;
				 */

				// System.out.println(factor);
				if (factor > 1) {
					RedTemp = (int) (RedTemp / factor);
					GreenTemp = (int) (GreenTemp / factor);
					BlueTemp = (int) (BlueTemp / factor);
				}
				myWhite = new Color(RedTemp, GreenTemp, BlueTemp);

				int color = myWhite.getRGB();

				image.setRGB(i, j, color);

			}
		}
		return image;

	}

	private int Max(int redTemp, int greenTemp, int blueTemp) {
		// TODO Auto-generated method stub
		int index;
		if (redTemp > greenTemp) {
			index = redTemp;
		} else {
			index = greenTemp;
		}
		if (index < blueTemp) {
			index = blueTemp;
		}
		return index;
	}

	// 高斯平滑
	public Image GausslanBlur(BufferedImage image) throws IOException {
//		File file = new File("d://tupian/IMG_20160726_140328.jpg");
//		BufferedImage image = ImageIO.read(file);

		int width = image.getWidth();
		int height = image.getHeight();
		// System.out.println("宽"+width+"高"+height);
		int[][] martrix = new int[3][3];
		int[] values = new int[9];
		// System.out.println("for begin:"+values[8]);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				readPixel(image, i, j, values);
				fillMatrix(martrix, values);
				image.setRGB(i, j, avgMatrix(martrix));
			}
		}
		return image;
	}

	private int avgMatrix(int[][] martrix) {
		// TODO Auto-generated method stub
		int r = 0;
		int g = 0;
		int b = 0;
		for (int i = 0; i < martrix.length; i++) {
			int[] x = martrix[i];
			for (int j = 0; j < x.length; j++) {
				if (j == 1) {
					continue;
				}
				Color c = new Color(x[j]);
				r += c.getRed();
				g += c.getGreen();
				b += c.getBlue();

			}
		}
		return new Color(r / 8, g / 8, b / 8).getRGB();
	}

	private void fillMatrix(int[][] martrix, int[] values) {
		// TODO Auto-generated method stub
		int filled = 0;
		for (int i = 0; i < martrix.length; i++) {
			int[] x = martrix[i];
			for (int j = 0; j < x.length; j++) {
				x[j] = values[filled++];
			}
		}
	}

	private void readPixel(BufferedImage image, int i, int j, int[] values) {
		// TODO Auto-generated method stub
		int xStart = i - 1;
		int yStart = j - 1;

		int current = 0;
		for (int x = xStart; x < 3 + xStart; x++) {
			for (int y = yStart; y < 3 + yStart; y++) {
				// System.out.println("x:"+x+"y:"+y);
				int tx = x;
				if (tx < 0) {
					tx = -tx;
				} else if (tx >= image.getWidth()) {
					tx = i;
				}
				int ty = y;
				if (ty < 0) {
					ty = -ty;
				} else if (ty >= image.getHeight()) {
					ty = j;
				}
				// System.out.println("tx:"+tx+"ty:"+ty);
				// System.out.println(current);
				values[current++] = image.getRGB(tx, ty);
			}
		}
	}

}
