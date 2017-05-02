package etc.a0la0.glcamera.util;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {

//    public static Buffer getBuffer(double[] array) {
//        ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length); // each float takes 4 bytes
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (double d : array)
//            bb.putFloat((float) d);
//        bb.rewind();
//
//        return bb;
//    }
//
//    public static Buffer getBuffer(short[] array) {
//        ByteBuffer bb = ByteBuffer.allocateDirect(2 * array.length); // each short takes 2 bytes
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (short s : array)
//            bb.putShort(s);
//        bb.rewind();
//        return bb;
//    }
//
//    public static Buffer getBuffer(float[] array) {
//        ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length); // each float takes 4 bytes
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (float d : array)
//            bb.putFloat(d);
//        bb.rewind();
//        return bb;
//    }

    public static FloatBuffer getFloatBuffer(float[] floatArray) {
        FloatBuffer vertexBuffer;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(floatArray.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(floatArray);
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    public static ShortBuffer getShortBuffer(short[] shortArray) {
        ShortBuffer shortBuffer;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(shortArray.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(shortArray);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static ByteBuffer getByteBuffer(byte[] byteArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(byteArray.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(byteArray);
        byteBuffer.position(0);
        return byteBuffer;
    }

}
