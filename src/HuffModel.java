import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author adamweld
 * @version Apr 12, 2016
 */
public class HuffModel
    implements IHuffModel
{
    BitInputStream istream;


    /**
     * Display all encodings (via the associated view).
     */
    @Override
    public void showCodings()
    {
        HuffTree[] out = new HuffTree[256];
        CharCounter cc = new CharCounter();
        try
        {
            cc.countAll(istream);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int j = 0;
        for (int i = 0; i < cc.array.length; i++)
        {
            if (cc.array[i] != 0) {
               out[j++] = new HuffTree((char) i, cc.array[i]);
            }
        }


        MinHeap Hheap = new MinHeap(out, j, 256);

        HuffTree tree = buildTree(Hheap);

        String[] return = new String[256];

     // use the zero/one value of the bit read

     // to traverse Huffman coding tree

     // if a leaf is reached, decode the character and print UNLESS

     // the character is pseudo-EOF, then decompression done
        int bits;



     if ( (bits & 1) == 0) // read a 0, go left in tree

     else // read a 1, go right in tree



     if (at leaf-node in tree)

     {

     if (leaf-node stores pseudo-eof char)

     break; // out of loop

     else

     write character stored in leaf-node

     }

     }

    }


    public String[] Traversal(String[] in);
    {



    HuffTree buildTree(MinHeap Hheap)
    {
        HuffTree tmp1, tmp2, tmp3 = null;

        while (Hheap.heapsize() > 1)
        { // While two items left
            tmp1 = (HuffTree)Hheap.removemin();
            tmp2 = (HuffTree)Hheap.removemin();
            tmp3 = new HuffTree(
                tmp1.root(),
                tmp2.root(),
                tmp1.weight() + tmp2.weight());
            Hheap.insert(tmp3); // Return new tree to heap
        }
        return tmp3; // Return the tree
    }


    /**
     * Display all chunk/character counts (via the associated view).
     */
    @Override
    public void showCounts()
    {
        CharCounter cc = new CharCounter();
        try
        {
            cc.countAll(istream);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Frequency of each character (0 - 255):");
        for (int i = 0; i < cc.array.length; i++)
        {
            System.out.println(i + ": \t" + cc.array[i]);
        }
    }


    /**
     * Initialize state via an input stream. The stream most likely comes from a
     * view, it's NOT a BitInputStream
     *
     * @param stream
     *            is an input stream for initializing state of this model
     */
    @Override
    public void initialize(InputStream stream)
    {
        istream = (BitInputStream)stream;
    }


    /**
     * Write a compressed version of the data read by the InputStream parameter,
     * -- if the stream is not the same as the stream last passed to initialize,
     * then compression won't be optimal, but will still work. If force is
     * false, compression only occurs if it saves space. If force is true
     * compression results even if no bits are saved.
     *
     * @param stream
     *            is the input stream to be compressed
     * @param file
     *            specifes the file to be written with compressed data
     * @param force
     *            indicates if compression forced
     */
    @Override
    public void write(InputStream stream, File file, boolean force)
    {
        // TODO Auto-generated method stub

    }


    /**
     * Uncompress a previously compressed file.
     *
     * @param in
     *            is the compressed file to be uncompressed
     * @param out
     *            is where the uncompressed bits will be written
     */
    @Override
    public void uncompress(InputStream in, OutputStream out)
    {
        // TODO Auto-generated method stub

    }

}
