# DecDat

Originally built by Gottfried: [original release post](https://forum.worldofplayers.de/forum/threads/1151032-Tool-DecDat)

While working on [Phoenix](https://phoenixthegame.com/) I had to decompile compiled scripts from an older demo so it could be passed on to people doing alpha version research. To do this, I changed the source code of DecDat to work on the old `.DAT` file format.

When I was at it I fixed the string substitution feature and int to instance substitution.

## String substitution

The decompiled code now shows the string literals instead of the const string of the string literal.

Before
```c++
description = ˙10722;
```
Now
```c++
description = "ENDE";
```

## Instance substitution

Due to the way how Daedalus treats instances (they are basically symbol table indicies) the integers get decompiled back and the instance information is lost.

As a quick fix I introduced instance substitution, every integer higher than 60 is checked whether it is also an instance and then the substitution occurs. This is not a bullet proof solution, so the original integer is placed as a comment right after the substituted instance identifier.

Before
```c++
if(npc_knowsinfo(hero, 12408)) {
    return true;
};
```

After
```c++
if(npc_knowsinfo(hero, info_xardasdemon_intro /*12408*/)) {
    return true;
};
```
