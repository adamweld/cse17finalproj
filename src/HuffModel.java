import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
        // TODO Auto-generated method stub

    }


    /**
     * Display all chunk/character counts (via the associated view).
     */
    @Override
    public void showCounts()
    {
        // TODO Auto-generated method stub

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
        BitInputStream bits = new BitInputStream(new FileInputStream("poe.txt"));
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