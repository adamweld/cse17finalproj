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

    public static void main(boolean force, String filepath) throws FileNotFoundException
    {

        BitInputStream bits = new BitInputStream(new FileInputStream(filepath));
        HuffModel huff = new HuffModel();
        huff.initialize(bits);
        huff.showCounts();
        huff.showCodings();
        /*
         * TODO: compute expected filesize Before writing the compressed file,
         * you should compute its expected size. To get the expected size (in
         * bits) of the compressed file, you will: - Add the size of the
         * MAGIC_NUMBER (32 bits) - Add the size of the Huffman Tree (you might
         * want to write a new method): add 1 for each internal node add 10 for
         * each leaf node - Add the size of the compressed content (you might
         * want to write a new method): For each char add the length of its huff
         * code multiply by its frequency (counts computed in Part I)
         */

        huff.write(bits, filepath, force);
    }

}
