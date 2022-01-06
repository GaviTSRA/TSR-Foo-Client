package mindustry.tsr.ui;

import arc.Core;
import arc.math.Rand;
import arc.util.serialization.Base64Coder;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.ui;

public class DeleteProfileDialog extends BaseDialog {
    String profileString = "";

    public DeleteProfileDialog(String profileString) {
        super("@tsr.del_profile.title");
        this.profileString = profileString;
        shown(this::setup);
    }

    void setup() {
        cont.label(() -> Core.bundle.get("tsr.del_profile.ask"));

        addCloseButton();
        buttons.button("@tsr.del_profile.delete", () -> {
            String profiles = Core.settings.getString("profiles");
            String newProfiles = "";

            for (String profile : profiles.split(",")) {
                System.out.println(profileString);
                System.out.println(profile);
                if (!profile.equals(profileString)) {
                    if (newProfiles.equals("")) newProfiles += profile;
                    else newProfiles += "," + profile;
                }
            }

            Core.settings.put("profiles", newProfiles);
            this.hide();
            ui.profileManagerDialog.hide();
        }).growX();
    }
}
