package de.lego.gottfried.decdat.ou;

import java.util.HashMap;
import java.util.Map;

import de.lego.gottfried.decdat.MainForm;
import de.lego.gottfried.decdat.parser.Ou;
import de.lego.gottfried.decdat.parser.Ou.TypeOuString;

public class zCCSLib {
    public Map<String, zCCSBlock> blocks = new HashMap<String, zCCSBlock>();

    public zCCSLib(Ou ou_file) {
        //Added just to know if it was actually loaded
        MainForm.Log("load new ou.bin");
        MainForm.Indent(2);
        MainForm.Log("read " + ou_file.zClib().numOfItems() + " elements");
        for (int i = 0; i < ou_file.zClib().numOfItems(); i++)
        {
            String id    = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(2 ).propBody()).objectId();
            String text  = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(12).propBody()).objectId();
            //String sound = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(14).propBody()).objectId();
            blocks.put(id.toUpperCase(), new zCCSBlock(id, text/* , sound*/));
        }
        MainForm.Log("valid ou.bin loaded");
        MainForm.Indent(-2);
    }

    public String getText (String id)
    {
        if (id.length() > 0 && id.charAt(0) == '\"')
        {
            id = id.substring(1, id.length() - 1);
        }
        zCCSBlock block = blocks.get(id.toUpperCase());
        if (block == null)
            return "";
        else
            return block.text;
    }
}
