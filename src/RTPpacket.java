public class RTPpacket extends RTPpacketDemo{
    public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length) {
        super(PType, Framenb, Time, data, data_length);
    }

    public RTPpacket(byte[] packet, int packet_size) {
        super(packet, packet_size);
    }

    @Override
    void setRtpHeader() {

    }
}
