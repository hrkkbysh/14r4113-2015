package uicomponent.FileTree;

/**
 * @author 14r4113 on 2016/01/31.
 */
import java.nio.file.Path;

public class PathItem {
	private Path path;
	private int countNewDir = 0;
	public PathItem(Path path) {
		this.path = path;
	}
	public Path getPath() {
		return path;
	}
	@Override
	public String toString() {
		if (path.getFileName() == null) {
			return path.toString();
		} else {
			return path.getFileName().toString();
		}
	}
	public int getCountNewDir() {
		return ++this.countNewDir;
	}
}
