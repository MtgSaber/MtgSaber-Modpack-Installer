package net.mtgsaber.minecraft.packinstallers.dwt2;

/**
 * Author: Andrew Arnold (12/15/2018)
 */
public enum SQLQuery {
    PACK_INFO (
            "Pack Info",
            "select\n" +
                    "\tA.Name as 'Pack Name',\n" +
                    "\tA.Ver as 'Pack Version',\n" +
                    "\tC.Ver as 'MC Version',\n" +
                    "\tB.Ver as 'Forge Version',\n" +
                    "\tD.ModCnt as 'Mods',\n" +
                    "\tE.UsrCnt as 'Users'\n" +
                    "from Pack as A\n" +
                    "\tjoin ForgeInstFile as B\n" +
                    "\t\ton A.ForgeInstFileID = B.ForgeInstFileID\n" +
                    "\tjoin MCVersion as C\n" +
                    "\t\ton B.MCVersionID = C.MCVersionID\n" +
                    "\tjoin (\n" +
                    "\t\tselect F.PackID, count(F.PackID) as ModCnt\n" +
                    "\t\tfrom Pack_MCMod as F\n" +
                    "\t\tgroup by F.PackID\n" +
                    "\t) as D\n" +
                    "\t\ton A.PackID = D.PackID\n" +
                    "\tjoin (\n" +
                    "\t\tselect G.PackID, count(G.PackID) as UsrCnt\n" +
                    "\t\tfrom Usr_Pack as G\n" +
                    "\t\tgroup by G.PackID\n" +
                    "\t) as E\n" +
                    "\t\ton A.PackID = E.PackID"
    ),
    PACK_MODS (
            "select\n" +
                    "\tA.Name as 'Mod Name',\n" +
                    "\tA.PrjURL as 'Mod Project',\n" +
                    "\tB.FIleName as 'Mod File Name',\n" +
                    "\tB.FileURL as 'Mod File URL'\n" +
                    "from MCMod as A\n" +
                    "\tjoin Pack_MCMod as B\n" +
                    "\t\ton A.MCModID = B.MCModID\n" +
                    "\tjoin (\n" +
                    "\t\tselect top 1 PackID\n" +
                    "\t\tfrom Pack as D\n" +
                    "\t\twhere D.Name = '",
            "'\n" +
                    "\t) as C\n" +
                    "\t\ton B.PackID = C.PackID"
    ),
    PACK_DETAILS (
            "select\n" +
                    "\tA.Dir,\n" +
                    "\tA.LastUpdated,\n" +
                    "\tA.ProfileFileName,\n" +
                    "\tA.ProfileURL,\n" +
                    "\tA.CfgZipFileName,\n" +
                    "\tA.CfgZipFileURL,\n" +
                    "\tB.FileName,\n" +
                    "\tB.FileURL\n" +
                    "from Pack as A\n" +
                    "\tjoin ForgeInstFile as B\n" +
                    "\t\ton A.ForgeInstFileID = B.ForgeInstFileID\n" +
                    "where A.Name = '",
            "'"
    ),
    ;

    SQLQuery(String NAME, String... PARTS) {
        this.NAME = NAME;
        this.PARTS = PARTS;
    }

    public final String NAME;
    public final String[] PARTS;
    public static final String
            ADDR = "den1.mssql7.gear.host",
            USER = "mtgsaberdwt2";
    public static final char[] PSWD = {
            0x50,0x8,0x5a,0x19,0x70,0x1,0x16,0x70,0x1e,0x57,0x31,0x14
    };

}
