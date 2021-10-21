package mindustry.tsr.handlers;

import arc.util.Log;
import mindustry.Vars;
import mindustry.gen.Call;

public class ClientSideJSCommandHandler {

    public static void handleClientSideJS(String js) {
        Log.info("Running client side js:" + js);
        Vars.mods.getScripts().runConsole(js);
    }
}
