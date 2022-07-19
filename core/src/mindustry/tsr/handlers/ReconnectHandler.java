package mindustry.tsr.handlers;

import arc.Core;
import mindustry.core.NetClient;

import static mindustry.Vars.*;

public class ReconnectHandler {
    public static void reconnect() {
        String ip = Core.settings.getString("currentserverip");
        int port = Core.settings.getInt("currentserverport");
        netClient.disconnectQuietly();
        netClient.connect(ip, port);
    }
}
