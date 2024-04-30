import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.InetAddress;
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Client.Client;
import Main.Json;
import Main.Main;

public class MainTest {

    @Test
    public void testGetJsonObject() {
        String message = "command arg1 arg2";
        String expected = "{\"robot\":\"\",\"command\":\"command\",\"arguments\":[arg1, arg2]}";
        String actual = Client.getJsonObject(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testReader() {
        String jsonString = "{\"data\":{\"message\":\"Test message\"}}";
        String fileName = "test.json";
        Json.reader(fileName, jsonString);
    }

    @BeforeEach
    void setUp() {
        Main.getRobots().clear();
        Main.getGulag().clear();
    }

    @Test
    public void testRunClientCommand_NoExistingRobot() {
        InetAddress clientAddress = mockInetAddress();
        String clientMessage = "command arg1 arg2";
        Main.runClientCommand(clientAddress, clientMessage);
        assertEquals(clientAddress, Main.getClientAddress());
        assertNotNull(Main.getClientResponse());
    }

    @Test
    public void testSetGulag() {
        InetAddress address = mockInetAddress();
        String name = "Robot1";
        Main.setGulag(address, name);
        Map<InetAddress, String> gulag = Main.getGulag();
        assertEquals(name, gulag.get(address));
    }

    @Test
    public void testRunServerCommand() {
        String clientMessage = "command arg1 arg2";
        Main.runServerCommand(clientMessage);
        assertNull(Main.getClientAddress());
    }

    @Test
    public void testSetClientResponse() {
        String response = "Response";
        Main.setClientResponse(response);
        assertEquals(response, Main.getClientResponse());
    }

    private InetAddress mockInetAddress() {
        try {
            return InetAddress.getLocalHost();
        }
        catch (Exception e) {
            return null;
        }
    }
}