package org.vybe.wasm;

import com.dylibso.chicory.runtime.ConstantEvaluators;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.PassiveDataSegment;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class ContiguousByteArrayMemory implements Memory {
    private static final VarHandle INT_HANDLE =
        MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LONG_HANDLE =
        MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle SHORT_HANDLE =
        MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle FLOAT_HANDLE =
        MethodHandles.byteArrayViewVarHandle(float[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle DOUBLE_HANDLE =
        MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.LITTLE_ENDIAN);

    private final MemoryLimits limits;
    private byte[] data;
    private int nPages;
    private DataSegment[] dataSegments;

    public ContiguousByteArrayMemory(MemoryLimits limits) {
        this.limits = limits;
        this.nPages = limits.initialPages();
        this.data = new byte[Memory.bytes(this.nPages)];
    }

    public byte[] data() {
        return data;
    }

    @Override
    public int pages() {
        return nPages;
    }

    @Override
    public int grow(int size) {
        if (size < 0) {
            return -1;
        }
        int prevPages = nPages;
        int newPages = prevPages + size;
        if (newPages > maximumPages()) {
            return -1;
        }
        if (size == 0) {
            return prevPages;
        }
        data = Arrays.copyOf(data, Memory.bytes(newPages));
        nPages = newPages;
        return prevPages;
    }

    @Override
    public int initialPages() {
        return limits.initialPages();
    }

    @Override
    public int maximumPages() {
        return Math.min(limits.maximumPages(), Memory.RUNTIME_MAX_PAGES);
    }

    @Override
    public boolean shared() {
        return limits.shared();
    }

    @Override
    public Object lock(int address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int waitOn(int address, int expected, long timeoutMs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int waitOn(int address, long expected, long timeoutMs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int notify(int address, int count) {
        return 0;
    }

    @Override
    public void initialize(Instance instance, DataSegment[] dataSegments) {
        initialize(instance, dataSegments, 0);
    }

    @Override
    public void initialize(Instance instance, DataSegment[] dataSegments, int memoryIndex) {
        this.dataSegments = dataSegments;
        if (dataSegments == null) {
            return;
        }

        for (DataSegment dataSegment : dataSegments) {
            if (dataSegment instanceof ActiveDataSegment active) {
                if (active.index() != memoryIndex) {
                    continue;
                }
                byte[] segmentData = active.data();
                int offset = (int) ConstantEvaluators.computeConstantValue(
                    instance, active.offsetInstructions())[0];
                write(offset, segmentData, 0, segmentData.length);
            } else if (!(dataSegment instanceof PassiveDataSegment)) {
                throw new ChicoryException("Unsupported data segment: " + dataSegment);
            }
        }
    }

    @Override
    public void initPassiveSegment(int segmentId, int dest, int offset, int size) {
        write(dest, dataSegments[segmentId].data(), offset, size);
    }

    @Override
    public void write(int address, byte[] bytes, int offset, int size) {
        System.arraycopy(bytes, offset, data, address, size);
    }

    @Override
    public byte read(int address) {
        return data[address];
    }

    @Override
    public byte[] readBytes(int address, int size) {
        return Arrays.copyOfRange(data, address, address + size);
    }

    @Override
    public void writeI32(int address, int value) {
        INT_HANDLE.set(data, address, value);
    }

    @Override
    public int readInt(int address) {
        return (int) INT_HANDLE.get(data, address);
    }

    @Override
    public void writeLong(int address, long value) {
        LONG_HANDLE.set(data, address, value);
    }

    @Override
    public long readLong(int address) {
        return (long) LONG_HANDLE.get(data, address);
    }

    @Override
    public void writeShort(int address, short value) {
        SHORT_HANDLE.set(data, address, value);
    }

    @Override
    public short readShort(int address) {
        return (short) SHORT_HANDLE.get(data, address);
    }

    @Override
    public long readU16(int address) {
        return Short.toUnsignedLong(readShort(address));
    }

    @Override
    public void writeByte(int address, byte value) {
        data[address] = value;
    }

    @Override
    public void writeF32(int address, float value) {
        FLOAT_HANDLE.set(data, address, value);
    }

    @Override
    public long readF32(int address) {
        return Float.floatToRawIntBits((float) FLOAT_HANDLE.get(data, address));
    }

    @Override
    public float readFloat(int address) {
        return (float) FLOAT_HANDLE.get(data, address);
    }

    @Override
    public void writeF64(int address, double value) {
        DOUBLE_HANDLE.set(data, address, value);
    }

    @Override
    public double readDouble(int address) {
        return (double) DOUBLE_HANDLE.get(data, address);
    }

    @Override
    public long readF64(int address) {
        return Double.doubleToRawLongBits((double) DOUBLE_HANDLE.get(data, address));
    }

    @Override
    public void zero() {
        Arrays.fill(data, (byte) 0);
    }

    @Override
    public void fill(byte value, int fromIndex, int toIndex) {
        Arrays.fill(data, fromIndex, toIndex, value);
    }

    @Override
    public void copy(int dest, int src, int size) {
        System.arraycopy(data, src, data, dest, size);
    }

    @Override
    public void drop(int segment) {
        if (dataSegments != null) {
            dataSegments[segment] = PassiveDataSegment.EMPTY;
        }
    }
}
