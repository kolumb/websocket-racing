package tk.kolumb;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;
import java.util.HashMap;
import java.util.UUID;

public class Server {
    public static void main(String[] args) throws InterruptedException {

        HashMap<UUID, User> users = new HashMap<>();
        HashMap<UUID, String> userNames = new HashMap<>();

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
                String sessionId = client.getSessionId().toString();
                HandshakeData handshakeData = client.getHandshakeData();
                System.out.println("Client["+ sessionId +"] - Connected to chat module through " + handshakeData.getUrl());
                System.out.println(client.getRemoteAddress() + " " + handshakeData.getUrlParams().get("t"));//.get(0));
                users.put(client.getSessionId(), new User());
                userNames.put(client.getSessionId(), users.get(client.getSessionId()).getUserName());

                String name = userNames.get(client.getSessionId());
                myBroadcastOperations.sendEvent("new user", (Object) new String[]{client.getSessionId().toString(), name});
                client.sendEvent("everybody", userNames);
                client.sendEvent("you", sessionId);
//                client.sendEvent("everybody", users);
                //System.out.println(userNames);

            }
        });
        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
            }
        });
        server.addEventListener("down", ChatObject.class, (client, data, ackRequest) -> {
            System.out.println(data.getUserName() + " moving down");
            myBroadcastOperations.sendEvent("down", data);
        });
        server.addEventListener("up", ChatObject.class, (client, data, ackRequest) -> {
            System.out.println(data.getUserName() + " moving up");
            myBroadcastOperations.sendEvent("up", data);
        });
        server.addEventListener("new name", ChatObject.class, (client, data, ackRequest) -> {
            if(data.getUserName() == null) return;
            System.out.println( users.get(client.getSessionId()).getUserName() + " has new name: " + data.getUserName());
            myBroadcastOperations.sendEvent("new name", (Object) new String[]{client.getSessionId().toString(), data.getUserName()});
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("disconnected: " + users.get(client.getSessionId()));
                client.sendEvent("disconnected", users.get(client.getSessionId()));
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
