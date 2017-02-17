import java.io.File;
import java.io.FileInputStream;

/**
 * Created by fujie on 2017/2/17.
 */
public class testBin {
    static public void main (String[] argv) {
        try {
            FileInputStream fin = new FileInputStream(new File(""));
            byte[] bytes = new byte[4];
            fin.read(bytes, 0, 4);
            int dim =  bytes[0] & 0xFF |
                    (bytes[1] & 0xFF) << 8 |
                    (bytes[2] & 0xFF) << 16 |
                    (bytes[3] & 0xFF) << 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
