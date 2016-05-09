
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// -------------------------------------------------------------------------
/**
 * huffman compress
 *
 * @author adamweld
 * @version May 3, 2016
 */
public class huff
{

    // ----------------------------------------------------------
    /**
     * huff compresses files
     * @param force whether to force compression
     * @param filepath file to compress
     * @throws FileNotFoundException if you mess up
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        if(args.length == 0) {
            System.out.println("must include an argument");
            System.exit(0);
        }
        String filepath = args[0];
        Boolean force = args[1].equals("true");
        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.initialize(bits);
        huff.write(bits, filepath, force);
    }

}

