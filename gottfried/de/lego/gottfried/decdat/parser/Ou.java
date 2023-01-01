package de.lego.gottfried.decdat.parser;

// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild
// Auronen: but I changed it, to read strings based on selected encoding

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;
import java.io.IOException;
import java.util.ArrayList;

import de.lego.gottfried.decdat.MainForm;

import java.nio.charset.Charset;


/**
 * OU class represents the whole zCCSLib serialized in OU.bin files
 */
public class Ou extends KaitaiStruct {
    public static Ou fromFile(String fileName) throws IOException {
        return new Ou(new ByteBufferKaitaiStream(fileName));
    }

    public Ou(KaitaiStream _io) {
        this(_io, null, null);
    }

    public Ou(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public Ou(KaitaiStream _io, KaitaiStruct _parent, Ou _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
        this.header = new Header(this._io, this, _root);
        this.archiveHeader = new ArchiveHeader(this._io, this, _root);
        this.zClib = new ZClib(this._io, this, _root);
    }
    public static class TypeFloat extends KaitaiStruct {
        public static TypeFloat fromFile(String fileName) throws IOException {
            return new TypeFloat(new ByteBufferKaitaiStream(fileName));
        }

        public TypeFloat(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TypeFloat(KaitaiStream _io, Ou.Property _parent) {
            this(_io, _parent, null);
        }

        public TypeFloat(KaitaiStream _io, Ou.Property _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = this._io.readF4le();
        }
        private float value;
        private Ou _root;
        private Ou.Property _parent;
        public float value() { return value; }
        public Ou _root() { return _root; }
        public Ou.Property _parent() { return _parent; }
    }
    public static class ZClib extends KaitaiStruct {
        public static ZClib fromFile(String fileName) throws IOException {
            return new ZClib(new ByteBufferKaitaiStream(fileName));
        }

        public ZClib(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ZClib(KaitaiStream _io, Ou _parent) {
            this(_io, _parent, null);
        }

        public ZClib(KaitaiStream _io, Ou _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.specifier = new Property(this._io, this, _root);
            this.type1 = this._io.readU1();
            this.hash1 = this._io.readU4le();
            this.type2 = this._io.readU1();
            this.numOfItems = this._io.readU4le();
            this.properties = new ArrayList<Entry>();
            for (int i = 0; i < numOfItems(); i++) {
                this.properties.add(new Entry(this._io, this, _root));
            }
        }
        private Property specifier;
        private int type1;
        private long hash1;
        private int type2;
        private long numOfItems;
        private ArrayList<Entry> properties;
        private Ou _root;
        private Ou _parent;
        public Property specifier() { return specifier; }
        public int type1() { return type1; }
        public long hash1() { return hash1; }
        public int type2() { return type2; }
        public long numOfItems() { return numOfItems; }
        public ArrayList<Entry> properties() { return properties; }
        public Ou _root() { return _root; }
        public Ou _parent() { return _parent; }
    }
    public static class HashTableChunk extends KaitaiStruct {
        public static HashTableChunk fromFile(String fileName) throws IOException {
            return new HashTableChunk(new ByteBufferKaitaiStream(fileName));
        }

        public HashTableChunk(KaitaiStream _io) {
            this(_io, null, null);
        }

        public HashTableChunk(KaitaiStream _io, Ou.HashTableHeader _parent) {
            this(_io, _parent, null);
        }

        public HashTableChunk(KaitaiStream _io, Ou.HashTableHeader _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.nameLength = this._io.readU2le();
            this.linearValue = this._io.readU2le();
            this.hashValue = this._io.readU4le();
            this.str = new String(this._io.readBytes(nameLength()), Charset.forName(MainForm.encoding));
        }
        private int nameLength;
        private int linearValue;
        private long hashValue;
        private String str;
        private Ou _root;
        private Ou.HashTableHeader _parent;
        public int nameLength() { return nameLength; }
        public int linearValue() { return linearValue; }
        public long hashValue() { return hashValue; }
        public String str() { return str; }
        public Ou _root() { return _root; }
        public Ou.HashTableHeader _parent() { return _parent; }
    }
    public static class Entry extends KaitaiStruct {
        public static Entry fromFile(String fileName) throws IOException {
            return new Entry(new ByteBufferKaitaiStream(fileName));
        }

        public Entry(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Entry(KaitaiStream _io, Ou.ZClib _parent) {
            this(_io, _parent, null);
        }

        public Entry(KaitaiStream _io, Ou.ZClib _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.properties = new ArrayList<Property>();
            for (int i = 0; i < 18; i++) {
                this.properties.add(new Property(this._io, this, _root));
            }
        }
        private ArrayList<Property> properties;
        private Ou _root;
        private Ou.ZClib _parent;
        public ArrayList<Property> properties() { return properties; }
        public Ou _root() { return _root; }
        public Ou.ZClib _parent() { return _parent; }
    }
    public static class TypeEnum extends KaitaiStruct {
        public static TypeEnum fromFile(String fileName) throws IOException {
            return new TypeEnum(new ByteBufferKaitaiStream(fileName));
        }

        public TypeEnum(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TypeEnum(KaitaiStream _io, Ou.Property _parent) {
            this(_io, _parent, null);
        }

        public TypeEnum(KaitaiStream _io, Ou.Property _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = this._io.readU4le();
        }
        private long value;
        private Ou _root;
        private Ou.Property _parent;
        public long value() { return value; }
        public Ou _root() { return _root; }
        public Ou.Property _parent() { return _parent; }
    }
    public static class ArchiveHeader extends KaitaiStruct {
        public static ArchiveHeader fromFile(String fileName) throws IOException {
            return new ArchiveHeader(new ByteBufferKaitaiStream(fileName));
        }

        public ArchiveHeader(KaitaiStream _io) {
            this(_io, null, null);
        }

        public ArchiveHeader(KaitaiStream _io, Ou _parent) {
            this(_io, _parent, null);
        }

        public ArchiveHeader(KaitaiStream _io, Ou _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.bsVersion = this._io.readU4le();
            this.objectCount = this._io.readU4le();
            this.hashTableOffset = this._io.readU4le();
        }
        private HashTableHeader ht;
        public HashTableHeader ht() {
            if (this.ht != null)
                return this.ht;
            long _pos = this._io.pos();
            this._io.seek(hashTableOffset());
            this.ht = new HashTableHeader(this._io, this, _root);
            this._io.seek(_pos);
            return this.ht;
        }
        private long bsVersion;
        private long objectCount;
        private long hashTableOffset;
        private Ou _root;
        private Ou _parent;
        public long bsVersion() { return bsVersion; }
        public long objectCount() { return objectCount; }
        public long hashTableOffset() { return hashTableOffset; }
        public Ou _root() { return _root; }
        public Ou _parent() { return _parent; }
    }
    public static class TypeInteger extends KaitaiStruct {
        public static TypeInteger fromFile(String fileName) throws IOException {
            return new TypeInteger(new ByteBufferKaitaiStream(fileName));
        }

        public TypeInteger(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TypeInteger(KaitaiStream _io, Ou.Property _parent) {
            this(_io, _parent, null);
        }

        public TypeInteger(KaitaiStream _io, Ou.Property _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = this._io.readU4le();
        }
        private long value;
        private Ou _root;
        private Ou.Property _parent;
        public long value() { return value; }
        public Ou _root() { return _root; }
        public Ou.Property _parent() { return _parent; }
    }
    public static class TypeOuString extends KaitaiStruct {
        public static TypeOuString fromFile(String fileName) throws IOException {
            return new TypeOuString(new ByteBufferKaitaiStream(fileName));
        }

        public TypeOuString(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TypeOuString(KaitaiStream _io, Ou.Property _parent) {
            this(_io, _parent, null);
        }

        public TypeOuString(KaitaiStream _io, Ou.Property _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.size = this._io.readU2le();
            this.objectId = new String(this._io.readBytes(size()), Charset.forName(MainForm.encoding));
        }
        private int size;
        private String objectId;
        private Ou _root;
        private Ou.Property _parent;
        public int size() { return size; }
        public String objectId() { return objectId; }
        public Ou _root() { return _root; }
        public Ou.Property _parent() { return _parent; }
    }
    public static class Header extends KaitaiStruct {
        public static Header fromFile(String fileName) throws IOException {
            return new Header(new ByteBufferKaitaiStream(fileName));
        }

        public Header(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Header(KaitaiStream _io, Ou _parent) {
            this(_io, _parent, null);
        }

        public Header(KaitaiStream _io, Ou _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.magic = new OuString(this._io, this, _root);
            this.version = new OuString(this._io, this, _root);
            this.archiverClass = new OuString(this._io, this, _root);
            this.archiverType = new OuString(this._io, this, _root);
            this.saveGame = new OuString(this._io, this, _root);
            this.date = new OuString(this._io, this, _root);
            this.user = new OuString(this._io, this, _root);
            this.end = new OuString(this._io, this, _root);
        }
        private OuString magic;
        private OuString version;
        private OuString archiverClass;
        private OuString archiverType;
        private OuString saveGame;
        private OuString date;
        private OuString user;
        private OuString end;
        private Ou _root;
        private Ou _parent;
        public OuString magic() { return magic; }
        public OuString version() { return version; }
        public OuString archiverClass() { return archiverClass; }
        public OuString archiverType() { return archiverType; }
        public OuString saveGame() { return saveGame; }
        public OuString date() { return date; }
        public OuString user() { return user; }
        public OuString end() { return end; }
        public Ou _root() { return _root; }
        public Ou _parent() { return _parent; }
    }
    public static class Property extends KaitaiStruct {
        public static Property fromFile(String fileName) throws IOException {
            return new Property(new ByteBufferKaitaiStream(fileName));
        }

        public Property(KaitaiStream _io) {
            this(_io, null, null);
        }

        public Property(KaitaiStream _io, KaitaiStruct _parent) {
            this(_io, _parent, null);
        }

        public Property(KaitaiStream _io, KaitaiStruct _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            switch (type()) {
            case 17: {
                this.propBody = new TypeEnum(this._io, this, _root);
                break;
            }
            case 1: {
                this.propBody = new TypeOuString(this._io, this, _root);
                break;
            }
            case 3: {
                this.propBody = new TypeFloat(this._io, this, _root);
                break;
            }
            case 18: {
                this.propBody = new TypeHash(this._io, this, _root);
                break;
            }
            case 2: {
                this.propBody = new TypeInteger(this._io, this, _root);
                break;
            }
            }
        }
        private int type;
        private KaitaiStruct propBody;
        private Ou _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public KaitaiStruct propBody() { return propBody; }
        public Ou _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class TypeHash extends KaitaiStruct {
        public static TypeHash fromFile(String fileName) throws IOException {
            return new TypeHash(new ByteBufferKaitaiStream(fileName));
        }

        public TypeHash(KaitaiStream _io) {
            this(_io, null, null);
        }

        public TypeHash(KaitaiStream _io, Ou.Property _parent) {
            this(_io, _parent, null);
        }

        public TypeHash(KaitaiStream _io, Ou.Property _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = this._io.readU4le();
        }
        private long value;
        private Ou _root;
        private Ou.Property _parent;
        public long value() { return value; }
        public Ou _root() { return _root; }
        public Ou.Property _parent() { return _parent; }
    }
    public static class HashTableHeader extends KaitaiStruct {
        public static HashTableHeader fromFile(String fileName) throws IOException {
            return new HashTableHeader(new ByteBufferKaitaiStream(fileName));
        }

        public HashTableHeader(KaitaiStream _io) {
            this(_io, null, null);
        }

        public HashTableHeader(KaitaiStream _io, Ou.ArchiveHeader _parent) {
            this(_io, _parent, null);
        }

        public HashTableHeader(KaitaiStream _io, Ou.ArchiveHeader _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.chunkCount = this._io.readU4le();
            this.chunks = new ArrayList<HashTableChunk>();
            for (int i = 0; i < chunkCount(); i++) {
                this.chunks.add(new HashTableChunk(this._io, this, _root));
            }
        }
        private long chunkCount;
        private ArrayList<HashTableChunk> chunks;
        private Ou _root;
        private Ou.ArchiveHeader _parent;
        public long chunkCount() { return chunkCount; }
        public ArrayList<HashTableChunk> chunks() { return chunks; }
        public Ou _root() { return _root; }
        public Ou.ArchiveHeader _parent() { return _parent; }
    }
    public static class OuString extends KaitaiStruct {
        public static OuString fromFile(String fileName) throws IOException {
            return new OuString(new ByteBufferKaitaiStream(fileName));
        }

        public OuString(KaitaiStream _io) {
            this(_io, null, null);
        }

        public OuString(KaitaiStream _io, Ou.Header _parent) {
            this(_io, _parent, null);
        }

        public OuString(KaitaiStream _io, Ou.Header _parent, Ou _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.str = new String(this._io.readBytesTerm((byte) 10, false, true, true), Charset.forName(MainForm.encoding));
        }
        private String str;
        private Ou _root;
        private Ou.Header _parent;
        public String str() { return str; }
        public Ou _root() { return _root; }
        public Ou.Header _parent() { return _parent; }
    }
    private Header header;
    private ArchiveHeader archiveHeader;
    private ZClib zClib;
    private Ou _root;
    private KaitaiStruct _parent;
    public Header header() { return header; }
    public ArchiveHeader archiveHeader() { return archiveHeader; }
    public ZClib zClib() { return zClib; }
    public Ou _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
