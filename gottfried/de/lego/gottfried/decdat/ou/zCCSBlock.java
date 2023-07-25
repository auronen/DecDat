package de.lego.gottfried.decdat.ou;


public class zCCSBlock {
    // dialogue ID - "B_Gravo_HelpAttitude_ANGRY_04_00"
    public String   id;
    // dialogue text - "Stimmt, er ist nicht besonders gut auf dich zu sprechen."
    public String   text;
    // sound file - "B_Gravo_HelpAttitude_ANGRY_04_00.WAV"
    public String   wavFile;

    // Create new zCCSBlock
    public zCCSBlock(String id, String text/*, String wavFile*/) {
        this.id = id;
        this.text = text;
        //this.wavFile = wavFile;
    }
    
}
