package net.mtgsaber.minecraft.packinstallers.dwt2.dat;

import com.google.gson.Gson;
import net.mtgsaber.lib.Clock;
import net.mtgsaber.lib.TaskStack;
import net.mtgsaber.lib.sql.SQLHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Author: Andrew Arnold (10/1/2018)
 */
public class Data {
    public static final Gson GSON = new Gson();
    public static volatile SQLHandler SQL_HANDLER;
    public static final Clock CLK_MAIN = new Clock(250);
    public static final Thread THRD_CLK_MAIN = new Thread(
            CLK_MAIN,
            "Main Clock Thread"
    );

    public static final File
            JSON_DATA = new File("./Cfg.json"),
            DIR_MODPACKS = new File("./Packs/");

    public static final StringBuffer
            STRBF_HOME_DIR = new StringBuffer(),
            STRBF_PACK_SELECTION = new StringBuffer(),
            STRBF_VERSION_SELECTION = new StringBuffer()
            ;

    public static final Vector<Vector<String>>
            PACKS = new Vector<>(),
            MODSFILES = new Vector<>()
            ;

    public static final Vector<String>
            PACK_DET = new Vector<>();

    public static final Vector<File>
            PACK_MOD_ABS_FILES = new Vector<>(),
            BAD_MODS = new Vector<>(),
            MODS_MISSING = new Vector<>();

    public static volatile File
            DIR_SELECTED_PACK,
            DIR_MODS
            ;

    public static volatile boolean
            isErr,
            isInit,
            isVerCurrent,
            isModsCorrect,
            isModsDLDed,
            isCfgDLDed,
            isModsCp,
            isCfgExtracted
            ;


}
