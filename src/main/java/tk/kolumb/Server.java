package tk.kolumb;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;
import java.util.HashMap;

public class Server {
    public static void main(String[] args) throws InterruptedException {

//        HashMap<>

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9091);

        final SocketIOServer server = new SocketIOServer(config);
        BroadcastOperations myBroadcastOperations = server.getBroadcastOperations();
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
//                System.out.println(client.getHandshakeData());
//                System.out.println(client.getTransport());
                System.out.println(client.getRemoteAddress());
            }
        });
        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                System.out.println(data);
            }
        });
        server.addEventListener("hello", ChatObject.class, (client, data, ackRequest) -> {
            System.out.println(data.getUserName() + " hello");
            client.sendEvent("everybody", data);
            client.getSessionId();
        });
        server.addEventListener("down", ChatObject.class, (client, data, ackRequest) -> {
            System.out.println(data.getUserName() + " moving down");
            myBroadcastOperations.sendEvent("down", data);
        });
        server.addEventListener("up", ChatObject.class, (client, data, ackRequest) -> {
            System.out.println(data.getUserName() + " moving up");
            myBroadcastOperations.sendEvent("up", data);
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();

        /*System.out.println("started!!!");
        Socket socket = IO.socket("http://127.0.0.1:80");
        System.out.println("socketed!!!");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
            public void call(Object... args) {
                            System.out.println("connetcted!!!");
                socket.emit("foo", "hi");
                socket.disconnect();
            }
        }).on("clicking", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
        socket.connect();*/

        /*ServerSocket server = new ServerSocket(80);
        try {
            System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection...");
            Socket client = server.accept();
            System.out.println("A client connected.");
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            Scanner s = new Scanner(in, "UTF-8");
            try {
                String data = s.useDelimiter("\\r\\n\\r\\n").next();
                Matcher get = Pattern.compile("^GET").matcher(data);
                if (get.find()) {
                    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    match.find();
                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: "
                        + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                        + "\r\n\r\n").getBytes("UTF-8");
                    out.write(response, 0, response.length);
                    byte[] decoded = new byte[6];
                    byte[] encoded = new byte[] { (byte) 198, (byte) 131, (byte) 130, (byte) 182, (byte) 194, (byte) 135 };
                    byte[] key = new byte[] { (byte) 167, (byte) 225, (byte) 225, (byte) 210 };
                    for (int i = 0; i < encoded.length; i++) {
                        decoded[i] = (byte) (encoded[i] ^ key[i & 0x3]);
                    }
                    System.out.println(encoded);
                }
            } finally {
                s.close();
            }
        } finally {
            server.close();
        }*/
    }
}
