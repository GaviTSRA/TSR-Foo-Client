package mindustry.world.meta;

import arc.Core;
import arc.func.*;
import mindustry.*;

public enum BuildVisibility{
    hidden(() -> Core.settings.getBool("showallblocks")),
    shown(() -> true),
    debugOnly(() -> Core.settings.getBool("showallblocks")),
    editorOnly(() -> Core.settings.getBool("showallblocks")),
    sandboxOnly(() -> Vars.state == null || Vars.state.rules.infiniteResources || Core.settings.getBool("showallblocks")),
    campaignOnly(() -> Vars.state == null || Vars.state.isCampaign() || Core.settings.getBool("showallblocks")),
    lightingOnly(() -> Vars.state == null || Vars.state.rules.lighting || Vars.state.isCampaign() || Core.settings.getBool("showallblocks")),
    ammoOnly(() -> Vars.state == null || Vars.state.rules.unitAmmo || Core.settings.getBool("showallblocks"));

    private final Boolp visible;

    public boolean visible(){
        return visible.get();
    }

    BuildVisibility(Boolp visible){
        this.visible = visible;
    }
}
