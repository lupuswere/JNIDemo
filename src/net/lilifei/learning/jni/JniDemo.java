package net.lilifei.learning.jni;

public class JniDemo {

    static {
        System.loadLibrary("jniwrapper");
    }

    public static void main(final String args[]) {
        final int a = 1, b = 2;
        final JniWrapper jniWrapper = new JniWrapper();
        System.out.println(jniWrapper.add(a, b));
    }
}
