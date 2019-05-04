package at.favre.benchmark.crypto;

import at.favre.lib.bytes.Bytes;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 4)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AES_GCM_Benchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        private Cipher cipher;
        private byte[] random16;
        private SecretKey key;
        private byte[] enc;
        private byte[] iv;

        @Setup
        public void setup() {
            try {
                cipher = Cipher.getInstance("AES/GCM/NoPadding");
                key = new SecretKeySpec(Bytes.random(16).array(), "AES");
                iv = Bytes.random(12).array();
                cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
                random16 = Bytes.random(16).array();
                enc = cipher.doFinal(random16);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Benchmark
    public void encrypt16Byte(BenchmarkState state, Blackhole blackhole) {
        try {
            state.cipher.init(Cipher.ENCRYPT_MODE, state.key);
            blackhole.consume(state.cipher.doFinal(state.random16));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Benchmark
    public void decrypt16Byte(BenchmarkState state, Blackhole blackhole) {
        try {
            state.cipher.init(Cipher.DECRYPT_MODE, state.key, new GCMParameterSpec(128, state.iv));
            blackhole.consume(state.cipher.doFinal(state.enc));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
