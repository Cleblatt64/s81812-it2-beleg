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
        if (fecList.get(fecNr.get(nr)) == null || fecList.get(nr) == null) return false;
        int c=0;
        for (int i : fecList.get(nr)){
            if (mediaPackets.get(i) == null) c++;
        }
        return c<=1;
    }

    @Override
    RTPpacket correctRtp(int nr, HashMap<Integer, RTPpacket> mediaPackets) {

        FECpacket p = fecStack.get(fecNr.get(nr));
        for (int i : fecList.get(nr)){
            if (i == nr) continue;
            p.addRtp(mediaPackets.get(i));
        }
        return p.getLostRtp(nr);
    }
}
