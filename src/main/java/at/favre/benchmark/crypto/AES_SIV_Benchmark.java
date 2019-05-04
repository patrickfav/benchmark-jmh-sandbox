package at.favre.benchmark.crypto;

import at.favre.lib.bytes.Bytes;
import org.cryptomator.siv.SivMode;
import org.cryptomator.siv.UnauthenticCiphertextException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 4)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AES_SIV_Benchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        private SivMode cipher;
        private byte[] random16;
        private SecretKey key;
        private SecretKey macKey;
        private byte[] enc;
        private byte[] iv;

        @Setup
        public void setup() {
            try {
                cipher = new SivMode();
                key = new SecretKeySpec(Bytes.random(16).array(), "AES");
                macKey = new SecretKeySpec(Bytes.random(16).array(), "AES");
                random16 = Bytes.random(16).array();
                enc = cipher.encrypt(key, macKey, random16);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Benchmark
    public void encrypt16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.cipher.encrypt(state.key, state.macKey, state.random16));
    }

    @Benchmark
    public void decrypt16Byte(BenchmarkState state, Blackhole blackhole) throws UnauthenticCiphertextException, IllegalBlockSizeException {
        blackhole.consume(state.cipher.decrypt(state.key, state.macKey, state.enc));
    }
}
