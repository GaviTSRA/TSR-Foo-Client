package mindustry.tsr.ui;

import arc.Core;
import arc.func.Cons;
import arc.scene.style.Drawable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.net.Administration;
import mindustry.ui.dialogs.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import static arc.Core.bundle;
import static mindustry.Vars.netServer;
import static mindustry.Vars.ui;

public class ProfileManagerDialog extends BaseDialog {
    public ProfileManagerDialog() {
        super("@tsr.profiles.title");
        shown(this::setup);
    }

    void setup(){
        cont.clear();
        buttons.clear();

        String profilesString = Core.settings.getString("profiles", "");
        ArrayList<String> profiles = new ArrayList<>();

        if (!profilesString.equals("") && !profilesString.equals(",")) {
            for (String profile : profilesString.split(",")) {
                profiles.add(profile);
            }
        } else {
            String profile = Core.settings.getString("name") + ":" + Core.settings.getString("uuid");
            profiles.add(profile);
            Core.settings.put("profiles", profile);
        }

        for(String profile : profiles) {
            String profileName = profile.split(":")[0];
            String profileUUID = profile.split(":")[1];
            String profileString = profileName + ":" + profileUUID;

            cont.label(() -> profileName);
            cont.button("@tsr.profile.load", () -> {
                Core.settings.put("name", profileName);
                Core.settings.put("uuid", profileUUID);
                this.hide();
            }).growX();
            cont.button("@tsr.profile.delete", () -> {
                DeleteProfileDialog deleteDialog = new DeleteProfileDialog(profileString);
                deleteDialog.show();
            }).growX();
            cont.row();
        }
        buttons.button("@tsr.profile.add", () -> {
            AddProfileDialog dialog = new AddProfileDialog();
            dialog.show();
        }).width(300).height(50);
        addCloseButton();
    }
}