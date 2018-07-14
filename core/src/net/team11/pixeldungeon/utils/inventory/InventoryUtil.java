package net.team11.pixeldungeon.utils.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.inventory.skinselect.SkinList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InventoryUtil {
    private static InventoryUtil INSTANCE;
    private SkinList skinList;
    private HashMap<Integer,Skin> skins;

    private InventoryUtil() {
//        clearLocal();
        skins = readSkins();
//        skinList = parseSkinList();
    }

    private HashMap<Integer,Skin> readSkins() {
        HashMap<Integer,Skin> list = new HashMap<>();
        Json json = new Json();
        for (FileHandle file : Gdx.files.internal("shop/shopitems/skins").list()) {
            if (file.toString().endsWith(".json")) {
                Skin skin = json.fromJson(Skin.class,file);
                list.put(skin.getId(),skin);
            }
        }
        return list;
    }

    public void setSkinList(SkinList skinList){
        this.skinList = skinList;
    }

    private SkinList parseSkinList() {
        String filePath = "shop/shopitems/skinList.json";

        Json json = new Json();
        SkinList internalList = json.fromJson(SkinList.class,
                Gdx.files.internal(filePath));

        if (Gdx.files.local("shop/shopitems/").isDirectory()) {
            SkinList localList = json.fromJson(SkinList.class,
                    Gdx.files.local(filePath));

            if (localList.getVersion() < internalList.getVersion()) {
                for (String skin : internalList.getSkinList()) {
                    internalList.setSkin(skin,localList.hasSkin(skin));
                }
                Gdx.files.local(filePath).writeString(json.toJson(internalList),false);
            } else {
                internalList = localList;
            }
        } else {
            Gdx.files.local(filePath).writeString(json.toJson(internalList),false);
        }

        return internalList;
    }

    public void save() {
        String filePath = "shop/shopitems/skinList.json";

        Json json = new Json();
        Gdx.files.local(filePath).writeString(json.toJson(skinList),false);
    }

    public HashMap<Integer,Skin> getSkins() {
        return skins;
    }

    public SkinList getSkinList() {
        return skinList;
    }

    private void clearLocal() {
        Gdx.files.local("shop").deleteDirectory();
    }

    public static InventoryUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryUtil();
        }
        return INSTANCE;
    }
}
