import java.util.HashMap;

public class FecHandler extends FecHandlerDemo{
    public FecHandler(int size) {
        super(size);
    }

    public FecHandler(boolean useFec) {
        super(useFec);
    }

    @Override
    boolean checkCorrection(int nr, HashMap<Integer, RTPpacket> mediaPackets) {
        return false;
    }

    @Override
    RTPpacket correctRtp(int nr, HashMap<Integer, RTPpacket> mediaPackets) {
        return null;
    }
}
