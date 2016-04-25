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

        String[] output = new String[256];

        j = 0;
        for(int k = 0; k < out.length; k++) {
            String str = "";
            output[j++] = traverse((HuffBaseNode)out[k], str);

            System.out.println(output[j - 1]);
        }


    }


    // ----------------------------------------------------------
    /**
     * recursive traversal method
     * @param root
     * @param path
     * @return
     */
    public String traverse(HuffBaseNode root, String path) {

        traverse(((HuffInternalNode)root).left(), path + "0");

        traverse(((HuffInternalNode)root).right(), path + "1");

        return path;

    }





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
    public void write(InputStream stream, String file, boolean force)
    {
        BitOutputStream out = new BitOutputStream("file");
        out.write(BITS_PER_INT, MAGIC_NUMBER);
    }

    public void wTraverse(HuffTree tree, BitOutputStream bit) {

        if(tree.root().isLeaf()) {
            bit.write(1,1); // leaf node
            bit.write(9, ((HuffLeafNode)tree.root()).element());
        } else {
            bit.write(1,0);
        }
        wTraverse(((HuffInternalNode)tree.root()).left(), bit);
        wTraverse(((HuffInternalNode)tree.root()).right(), bit);

        bit.write(1, 1);
        bit.write(9, PSEUDO_EOF);
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
