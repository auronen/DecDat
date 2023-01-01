package de.lego.gottfried.decdat.ou;

import java.util.HashMap;
import java.util.Map;

import de.lego.gottfried.decdat.parser.Ou;
import de.lego.gottfried.decdat.parser.Ou.TypeOuString;

public class zCCSLib {
    public Map<String, zCCSBlock> blocks = new HashMap<String, zCCSBlock>();

    public zCCSLib(Ou ou_file) {
        for (int i = 0; i < ou_file.zClib().numOfItems(); i++)
        {
            String id    = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(2 ).propBody()).objectId();
            String text  = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(12).propBody()).objectId();
            String sound = ((TypeOuString)ou_file.zClib().properties().get(i).properties().get(14).propBody()).objectId();
            blocks.put(id.toUpperCase(), new zCCSBlock(id, text, sound));
        }
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
