package udp.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UdpTest {
    EchoClient client;

    @Before
    public void setup(){
        new EchoServer(4444).start();
        client = new EchoClient(null, 4445);
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() {
        String echo = client.sendEcho("hello server", "localhost", "4444");
        assertEquals("hello server", echo);
        echo = client.sendEcho("server is working", "localhost", "4444");
        assertFalse(echo.equals("hello server"));
    }

    @After
    public void tearDown() {
        client.sendEcho("end", "localhost", "4445");
        client.close();
    }
}