package at.favre.benchmark.crypto;

import at.favre.lib.bytes.Bytes;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 4)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AES_ECB_Benchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        private Cipher cipher;
        private byte[] random16;
        private byte[] random128;
        private SecretKey key;
        private byte[] enc;
        private byte[] enc2;

        @Setup
        public void setup() {
            try {
                cipher = Cipher.getInstance("AES/ECB/NoPadding");
                key = new SecretKeySpec(Bytes.random(16).array(), "AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                random16 = Bytes.random(16).array();
                random128 = Bytes.random(128).array();
                enc = cipher.doFinal(random16);
                enc2 = cipher.doFinal(random128);
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
            state.cipher.init(Cipher.DECRYPT_MODE, state.key);
            blackhole.consume(state.cipher.doFinal(state.enc));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Benchmark
    public void encrypt128Byte(BenchmarkState state, Blackhole blackhole) {
        try {
            state.cipher.init(Cipher.ENCRYPT_MODE, state.key);
            blackhole.consume(state.cipher.doFinal(state.random128));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Benchmark
    public void decrypt128Byte(BenchmarkState state, Blackhole blackhole) {
        try {
            state.cipher.init(Cipher.DECRYPT_MODE, state.key);
            blackhole.consume(state.cipher.doFinal(state.enc2));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
