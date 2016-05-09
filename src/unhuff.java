import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class unhuff
{

    public static void main(String filepath)
        throws IOException
    {
        BitOutputStream out = new BitOutputStream(filepath + "d.txt");
        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.uncompress(bits, out);
    }
}
