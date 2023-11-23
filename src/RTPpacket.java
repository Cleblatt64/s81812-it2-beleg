public class RTPpacket extends RTPpacketDemo{
    public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length) {
        super(PType, Framenb, Time, data, data_length);
    }

    public RTPpacket(byte[] packet, int packet_size) {
        super(packet, packet_size);
    }

    @Override
    void setRtpHeader() {
        header[0] = (byte) (Version << 6);
        header[0] |= (byte) (Padding << 5);
        header[0] |= (byte) (Extension << 4);
        header[0] |= (byte) (CC);
        header[1] = (byte) (Marker << 7);
        header[1] |= (byte) (PayloadType);
        // sequence number
        header[2] = (byte) (SequenceNumber >> 8);
        header[3] = (byte) (SequenceNumber);
        // timestamp
        header[4] = (byte) (TimeStamp >> 24);
        header[5] = (byte) (TimeStamp >> 16);
        header[6] = (byte) (TimeStamp >> 8);
        header[7] = (byte) (TimeStamp);
        // synchro source
        header[8] = (byte) (Ssrc >> 24);
        header[9] = (byte) (Ssrc >> 16);
        header[10] = (byte) (Ssrc >> 8);
        header[11] = (byte) (Ssrc);
    }
}
