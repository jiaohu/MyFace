package params;
import java.util.List;

public class Image {
	private String id;
	private String path;
	private String height;
	private String width;

	public String getPath() {
		return path;
	}

	public String getId() {
		return id;
	}

	public String getHeight() {
		return height;
	}

	public String getWidth() {
		return width;
	}

	public void Img(String id, String width, String height, String path) {
		this.id = id;
		this.height = height;
		this.width = width;
		this.path = path;
	}
}
