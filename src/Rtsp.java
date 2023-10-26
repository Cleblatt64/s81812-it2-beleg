import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Rtsp extends RtspDemo{
    public Rtsp(BufferedReader RTSPBufferedReader, BufferedWriter RTSPBufferedWriter, int rtpRcvPort, String rtspUrl, String videoFileName) {
        super(RTSPBufferedReader, RTSPBufferedWriter, rtpRcvPort, rtspUrl, videoFileName);
    }

    public Rtsp(BufferedReader RTSPBufferedReader, BufferedWriter RTSPBufferedWriter) {
        super(RTSPBufferedReader, RTSPBufferedWriter);
    }

    @Override
    boolean play() {
        return false;
    }

    @Override
    boolean pause() {
        return false;
    }

    @Override
    boolean teardown() {
        return false;
    }

    @Override
    void describe() {

    }

    @Override
    void options() {

    }

    @Override
    void send_RTSP_request(String request_type) {

    }

    @Override
    String getOptions() {
        return null;
    }

    @Override
    String getDescribe(VideoMetadata meta, int RTP_dest_port) {
        return null;
    }
}
