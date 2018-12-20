package net.mtgsaber.minecraft.packinstallers.dwt2;

import javafx.util.Pair;
import net.mtgsaber.lib.sql.SQLHandler;
import net.mtgsaber.minecraft.packinstallers.dwt2.dat.Data;
import net.mtgsaber.minecraft.packinstallers.dwt2.util.DLFileTask;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Author: Andrew Arnold (10/1/2018)
 *
 * All of the important gui-independent processes are defined here.
 * Each process can be ran as Runnable for multithreading compatibility.
 * Each process name is preceded by a common programming term or
 * common UNIX terminal command, such as 'cp', 'rm', or 'get' to indicate the type of process.
 */
public enum Process implements Runnable {
    /**
     * Initializes the SQL handler.
     *
     * Done, tested 12/18/2018.
     */
    INIT_SQL_HANDLER (() -> {
        char[] pswd = new char[SQLQuery.PSWD.length];
        for (int i=0; i < SQLQuery.PSWD.length; i++) {
            int j;
            if (i%2==0)
                j = ((SQLQuery.PSWD[i] - (SQLQuery.PSWD.length*(i+1)))%128);
            else
                j = ((SQLQuery.PSWD[i] - (SQLQuery.PSWD.length*SQLQuery.PSWD.length*(i+1)))%128);
            if (j < 0)
                j += 128;
            pswd[i] = (char)j;
        }
        Properties props = new Properties();
        props.setProperty("username", SQLQuery.USER);
        props.setProperty("password", new String(pswd));
        Data.SQL_HANDLER = new SQLHandler(SQLQuery.ADDR, SQLHandler.Driver.SQL_SERVER, props);
        props = null;
        pswd = null;
    }),

    // CLIENT PROCESSES:

    /**
     * Use stdlib to determine the user's home directory and therefore their Minecraft launcher installation.
     *
     * DONE
     */
    GET_HOME_DIR (() -> {
        writeStrBuf(Data.STRBF_HOME_DIR, System.getProperty("user.home"));
    }),

    /**
     * Reads info from the app's data.json
     */
    READ_INST_DATA (() -> {

    }),

    /**
     * Uses SQL queries to get pack list, pack metadata, and pack mods.
     */
    QUERY_PACK_DATA (() -> {
        Data.SQL_HANDLER.query(SQLQuery.PACK_INFO.PARTS[0]);
        if (Data.SQL_HANDLER.lastQuery() != null)
            for (ArrayList<String> row : Data.SQL_HANDLER.lastQuery())
                Data.PACKS.add(new Vector<>(row));
    }),

    /**
     * Uses SQL queries to get mod files & their URLs, and config URL.
     *
     * Done, not tested.
     * todo: test
     */
    QUERY_PACK_DETAILS (() -> {
        Data.SQL_HANDLER.query(
                SQLQuery.PACK_DETAILS.PARTS[0]
                        + Data.STRBF_PACK_SELECTION
                        + SQLQuery.PACK_DETAILS.PARTS[1]
        );
        if (Data.SQL_HANDLER.lastQuery() != null)
            Data.PACK_DET.addAll(Data.SQL_HANDLER.lastQuery().get(0));

        Data.SQL_HANDLER.query(
                SQLQuery.PACK_MODS.PARTS[0]
                        + Data.STRBF_PACK_SELECTION
                        + SQLQuery.PACK_MODS.PARTS[1]
        );
        if (Data.SQL_HANDLER.lastQuery() != null)
            for (ArrayList<String> rsRow : Data.SQL_HANDLER.lastQuery())
                Data.MODSFILES.add(new Vector<>(rsRow));
    }),

    /**
     * Checks folder structure for selected pack
     *
     * todo: error handling, test
     */
    CHECK_FOLDER_STRUCT (() -> {
        Data.DIR_SELECTED_PACK = null;
        Data.DIR_MODS = null;
        if (!Data.DIR_MODPACKS.isDirectory()) {
            try {
                Files.createDirectory(Data.DIR_MODPACKS.toPath());
                Data.DIR_SELECTED_PACK = new File(
                        Data.DIR_MODPACKS.getAbsolutePath()
                                + File.pathSeparator
                                + Data.PACK_DET.get(0)
                );
            } catch (IOException ioex) {
                // todo: catch
            }
        }
        if (Data.DIR_SELECTED_PACK != null && !Data.DIR_SELECTED_PACK.isDirectory()) {
            try {
                Files.createDirectory(Data.DIR_SELECTED_PACK.toPath());
                Data.DIR_MODS = new File(
                        Data.DIR_SELECTED_PACK.getAbsolutePath()
                                + File.pathSeparator
                                + "mods"
                );
            } catch (IOException ioex) {
                // todo: catch
            }
        }
        if (Data.DIR_MODS != null && !Data.DIR_MODS.isDirectory()) {
            try {
                Files.createDirectory(Data.DIR_MODS.toPath());
            } catch (IOException ioex) {
                // todo: catch
            }
        }
    }),

    /**
     * Gets current installed version from this apps data.json, and checks it against the current version from DB.
     */
    CHECK_CUR_VER (() -> {

    }),

    /**
     * Checks whether this version's mods are installed correctly.
     *
     * Done, not tested.
     * todo: test
     */
    CHECK_CORRECT_MODS_PRESENT (() -> {
        Data.PACK_MOD_ABS_FILES.clear();
        Data.BAD_MODS.clear();
        Data.MODS_MISSING.clear();
        if (Data.PACKS.size() > 0 && Data.PACKS.firstElement().size() == 6) {
            if (Data.PACK_DET.size() == 8) {
                if (Data.DIR_MODS != null && Data.DIR_MODS.isDirectory()) {
                    Data.isModsCorrect = true;
                    Data.MODSFILES.iterator().forEachRemaining(
                            (Vector<String> strings) -> Data.PACK_MOD_ABS_FILES.add(new File(
                                    Data.DIR_MODS.getAbsolutePath()
                                            + File.pathSeparator
                                            + strings.get(2)
                            ))
                    );
                    final File[] LOCAL_MODS = Data.DIR_MODS.listFiles(
                            (File pathname) -> pathname.getName().endsWith(".jar")
                    );
                    final LinkedList<File>
                            PACK_MODS_MISSING = new LinkedList<>(Data.PACK_MOD_ABS_FILES);
                    if (LOCAL_MODS != null) {
                        final LinkedList<File>
                                BAD_LOCAL_MODS = new LinkedList<>(Arrays.asList(LOCAL_MODS));
                        PACK_MODS_MISSING.removeIf(
                                (File file) -> Arrays.binarySearch(
                                        LOCAL_MODS, file,
                                        Comparator.naturalOrder()
                                ) >= 0
                        );
                        BAD_LOCAL_MODS.removeIf(
                                file -> Data.PACK_MOD_ABS_FILES.contains(file)
                        );
                        Data.BAD_MODS.addAll(BAD_LOCAL_MODS);
                        if (BAD_LOCAL_MODS.size() > 0)
                            Data.isModsCorrect = false;
                    }
                    Data.MODS_MISSING.addAll(PACK_MODS_MISSING);
                    if (PACK_MODS_MISSING.size() > 0)
                        Data.isModsCorrect = false;
                }
            }
        }
    }),

    /**
     * Remove all mod jars that match the list of removed mods from this version's configuration.
     *
     * Done, not tested.
     * todo: test
     */
    RM_BAD_MODS (() -> {
        for (File toRemove : Data.BAD_MODS)
            if (toRemove.isFile()) {
                try {
                    Files.delete(toRemove.toPath());
                } catch (IOException ioex) {
                    // todo: catch
                }
            }
    }),

    /**
     * Removes all contents of config folder.
     *
     * Done, not tested.
     * todo: error handling, test
     */
    RM_CFG (() -> {
        if (Data.DIR_SELECTED_PACK != null && Data.DIR_SELECTED_PACK.isDirectory()) {
            final File DIR_CFG = new File(
                    Data.DIR_SELECTED_PACK.getAbsolutePath()
                            + File.pathSeparator
                            + "config"
            );
            if (DIR_CFG.isDirectory()) {
                try {
                    Files.delete(DIR_CFG.toPath());
                } catch (IOException ioex) {
                    // todo: catch
                }
            }
        }
    }),

    /**
     * Downloads all missing mods to a download directory.
     *
     * todo: finish.
     */
    DLD_MODS (() -> {
        if (Data.DIR_MODS != null && Data.DIR_MODS.isDirectory() && Data.MODS_MISSING.size() > 0) {
            final LinkedList<Pair<String, File>> ARGS = new LinkedList<>();
            Data.MODS_MISSING.iterator().forEachRemaining(file -> {
                if (!file.isFile()) {
                    int preSize = ARGS.size();
                    Data.MODSFILES.iterator().forEachRemaining(strings -> {
                        if (file.getName().equals(strings.get(2))) {
                            boolean found = false;
                            for (Pair<String, File> pair : ARGS)
                                if (pair.getValue().equals(file))
                                    found = true;
                            if (!found)
                                ARGS.add(new Pair<>(strings.get(3), file));
                        }
                    });
                    if (preSize == ARGS.size()) {
                        // todo: handle data corruption.
                    }
                }
            });
            if (ARGS.size() > 0) {
                if (!Data.CLK_MAIN.isRunning && Data.THRD_CLK_MAIN.getState().equals(Thread.State.NEW))
                    Data.THRD_CLK_MAIN.start();

                final DLFileTask[] TASKS = new DLFileTask[ARGS.size()];
                for (int i = 0; i < TASKS.length; i++) {
                    final Pair<String, File> ARG = ARGS.get(i);
                    TASKS[i] = new DLFileTask(ARG.getKey(), ARG.getValue());
                }
                final DLFileTask.DLFileTaskMaster dlFileTaskMaster = new DLFileTask.DLFileTaskMaster(TASKS);
                dlFileTaskMaster.start();
                Data.CLK_MAIN.addTickable(dlFileTaskMaster);
            }
        }
    }),

    /**
     * Download the Config Zip file into download directory.
     */
    DLD_CFG (() -> {

    }),

    /**
     * Downloads the Profile.json from Drive.
     */
    DLD_PROFILE (() -> {}),

    /**
     * Install the mods from this version that aren't already installed.
     */
    CP_NEW_MODS (() -> {}),

    /**
     * Extracts the Config Zip file from download directory into install directory / config.
     */
    EXTRACT_CFG (() -> {}),

    /**
     * Read the launcher_profiles.json file to confirm if there is a valid profile for this modpack
     * to prevent duplicates during updates. This also reports if the profile's fields
     * match the field values in this version's config_[version].json.
     */
    CHECK_PROFILE (() -> {}),

    /**
     * Removes a profile from the profiles.json
     */
    RM_PROFILE (() -> {}),

    /**
     * Inject a new profile into the user's launcher_profiles.json with the fields defined in this version's config.
     */
    WRITE_PROFILE (() -> {}),

    /**
     * Uses SQL to report to the database that this user has used a pack.
     */
    QUERY_USER_INFO (() -> {}),

    /**
     * Read the selected version's config_[version].json and load its values.
     *
     * DONE
     *
     * @deprecated
     */
    READ_CONFIG (() -> {
        /*
        File jsonFile = new File(
                "modpacks\\"
                        + Data.STRBF_VERSION_SELECTION.toString()
                        + "\\config_"
                        + Data.STRBF_VERSION_SELECTION.toString()
                        + ".json"
        );
        if (jsonFile.isFile()) {
            try {
                Data.curConfig = Data.GSON.fromJson(
                        new FileReader(jsonFile),
                        DWT2VersionConfig.class
                );
            } catch (FileNotFoundException | JsonParseException ex) {
                ex.printStackTrace();
                Data.isBadConfigFile = true;
            }
        } else
            Data.isBadConfigFile = true;
        */
    }),

    // HOST PROCESSES:
    ;

    private final Runnable RUNNABLE;

    Process(Runnable runnable) {
        this.RUNNABLE = runnable;
    }

    @Override
    public void run() {
        RUNNABLE.run();
    }

    public static void writeStrBuf(StringBuffer strbuf, String str) {
        strbuf.replace(0, strbuf.length(), str);
    }
}
