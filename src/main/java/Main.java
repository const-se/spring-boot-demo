import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("/Users/kkseleznev/my/uploads");
        String fileName = "IMG_7505";
        System.out.println(path.resolve(fileName).toString());
    }
}
