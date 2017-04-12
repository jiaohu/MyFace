package function;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class compareFace {
	public static String[][] getPX(String args) {
		int[] rgb = new int[3];
		File file = new File(args);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int width = image.getWidth();
		int height = image.getHeight();

		int minx = image.getMinX();
		int miny = image.getMinY();

		String[][] list = new String[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = image.getRGB(i, j);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
			}
		}
		return list;

	}

	public void compare(String imgPath1, String imgPath2) {
		String[] images = { imgPath1, imgPath2 };
		if (images.length == 0) {
			System.exit(0);
		}

		// 比较图片的相似度
		String[][] list1 = getPX(images[0]);
		String[][] list2 = getPX(images[1]);
		int similiar = 0;
		int usimiliar = 0;
		int i = 0, j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m = 0; m < strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n = 0; n < value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k])
								- Integer.parseInt(value2[k])) < 5) {
							similiar++;
						} else {
							usimiliar++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}
		list1 = getPX(images[1]);
		list2 = getPX(images[0]);
		i = 0;
		j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m = 0; m < strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n = 0; n < value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k])
								- Integer.parseInt(value2[k])) < 5) {
							similiar++;
						} else {
							usimiliar++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}
		String res = "";
		try {
			res = ((Double.parseDouble(similiar + "") / Double
					.parseDouble((similiar + usimiliar) + "")) + "");
			res = res.substring(res.indexOf(".") + 1, res.indexOf(".") + 3);
		} catch (Exception e) {
			res = "0";
		}
		if (res.length() <= 0) {
			res = "0";
		}
		if (usimiliar == 0) {
			res = "100";
		}
		System.out.println("相似像素数量" + similiar + "不相似像素数量" + usimiliar + "相似率"
				+ Integer.parseInt(res) + "%");
	}
}
