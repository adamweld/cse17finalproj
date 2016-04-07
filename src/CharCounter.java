import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author adamweld
 * @version Apr 7, 2016
 */
public class CharCounter
    implements ICharCounter
{
    /**
     * 256 bit array object to store values
     */
    private int[] array = new int[256];
    private int inbits;
    private final int BITS_PER_WORD = 16;


    /**
     * Returns the count associated with specified character.
     *
     * @param ch
     *            is the chunk/character for which count is requested
     * @return count of specified chunk
     * @throws some
     *             kind of exception if ch isn't a valid chunk/character
     */
    @Override
    public int getCount(int ch)
    {
        int ret = 0;
//        for(int i = 0; i < array.length; i++) {
//
//        }

        return ret;
    }


    /**
     * Initialize state by counting bits/chunks in a stream
     *
     * @param stream
     *            is source of data
     * @return count of all chunks/read
     * @throws IOException
     *             if reading fails
     */
    @Override
    public int countAll(InputStream stream)
        throws IOException
    {
        BitInputStream bits = new BitInputStream(new FileInputStream("poe.txt"));
        while ((inbits = bits.read(BITS_PER_WORD)) != -1)
        {

            System.out.println((char)inbits); // put writes one character
            add(inbits)

        }
    }


    /**
     * Update state to record one occurrence of specified chunk/character.
     *
     * @param i
     *            is the chunk being recorded
     */
    @Override
    public void add(int i)
    {
        array[i]++;
    }


    /**
     * Set the value/count associated with a specific character/chunk.
     *
     * @param i
     *            is the chunk/character whose count is specified
     * @param value
     *            is # occurrences of specified chunk
     */
    @Override
    public void set(int i, int value)
    {
        // TODO Auto-generated method stub

    }


    /**
     * All counts cleared to zero.
     */
    @Override
    public void clear()
    {
        // TODO Auto-generated method stub

    }

}
