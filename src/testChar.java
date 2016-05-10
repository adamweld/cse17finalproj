import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author adamweld
 * @version Apr 7, 2016
 */
public class testChar
{

    // ----------------------------------------------------------
    /**
     * tests our CharCounter methods
     *
     * @param args
     */
    public static void main(String[] args)
    {

        BitInputStream bits;
        try
        {
            bits = new BitInputStream(
                new FileInputStream(
                    "C:\\Users\\adamweld\\Downloads\\test.txt"));

            HuffModel huff = new HuffModel();
            huff.initialize(bits);
            huff.write(bits, "C:\\Users\\adamweld\\Downloads\\test.txt", true);
            try
            {
                BitInputStream bits2 = new BitInputStream(
                    new FileInputStream(
                        "C:\\Users\\adamweld\\Downloads\\test.txt.huff"));
                huff.uncompress(bits2, new BitOutputStream("C:\\Users\\adamweld\\Downloads\\testout.txt"));
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
