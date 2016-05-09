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
    private BitInputStream istream;
    private int            numCount;
    private String[]       encodings;
    private int[]          counts;
    private HuffTree       tree;
    private MinHeap        Hheap;


    /**
     * Display all encodings (via the associated view).
     */
    public void showCodings()
    {
        showCounts();
        HuffTree[] out = new HuffTree[numCount + 1];
        int n = 0;
        for (int i = 0; i < 256; i++)
        {
            if (counts[i] != 0)
            {
                out[n++] = new HuffTree((char)i, counts[i]);
            }
        }
        out[n] = new HuffTree((char)IHuffModel.PSEUDO_EOF, 1);

        Hheap = new MinHeap(out, numCount + 1, numCount + 1);

        tree = buildTree();

        encodings = new String[257];

        System.out.println("Showing Encoding of Data:");
        traverse(tree.root(), "");
    }


    // ----------------------------------------------------------
    /**
     * recursive traversal method
     *
     * @param root
     * @param path
     */
    public void traverse(HuffBaseNode root, String path)
    {
        if (root != null)
        {
            if (root.isLeaf())
            {
                int index = ((HuffLeafNode)root).element();
                encodings[index] = path + Integer.toBinaryString(index);
                System.out.println((char)index + ": " + encodings[index]);
            }
            else
            {
                traverse(((HuffInternalNode)root).left(), path + "0");
                traverse(((HuffInternalNode)root).right(), path + "1");
            }
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
    public HuffTree buildTree()
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
        for (int i = 0; i < 256; i++)
        {
            if (cc.getCount((char)i) != 0)
            {
                int num = cc.getCount((char)i);
                System.out.println((char)i + ": \t" + num);
                numCount++;
                counts[i] = num;
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
        numCount = 0;
        counts = new int[256];
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
        BitOutputStream out = new BitOutputStream(file + ".huff");
        showCodings();
        out.write(BITS_PER_INT, MAGIC_NUMBER);

        wTraverse(tree.root(), out);
        BitInputStream bit = new BitInputStream(file);
        int inbits;
        try
        {
            while ((inbits = bit.read(BITS_PER_WORD)) != -1)
            {
                char[] data = encodings[inbits].toCharArray();
                for (int i = 0; i < data.length; i++)
                {
                    out.write(1, data[i]);
                }
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String eof = ((Integer)IHuffModel.PSEUDO_EOF).toString();
        ;
        char[] endfile = eof.toCharArray();
        for (int i = 0; i < endfile.length; i++)
        {
            out.write(1, endfile[i]);
        }

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
        if (root != null)
        {
            if (root.isLeaf())
            {
                bit.write(1, 1); // leaf node
                bit.write(9, ((HuffLeafNode)root).element());
            }
            else
            {
                bit.write(1, 0);
                wTraverse(((HuffInternalNode)root).left(), bit);
                wTraverse(((HuffInternalNode)root).right(), bit);
            }
        }
    }


    /**
     * Uncompress a previously compressed file.
     *
     * @param in
     *            is the compressed file to be uncompressed
     * @param out
     *            is where the uncompressed bits will be written
     * @throws IOException
     */
    public void uncompress(InputStream in, OutputStream out) throws IOException
    {
        int magic = 0;
        try
        {
            magic = ((BitInputStream)in).read(BITS_PER_INT);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (magic != MAGIC_NUMBER){
            throw new IOException("magic number not right");
         }
        HuffInternalNode rootNode = null;
        buildTree(rootNode, (BitInputStream)in);



        int bits;
        HuffBaseNode thisNode = rootNode;
        while (true)
        {
            bits = ((BitInputStream)in).read(1);
            if (bits == -1)
            {
                throw new IOException("unexpected end of input file");
            }
            else
            {

                if (bits == 0) {
                    thisNode = ((HuffInternalNode)thisNode).left();
                }
                else thisNode = ((HuffInternalNode)thisNode).right();
                if (thisNode.isLeaf())
                {
                    int val = ((HuffLeafNode)thisNode).element();
                    if (val == IHuffModel.PSEUDO_EOF)
                        break; // out of loop
                    else
                        ((BitOutputStream)out).write(val);
                }
            }
        }
        in.close();
        out.close();
    }


    // ----------------------------------------------------------
    /**
     * builds huffman tree from file
     * @param root
     * @param bit
     * @return
     * @throws IOException
     */
    public HuffBaseNode buildTree(HuffBaseNode root, BitInputStream bit) throws IOException
    {
        int inbits = bit.read(BITS_PER_WORD);
        try
        {
            if (inbits == 0)
            {
                HuffBaseNode left = null, right = null;
                root = new HuffInternalNode(
                    buildTree(left, bit),
                    buildTree(right, bit),
                    0);
            }
            else if (inbits == 1)
            {
                char data = (char)bit.read(BITS_PER_WORD * 9);
                root = new HuffLeafNode(data, 0);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return root;
    }

}
