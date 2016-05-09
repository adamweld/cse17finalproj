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
    /**
     * input stream
     */
    public BitInputStream istream;
    public String[]       encodings;


    /**
     * Display all encodings (via the associated view).
     */
    public void showCodings()
    {
        HuffTree[] out = new HuffTree[256];
        CharCounter cc = new CharCounter();
        cc.countAll(istream);

        int j = 0;
        for (int i = 0; i < cc.array.length; i++)
        {
            if (cc.array[i] != 0)
            {
                out[j++] = new HuffTree((char)i, cc.array[i]); // create new
                                                               // leaf node with
                                                               // each char
            }
        }

        MinHeap Hheap = new MinHeap(out, j, 256);

        HuffTree tree = buildTree(Hheap);

        encodings = new String[j];

        traverse(tree.root(), "", 0);

        for (int k = 0; k < j; k++)
        {
            System.out.println(encodings[k]);
        }

    }


    // ----------------------------------------------------------
    /**
     * recursive traversal method
     *
     * @param root
     * @param path
     * @param num
     */
    public void traverse(HuffBaseNode root, String path, int num)
    {
        System.out.println("made it");
        while (root != null)
        {

            traverse(((HuffInternalNode)root).left(), path + "0", num);
            if (root.isLeaf())
            {
                encodings[num] = path + ((HuffLeafNode)root).element();
                num++;
// path = "";
            }
            traverse(((HuffInternalNode)root).right(), path + "1", num);
        }
    }


    // ----------------------------------------------------------
    /**
     * build tree method from canvas
     *
     * @param Hheap
     *            is input
     * @return built tree
     */
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
    public void showCounts()
    {
        CharCounter cc = new CharCounter();
        cc.countAll(istream);
        System.out.println("Frequency of each character (0 - 255):");
        for (int i = 0; i < cc.array.length; i++)
        {
            if (cc.array[i] != 0)
            {
                System.out.println(i + ": \t" + cc.array[i]);
            }
        }
    }


    /**
     * Initialize state via an input stream. The stream most likely comes from a
     * view, it's NOT a BitInputStream
     *
     * @param stream
     *            is an input stream for initializing state of this model
     */
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
    public void write(InputStream stream, String file, boolean force)
    {
        BitOutputStream out = new BitOutputStream("file");
        istream = (BitInputStream)stream;
        HuffTree[] treeArray = new HuffTree[257];
        CharCounter cc = new CharCounter();

        cc.countAll(istream);
        int j = 0;
        for (int i = 0; i < cc.array.length; i++)
        {
            if (cc.array[i] != 0)
            {
                treeArray[j++] = new HuffTree((char)i, cc.array[i]);
            }
        }
        treeArray[j] = new HuffTree((char)IHuffModel.PSEUDO_EOF, 1); // add EOF
                                                                     // char

        MinHeap Hheap = new MinHeap(treeArray, j, 257);

        HuffTree tree = buildTree(Hheap);

        out.write(BITS_PER_INT, MAGIC_NUMBER);

        wTraverse(tree.root(), out);

        // TODO write original file
// while ((inbits = bit.read(BITS_PER_WORD)) != -1)
// {
// // get the code computed in part II
// // convert that code into an array of chars using .toCharArray() method
// //go through the array of char and write each char (cast as int) using 1 bit
// //out.write(1, (int)char);
//
// }
        out.close();
    }


    // ----------------------------------------------------------
    /**
     * traversal method for Write()
     *
     * @param root
     * @param bit
     */
    public void wTraverse(HuffBaseNode root, BitOutputStream bit)
    {
        while (root != null)
        {
            wTraverse(((HuffInternalNode)root).left(), bit);
            if (root.isLeaf())
            {
                bit.write(1, 1); // leaf node
                bit.write(9, ((HuffLeafNode)root).element());
            }
            else
            {
                bit.write(1, 0);
            }
            wTraverse(((HuffInternalNode)root).right(), bit);
        }
    }


    /**
     * Uncompress a previously compressed file.
     *
     * @param in
     *            is the compressed file to be uncompressed
     * @param out
     *            is where the uncompressed bits will be written
     */
    public void uncompress(InputStream in, OutputStream out)
    {
        // TODO Auto-generated method stub

    }

}
