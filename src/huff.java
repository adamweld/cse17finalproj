
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author adamweld
 * @version May 3, 2016
 */
public class huff
{

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param force
     * @param filepath
     * @throws FileNotFoundException
     */
    public static void main(boolean force, String filepath) throws FileNotFoundException
    {

        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.initialize(bits);
        huff.write(bits, filepath, force);
    }

}

