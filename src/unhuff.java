import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class unhuff
{

    public static void main(String filepath) throws FileNotFoundException
    {
        BitOutputStream out = new BitOutputStream(filepath + "unhuffed");
        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.uncompress(bits, out);
    }
}
