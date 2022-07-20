package mindustry.tsr.handlers;

import arc.Core;
import arc.math.Rand;
import arc.util.serialization.Base64Coder;

import java.util.Random;

public class RandomProfileManager {
    public static void useRandomProfile() {
        String newUUID;
        byte[] result = new byte[8];
        new Rand().nextBytes(result);
        newUUID = new String(Base64Coder.encode(result));
        Core.settings.put("uuid", newUUID);

        String newName;
        String[] nameParts1 = {"Luk", "Random", "Name", "Freeeeee", "|Yes|", "BBBB", "I am ", "cl", "Anifan", "MAIf", "anf"};
        String[] nameParts2 = {".org", "in", "or", "chuck", "-1", "|Why|", "AAAA", " - the only one", "as", "rooter", "dead"};
        newName = nameParts1[new Random().nextInt(nameParts1.length)];
        newName += nameParts2[new Random().nextInt(nameParts2.length)];
        Core.settings.put("name", newName);
    }
}
