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
     * @param args
     *            is input
     * @throws IOException
     *             for wrong files
     */
    public static void main(String[] args)
        throws IOException
    {
        if (args.length == 0)
        {
            System.out.println("must include an argument");
            System.exit(0);
        }
        String filepath = args[0], extension = "";
        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        int index1 = filepath.length(), index2 = index1;
        for (int i = 0; i < filepath.length(); i++)
        {
            if (filepath.charAt(i) == '.')
            {
                index1 = i;
            }
        }
        filepath = filepath.substring(0, index1); // remove .huff
        for (int j = filepath.length() - 1; j >= 0; j--)
        {
            if (filepath.charAt(j) == '.')
            {
                index2 = j;
            }
        }
        extension = filepath.substring(index2); // file extension
        BitOutputStream out = new BitOutputStream(
            filepath.substring(0, index1) + ".unhuffed" + extension);
        HuffModel huff = new HuffModel();
        try
        {
            huff.uncompress(bits, out);
        }
        catch (IOException e)
        {
            System.out
                .println("you probably tried to unhuff a non-huffed file");
        }
    }
}
