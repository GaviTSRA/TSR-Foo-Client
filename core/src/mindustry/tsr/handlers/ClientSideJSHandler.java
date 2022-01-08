package mindustry.tsr.handlers;

import arc.util.Log;
import mindustry.Vars;

public class ClientSideJSHandler {
    public static void handleClientSideJS(String js) {
        Log.info("Running client side js:" + js);
        Vars.mods.getScripts().runConsole(js);
    }
}
