import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.logging.Level;

public class Rtsp extends RtspDemo{
    public Rtsp(BufferedReader RTSPBufferedReader, BufferedWriter RTSPBufferedWriter, int rtpRcvPort, String rtspUrl, String videoFileName) {
        super(RTSPBufferedReader, RTSPBufferedWriter, rtpRcvPort, rtspUrl, videoFileName);
    }

    public Rtsp(BufferedReader RTSPBufferedReader, BufferedWriter RTSPBufferedWriter) {
        super(RTSPBufferedReader, RTSPBufferedWriter);
    }

    @Override
    boolean play() {
        if (state != State.READY) {
            logger.log(Level.WARNING, "RTSP state: " + state);
            return false;
        }
        RTSPSeqNb++;
        send_RTSP_request("PLAY");
        // Wait for the response
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.PLAYING;
            logger.log(Level.INFO, "New RTSP state: " + state + "\n");
            return true;
        }
    }

    @Override
    boolean pause() {
        if (state != State.PLAYING) {
            logger.log(Level.WARNING, "RTSP state: " + state);
            return false;
        }
        RTSPSeqNb++;
        send_RTSP_request("PAUSE");
        // Wait for the response
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.READY;
            logger.log(Level.INFO, "New RTSP state: " + state + "\n");
            return true;
        }
    }

    @Override
    boolean teardown() {
        if (state != State.PLAYING && state != State.READY) {
            logger.log(Level.WARNING, "RTSP state: " + state);
            return false;
        }
        RTSPSeqNb++;
        send_RTSP_request("TEARDOWN");
        // Wait for the response
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
            return false;
        } else {
            state = State.INIT;
            logger.log(Level.INFO, "New RTSP state: " + state + "\n");
            return true;
        }
    }

    @Override
    void describe() {
        RTSPSeqNb++;
        send_RTSP_request("DESCRIBE");
        // Wait for the response
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
        }
    }

    @Override
    void options() {
        RTSPSeqNb++;
        send_RTSP_request("OPTIONS");
        // Wait for the response
        logger.log(Level.INFO, "Wait for response...");
        if (parse_server_response() != 200) {
            logger.log(Level.WARNING, "Invalid Server Response");
        }
    }

    @Override
    void send_RTSP_request(String request_type) {
        try {
            switch (request_type) {
                case "OPTIONS" -> {
                    RTSPBufferedWriter.write("OPTIONS * RTSP/1.0\n");
                    RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+"\n\n");
                }
                case "DESCRIBE" -> {
                    RTSPBufferedWriter.write("DESCRIBE "+rtspUrl+VideoFileName+" RTSP/1.0\n");
                    RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+"\n\n");
                }
                case "SETUP" -> {
                    RTSPBufferedWriter.write("SETUP "+rtspUrl+VideoFileName+"/trackID=0 RTSP/1.0\n");
                    RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+"\n");
                    RTSPBufferedWriter.write("Transport: RTP/AVP;unicast;client_port=25000-25001\n\n");
                }
                case "PLAY","PAUSE","TEARDOWN" -> {
                    RTSPBufferedWriter.write(request_type+" " + rtspUrl + VideoFileName + " RTSP/1.0\n");
                    RTSPBufferedWriter.write("CSeq: " + RTSPSeqNb + "\n");
                    RTSPBufferedWriter.write("Session: 123456\n\n");
                }
            }
            RTSPBufferedWriter.flush();
        } catch (Exception e){
            System.exit(-1);
        }
    }
}
