import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// -------------------------------------------------------------------------
/**
 * main class to create file compressor/uncompressor
 *
 * @author adamweld, conor
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
    private int            size;


    /**
     * Display all encodings (via the associated view).
     */
    public void showCodings()
    {
        showCounts();
        HuffTree[] out = new HuffTree[numCount + 1];
        encodings = new String[257];
        int n = 0;
        for (int i = 0; i < 256; i++)
        {
            if (counts[i] != 0)
            {
                out[n++] = new HuffTree((char)i, counts[i]);
            }
        }
        out[n++] = new HuffTree((char)IHuffModel.PSEUDO_EOF, 1); // end of file
        Hheap = new MinHeap(out, n, n); // create leaf nodes for hufftree
        tree = buildTree(); // build full huff tree
        traverse(tree.root(), "");
        System.out.println("Showing Encoding of Data:");
        for (int k = 0; k < counts.length; k++)
        {
            String e = encodings[k];
            if (e != null)
            {
                System.out.println((char)k + ": \t" + e);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * recursive traversal method
     *
     * @param root
     *            is root node
     * @param path
     *            is path in string form
     */
    private void traverse(HuffBaseNode root, String path)
    {
        if (root == null)
            return;
        else if (root.isLeaf())
        {
            size += 10;
            encodings[((HuffLeafNode)root).element()] = path;
        }
        else
        {
            size++;
            traverse(((HuffInternalNode)root).left(), path + "0");
            traverse(((HuffInternalNode)root).right(), path + "1");
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
    private HuffTree buildTree()
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
                counts[i] = num;
                numCount++;
            }
        }
        if (numCount == 0)
        {
            System.out.println("file is empty");
            System.exit(0);
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
        counts = new int[257];
        size = 0;
    }


    // ----------------------------------------------------------
    /**
     * size of compressed part of tree
     *
     * @return length of compressed part
     */
    private int huffLength()
    {
        int ret = 0;
        for (int i = 0; i < encodings.length; i++)
        {
            if (encodings[i] != null)
            {
                ret += encodings[i].length() * counts[i];
            }
        }
        return ret;
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
     *            specifies the file to be written with compressed data
     * @param force
     *            indicates if compression forced
     * @throws IOException
     */
    public void write(InputStream stream, String file, boolean force)
    {
        BitOutputStream out = new BitOutputStream(file + ".huff");
        showCodings();
        if (!force)
        {
            int expectedSize = size + BITS_PER_INT + huffLength();
            int originalSize = 0;
            for (int i = 0; i < counts.length; i++)
            {
                if (counts[i] != 0)
                {
                    originalSize += 8 * counts[i];
                }
            }
            if (expectedSize >= originalSize)
            {
                System.out
                    .println("File would be larger if compressed, skipping...");
                System.exit(0);
            }
            else
            {
                System.out.println("Compressing...");
            }
        }
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
            System.out.println("IO Exception");
            e.printStackTrace();
        }
        char[] data = encodings[256].toCharArray(); // Write EOF
        for (int i = 0; i < data.length; i++)
        {
            out.write(1, data[i]);
        }

        out.close();
        bit.close();
    }


    // ----------------------------------------------------------
    /**
     * traversal method for Write()
     *
     * @param root
     * @param bit
     */
    private void wTraverse(HuffBaseNode root, BitOutputStream bit)
    {
        if (root == null)
            return;
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


    /**
     * Uncompress a previously compressed file.
     *
     * @param in
     *            is the compressed file to be uncompressed
     * @param out
     *            is where the uncompressed bits will be written
     * @throws IOException
     */
    public void uncompress(InputStream in, OutputStream out)
        throws IOException
    {
        int magic = 0;
        try
        {
            magic = ((BitInputStream)in).read(BITS_PER_INT);
        }
        catch (IOException e)
        {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
        if (magic != MAGIC_NUMBER)
        {
            throw new IOException("magic number not right");
        }
        HuffBaseNode rootNode = buildHuffTree((BitInputStream)in);
        HuffBaseNode thisNode = rootNode;
        int bits;
        while (true)
        {
            bits = ((BitInputStream)in).read(1);
            if (bits == -1)
            {
                throw new IOException("unexpected end of input file");
            }
            else
            {
                {
                    if (bits == 0)
                    {
                        thisNode = ((HuffInternalNode)thisNode).left();
                    }
                    else
                    {
                        thisNode = ((HuffInternalNode)thisNode).right();
                    }
                    if (thisNode.isLeaf())
                    {
                        int val = ((HuffLeafNode)thisNode).element();
                        if (val == PSEUDO_EOF)
                        {
                            break; // out of loop
                        }
                        else
                        {
                            ((BitOutputStream)out).write(val);
                            thisNode = rootNode;
                        }
                    }
                }
            }
        }
        in.close();
        out.close();
    }


    // ----------------------------------------------------------
    /**
     * builds huffman tree from file
     *
     * @param bit
     * @return the built tree
     * @throws IOException
     */
    private HuffBaseNode buildHuffTree(BitInputStream bit)
        throws IOException
    {
        int inbits = bit.read(1);
        HuffBaseNode root = null;
        try
        {
            if (inbits == 0)
            {
                root = new HuffInternalNode(
                    buildHuffTree(bit),
                    buildHuffTree(bit),
                    1);
            }
            else if (inbits == 1)
            {
                int data = bit.read(9);
                return new HuffLeafNode((char)data, 1);
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