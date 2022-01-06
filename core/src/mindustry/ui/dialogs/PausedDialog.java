package mindustry.ui.dialogs;

import arc.*;
import arc.math.Rand;
import arc.util.serialization.Base64Coder;
import mindustry.client.*;
import mindustry.client.ui.*;
import mindustry.gen.*;
import mindustry.tsr.handlers.ReconnectHandler;
import mindustry.tsr.ui.ProfileManagerDialog;

import java.util.Random;

import static mindustry.Vars.*;

public class PausedDialog extends BaseDialog{
    private SaveDialog save = new SaveDialog();
    private LoadDialog load = new LoadDialog();
    private boolean wasClient = false;

    public PausedDialog(){
        super("@menu");
        shouldPause = true;

        shown(this::rebuild);

        addCloseListener();
    }

    void rebuild(){
        cont.clear();

        update(() -> {
            if(state.isMenu() && isShown()){
                hide();
            }
        });

        if(!mobile){
            float dw = 220f;
            cont.defaults().width(dw).height(55).pad(5f);

            cont.button("@back", Icon.left, this::hide).name("back");
            cont.button("@settings", Icon.settings, ui.settings::show).name("settings");

            if(!state.isCampaign() && !state.isEditor()){
                cont.row();
                cont.button("@savegame", Icon.save, save::show);
                cont.button("@loadgame", Icon.upload, load::show).disabled(b -> net.active());
            }

            cont.row();

            cont.button("@hostserver", Icon.host, () -> {
                if(net.server() && steam){
                    platform.inviteFriends();
                }else{
                    if(steam){
                        ui.host.runHost();
                    }else{
                        ui.host.show();
                    }
                }
            }).disabled(b -> !((steam && net.server()) || !net.active())).update(e -> e.setText(net.server() && steam ? "@invitefriends" : "@hostserver"));
            cont.button("@tsr.reconnect", Icon.host, ReconnectHandler::reconnect);

            cont.row();

            cont.button("@client.changelog", Icon.edit, ChangelogDialog.INSTANCE::show);
            cont.button("@client.features", Icon.book, FeaturesDialog.INSTANCE::show);

            cont.row();

            cont.button("@tsr.profiles.manage", Icon.book, () -> {
                ui.profileManagerDialog = new ProfileManagerDialog();
                ui.profileManagerDialog.show();
            });
            cont.button("@tsr.profile.random", Icon.refresh, () -> {
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
                Call.infoMessage("Assigned random profile");
            });

            cont.row();

            cont.button("@client.keyshare", Icon.lock, () -> new KeyShareDialog(Main.messageCrypto).show());
            cont.button("@quit", Icon.exit, this::showQuitConfirm).update(s -> s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit"));


        }else{
            cont.defaults().size(130f).pad(5);
            cont.buttonRow("@back", Icon.play, this::hide);
            cont.buttonRow("@settings", Icon.settings, ui.settings::show);

            if(!state.isCampaign() && !state.isEditor()){
                cont.buttonRow("@save", Icon.save, save::show);

                cont.row();

                cont.buttonRow("@load", Icon.download, load::show).disabled(b -> net.active());
            }else if(state.isCampaign()){
                cont.buttonRow("@research", Icon.tree, ui.research::show);

                cont.row();

                cont.buttonRow("@planetmap", Icon.map, () -> {
                    hide();
                    ui.planet.show();
                });
            }else{
                cont.row();
            }

            cont.buttonRow("@hostserver.mobile", Icon.host, ui.host::show).disabled(b -> net.active());

            cont.buttonRow("@quit", Icon.exit, this::showQuitConfirm).update(s -> {
                s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit");
                s.getLabelCell().growX().wrap();
            });
        }
    }

    void showQuitConfirm(){
        ui.showConfirm("@confirm", "@quit.confirm", () -> {
            wasClient = net.client();
            if(net.client()) netClient.disconnectQuietly();
            runExitSave();
            hide();
        });
    }

    public void runExitSave(){
        if(state.isEditor() && !wasClient){
            ui.editor.resumeEditing();
            return;
        }

        if(control.saves.getCurrent() == null || !control.saves.getCurrent().isAutosave() || wasClient){
            logic.reset();
            return;
        }

        ui.loadAnd("@saving", () -> {
            try{
                control.saves.getCurrent().save();
            }catch(Throwable e){
                e.printStackTrace();
                ui.showException("[accent]" + Core.bundle.get("savefail"), e);
            }
            logic.reset();
        });
    }
}
