package io.chunkworld.api.core.utils;


import com.badlogic.gdx.utils.BufferUtils;
import lombok.SneakyThrows;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class has various utilities
 * to manage an
 */
public class CommonUtils {
    /**
     * Gets the extension of a file
     *
     * @param file the file to grab the extension for
     * @return the extension of the file
     * @throws IOException throws IoException if the file is a directory
     */
    public static String getExtension(File file) throws IOException {
        String fileName = file.getName();
        return Optional.of(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1)).orElseThrow(IOException::new);
    }

    /**
     * Gets the name of a file minus the extension
     *
     * @param file the file to grab the extension for
     * @return the extension of the file
     * @throws IOException throws IoException if the file is a directory
     */
    public static String removeExtension(File file) throws IOException {
        String fileName = file.getName();
        return Optional.of(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(0, fileName.lastIndexOf("."))).orElseThrow(IOException::new);
    }

    /**
     * Simply checks the path to make sure it's not the "info" directory
     *
     * @param p the path to check
     * @return checks to see if a path is valid or not
     */
    public static boolean isValidDirectory(Path p) {
        return Files.isDirectory(p) && Objects.requireNonNull(p.toFile().list()).length > 0;
    }

    /**
     * This method will go find the domain name
     *
     * @param file the file to search for
     * @return the domain of the file
     */
    public static String resolveDomain(File file) throws IOException {
        File parentFile = file.getParentFile();
        List<String> parts = new ArrayList<>();
        while (parentFile != null) {
            if (parentFile.getName().equalsIgnoreCase("domains"))
                break;
            parts.add(parentFile.getName());
            parentFile = parentFile.getParentFile();
        }
        Collections.reverse(parts);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parts.size() - 1; i++)
            stringBuilder.append(parts.get(i)).append(":");
        stringBuilder.append(parts.get(parts.size() - 1));
        return stringBuilder.toString();
    }


    /**
     * Gets all of the text after the search char in the input text
     *
     * @param input  the input string
     * @param search the search string
     * @param index  the index to grab
     * @return the formatted text
     */
    public static String getTextAfter(String input, char search, int index) {
        String segments[] = input.split(String.valueOf(search));
        if (segments.length == 1)
            return input;
        if (index > segments.length - 1)
            return input;
        return segments[index];
    }

    /**
     * Converts an intbuffer to an int array
     *
     * @return the converted int array
     */
    public static int[] reverse(int[] arr) {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < arr.length; i++)
            ints.add(arr[i]);
        Collections.reverse(ints);
        int[] newArray = new int[ints.size()];
        for (int i = 0; i < newArray.length; i++)
            newArray[i] = ints.get(i);
        return newArray;
    }


    /**
     * Converts an int list to int array
     *
     * @param ints input ints
     * @return int array
     */
    public static int[] toIntArray(List<Integer> ints) {
        int[] intArray = new int[ints.size()];
        for (int i = 0; i < ints.size(); i++)
            intArray[i] = ints.get(i);
        return intArray;
    }

    /**
     * Converts an int list to float array
     *
     * @param floats input floats
     * @return int array
     */
    public static float[] toFloatArray(List<Float> floats) {
        float[] intArray = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++)
            intArray[i] = floats.get(i);
        return intArray;
    }

    public static Matrix4f toRotationMatrix(Quaternionf quaternionf) {
        Matrix4f matrix = new Matrix4f();
        final float xy = quaternionf.x * quaternionf.y;
        final float xz = quaternionf.x * quaternionf.z;
        final float xw = quaternionf.x * quaternionf.w;
        final float yz = quaternionf.y * quaternionf.z;
        final float yw = quaternionf.y * quaternionf.w;
        final float zw = quaternionf.z * quaternionf.w;
        final float xSquared = quaternionf.x * quaternionf.x;
        final float ySquared = quaternionf.y * quaternionf.y;
        final float zSquared = quaternionf.z * quaternionf.z;
        matrix.m00(1 - 2 * (ySquared + zSquared));
        matrix.m01(2 * (xy - zw));
        matrix.m02(2 * (xz + yw));
        matrix.m03(0);
        matrix.m10(2 * (xy + zw));
        matrix.m11(1 - 2 * (xSquared + zSquared));
        matrix.m12(2 * (yz - xw));
        matrix.m13(0);
        matrix.m20(2 * (xz - yw));
        matrix.m21(2 * (yz + xw));
        matrix.m22(1 - 2 * (xSquared + ySquared));
        matrix.m23(0);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        matrix.m33(1);
        return matrix;
    }

    /**
     * Extracts the rotation part of a transformation matrix and converts it to
     * a quaternion using the magic of maths.
     * <p>
     * More detailed explanation here:
     * http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
     *
     * @param matrix - the transformation matrix containing the rotation which this
     *               quaternion shall represent.
     */
    public static Quaternionf fromMatrix(Matrix4f matrix) {
        float w, x, y, z;
        float diagonal = matrix.m00() + matrix.m11() + matrix.m22();
        if (diagonal > 0) {
            float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
            w = w4 / 4f;
            x = (matrix.m21() - matrix.m12()) / w4;
            y = (matrix.m02() - matrix.m20()) / w4;
            z = (matrix.m10() - matrix.m01()) / w4;
        } else if ((matrix.m00() > matrix.m11()) && (matrix.m00() > matrix.m22())) {
            float x4 = (float) (Math.sqrt(1f + matrix.m00() - matrix.m11() - matrix.m22()) * 2f);
            w = (matrix.m21() - matrix.m12()) / x4;
            x = x4 / 4f;
            y = (matrix.m01() + matrix.m10()) / x4;
            z = (matrix.m02() + matrix.m20()) / x4;
        } else if (matrix.m11() > matrix.m22()) {
            float y4 = (float) (Math.sqrt(1f + matrix.m11() - matrix.m00() - matrix.m22()) * 2f);
            w = (matrix.m02() - matrix.m20()) / y4;
            x = (matrix.m01() + matrix.m10()) / y4;
            y = y4 / 4f;
            z = (matrix.m12() + matrix.m21()) / y4;
        } else {
            float z4 = (float) (Math.sqrt(1f + matrix.m22() - matrix.m00() - matrix.m11()) * 2f);
            w = (matrix.m10() - matrix.m01()) / z4;
            x = (matrix.m02() + matrix.m20()) / z4;
            y = (matrix.m12() + matrix.m21()) / z4;
            z = z4 / 4f;
        }
        return new Quaternionf(x, y, z, w);
    }

    /**
     * Normalizes the quaternion.
     */
    public static void normalize(Quaternionf quat) {
        float mag = (float) Math.sqrt(quat.w * quat.w + quat.x * quat.x + quat.y * quat.y + quat.z * quat.z);
        quat.w /= mag;
        quat.x /= mag;
        quat.y /= mag;
        quat.z /= mag;
    }


    /**
     * Interpolates between two quaternion rotations and returns the resulting
     * quaternion rotation. The interpolation method here is "nlerp", or
     * "normalized-lerp". Another mnethod that could be used is "slerp", and you
     * can see a comparison of the methods here:
     * https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
     * <p>
     * and here:
     * http://number-none.com/product/Understanding%20Slerp,%20Then%20Not%20Using%20It/
     *
     * @param a
     * @param b
     * @param blend - a value between 0 and 1 indicating how far to interpolate
     *              between the two quaternions.
     * @return The resulting interpolated rotation in quaternion format.
     */
    public static Quaternionf interpolate(Quaternionf a, Quaternionf b, float blend) {
        Quaternionf result = new Quaternionf(0, 0, 0, 1);
        float dot = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
        float blendI = 1f - blend;
        if (dot < 0) {
            result.w = blendI * a.w + blend * -b.w;
            result.x = blendI * a.x + blend * -b.x;
            result.y = blendI * a.y + blend * -b.y;
            result.z = blendI * a.z + blend * -b.z;
        } else {
            result.w = blendI * a.w + blend * b.w;
            result.x = blendI * a.x + blend * b.x;
            result.y = blendI * a.y + blend * b.y;
            result.z = blendI * a.z + blend * b.z;
        }
        result.normalize();
        return result;
    }

    /**
     * Linearly interpolates between two translations based on a "progression"
     * value.
     *
     * @param start       - the start translation.
     * @param end         - the end translation.
     * @param progression - a value between 0 and 1 indicating how far to interpolate
     *                    between the two translations.
     * @return
     */
    public static Vector3f interpolate(Vector3f start, Vector3f end, float progression) {
        float x = start.x + (end.x - start.x) * progression;
        float y = start.y + (end.y - start.y) * progression;
        float z = start.z + (end.z - start.z) * progression;
        return new Vector3f(x, y, z);
    }

    /**
     * Converts a byte array to a float array
     *
     * @param byteBuffer the byte buffer to convert
     * @return
     */
    public static float[] toFloatArray(ByteBuffer byteBuffer) {
        FloatBuffer fb = byteBuffer.asFloatBuffer();
        float[] floatArray = new float[fb.limit()];
        fb.get(floatArray);
        return floatArray;
    }

    /**
     * Converts a byte array to a float array
     *
     * @param byteBuffer the byte buffer to convert
     * @return
     */
    public static int[] toIntArray(ByteBuffer byteBuffer) {
        IntBuffer fb = byteBuffer.asIntBuffer();
        int[] intArray = new int[fb.limit()];
        fb.get(intArray);
        return intArray;
    }

    /**
     * Converts a byte array to a float array
     *
     * @param fb the byte buffer to convert
     * @return
     */
    public static int[] toIntArray(IntBuffer fb) {
        int[] intArray = new int[fb.limit()];
        fb.get(intArray);
        return intArray;
    }

    /**
     * Simply prints some byte array to console
     *
     * @param data data to print
     */
    public static void printBytes(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (i % 4 == 0) System.out.print("\n");
            System.out.printf("0x%x ", data[i]);
        }
    }

    /**
     * Gets a byte buffer from a decoded file
     *
     * @param file the file to decode
     * @return the bytebuffer from the file
     */
    @SneakyThrows
    public static ByteBuffer decodeFile(File file) {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] data = new byte[(int) f.length()];
        f.readFully(data);
        f.close();
        return ByteBuffer.wrap(data);
    }


    /**
     * Convert a list of matricies to an array of matricies
     *
     * @param mats matrix list to convert
     * @return array of matricies
     */
    public static Matrix4f[] matrixToArray(List<Matrix4f> mats) {
        Matrix4f[] matrices = new Matrix4f[mats.size()];
        for (int i = 0; i < matrices.length; i++)
            matrices[i] = mats.get(i);
        return matrices;
    }

    /**
     * A supplieer helper method
     */
    public static <T> Supplier<T> getter(Object obj, Class<T> fieldClass, Field f) {

        if (!fieldClass.isAssignableFrom(f.getType()))
            throw new RuntimeException("Field is not of expected type");

        return () -> {
            try {
                return (T) f.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <O, F> Consumer<F> setter(O obj, BiConsumer<O, F> modifier) {
        return field -> modifier.accept(obj, field);
    }
}