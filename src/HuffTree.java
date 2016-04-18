/** A Huffman coding tree */
class HuffTree
    implements Comparable
{
    private HuffBaseNode root;


    /** Constructors */
    HuffTree(char el, int wt)
    {
        root = new HuffLeafNode(el, wt);
    }


    HuffTree(HuffBaseNode l, HuffBaseNode r, int wt)
    {
        root = new HuffInternalNode(l, r, wt);
    }


    HuffBaseNode root()
    {
        return root;
    }


    int weight() // Weight of tree is weight of root
    {
        return root.weight();
    }


    public int compareTo(Object t)
    {
        HuffTree that = (HuffTree)t;
        if (root.weight() < that.weight())
            return -1;
        else if (root.weight() == that.weight())
            return 0;
        else
            return 1;
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
}
