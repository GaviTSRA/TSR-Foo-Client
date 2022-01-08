package mindustry.tsr.ui;

import static arc.Core.bundle;
import static mindustry.Vars.ui;

import arc.Core;
import arc.math.Rand;
import arc.util.serialization.Base64Coder;
import mindustry.ui.dialogs.BaseDialog;

public class AddProfileDialog extends BaseDialog {
    String playername = "";
    String uuid = "";

    public AddProfileDialog() {
        super("@tsr.add_profile.title");
        shown(this::setup);
    }

    void setup() {
        cont.label(() -> Core.bundle.get("tsr.profile.name"));
        cont.field("", (text) -> {
            this.playername = text;
        });
        cont.row();

        String newUUID;
        byte[] result = new byte[8];
        new Rand().nextBytes(result);
        newUUID = new String(Base64Coder.encode(result));
        this.uuid  = newUUID;

        cont.label(() -> Core.bundle.get("tsr.profile.uuid"));
        cont.field(newUUID, (text) -> {
            this.uuid = text;
        });
        cont.row();

        addCloseButton();
        buttons.button("@tsr.profile.add", () -> {
            String profiles = Core.settings.getString("profiles");
            profiles += "," + this.playername + ":" + this.uuid;
            Core.settings.put("profiles", profiles);
            this.hide();
            ui.profileManagerDialog.hide();
            ui.profileManagerDialog = new ProfileManagerDialog();
            ui.profileManagerDialog.show();
        }).growX();
    }
}