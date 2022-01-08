package mindustry.tsr.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Boolp;
import arc.graphics.Color;
import arc.graphics.g2d.Font;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.event.*;
import arc.scene.style.Drawable;
import arc.scene.style.Style;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.scene.utils.Disableable;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.pooling.Pools;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static arc.Core.bundle;
import static mindustry.Vars.maxNameLength;

public class AdvancedSettingsTable extends SettingsMenuDialog.SettingsTable {

    Boolean addResetButton;

    public AdvancedSettingsTable(Boolean addResetButton) {
        this.left();
        this.addResetButton = addResetButton;
    }

    public void textPref(String name, String def) {
        this.list.add(new TextSetting(name, def));
        this.rebuild();
    }

    public void category(String name){
        pref(new Category(bundle.get("setting." + name + ".name")));
        this.rebuild();
    }

    public void addButton(String name, Runnable func) {
        this.list.add(new Button(bundle.get("setting." + name + ".name"), func));
        this.rebuild();
    }

    public static class TextSetting extends Setting {
        String def;
        String label;

        TextSetting(String name, String def) {
            super(name);
            this.label = label;
            this.name = name;
            this.def = def;
        }

        public void add(SettingsMenuDialog.SettingsTable table) {
            table.table(tt -> {
                table.add(bundle.get("setting." + name + ".name")).left();
                tt.field(Core.settings.getString(this.name, this.def), text -> {
                    Core.settings.put(this.name, text);
                }).growX();
            }).left().fillX().padTop(3).height(32);
            table.row();
        }
    }

    public static class Category extends Setting{
        Category(String name){
            super(name);
            this.name = name;
            this.title = (name);
        }

        @Override
        public void add(SettingsMenuDialog.SettingsTable table){
            table.add("[accent]" + title).center();
            table.row();
        }
    }

    public static class Button extends Setting {
        String name;
        Runnable func;

        Button(String name, Runnable func) {
            super(name);
            this.name = name;
            this.func = func;
        }

        public void add(SettingsMenuDialog.SettingsTable table) {
            table.button(name, func).center().width(300).height(50);
            table.row();
        }
    }

    public void rebuild() {
        this.clearChildren();

        for (Setting setting : this.list) {
            setting.add(this);
        }

        if (this.addResetButton) {
            this.button(bundle.get("settings.reset", "Reset to Defaults"), () -> {
                for (Setting setting : this.list) {
                    if (setting.name != null && setting.title != null) {
                        Core.settings.put(setting.name, Core.settings.getDefault(setting.name));
                    }
                }
                this.rebuild();
            }).margin(14.0F).width(240.0F).pad(6.0F);
        }
    }
}