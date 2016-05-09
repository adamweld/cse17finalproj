import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * uncompress
 *
 * @author adamweld
 * @version May 9, 2016
 */
public class unhuff
{

    // ----------------------------------------------------------
    /**
     * uncompresses huff files
     *
     * @param filepath
     *            is file to unhuff
     * @throws IOException
     *             for wrong files
     */
    public static void main(String[] args)
        throws IOException
    {
        if(args.length == 0) {
            System.out.println("must include an argument");
            System.exit(0);
        }
        String filepath = args[0];
        BitOutputStream out = new BitOutputStream(filepath + "d.txt");
        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.uncompress(bits, out);
    }
}
