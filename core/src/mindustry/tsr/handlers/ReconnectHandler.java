package mindustry.tsr.handlers;

import arc.Core;
import mindustry.core.NetClient;

public class ReconnectHandler {
    public static void reconnect() {
        String ip = Core.settings.getString("currentserverip");
        int port = Core.settings.getInt("currentserverport");
        NetClient.disconnect();
        NetClient.connect(ip, port);
    }
}
