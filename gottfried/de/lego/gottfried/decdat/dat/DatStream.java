package de.lego.gottfried.decdat.dat;

public class DatStream {
	private byte	stream[];
	private int		offset;

	public DatStream(byte Stream[]) {
		stream = Stream;
		offset = 0;
	}

	public int ReadByte() {
		int b = stream[offset++];
		return (b < 0) ? (b + 256) : b;
	}

	public byte[] ReadBytes(int len) {
		byte ret[] = new byte[len];
		System.arraycopy(stream, offset, ret, 0, len);
		return ret;
	}

	public int ReadInt() {
		return (ReadByte()) + (ReadByte() << 8) + (ReadByte() << 16) + (ReadByte() << 24);
	}

	public float ReadFloat() {
		return Float.intBitsToFloat(ReadInt());
	}

	public char ReadChar() {
		return (char)ReadByte();
	}

	public String ReadString() {
		int s = offset;
		while((char)stream[offset++] != '\n')
			;
		return new String(stream, s, offset - s - 1);
	}
}
