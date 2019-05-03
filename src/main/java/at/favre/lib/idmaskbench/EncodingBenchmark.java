package at.favre.lib.idmaskbench;

import at.favre.lib.bytes.Bytes;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 3, time = 8)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EncodingBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        private Bytes bytes8Byte;
        private Bytes bytes16Byte;
        private Bytes bytes1024Byte;

        private String hexEncoded8Byte;
        private String hexEncoded16Byte;
        private String hexEncoded1024Byte;

        private String base32Encoded8Byte;
        private String base32Encoded16Byte;
        private String base32Encoded1024Byte;

        private String base64Encoded8Byte;
        private String base64Encoded16Byte;
        private String base64Encoded1024Byte;

        @Setup
        public void setup() {
            bytes8Byte = Bytes.random(8);
            bytes16Byte = Bytes.random(16);
            bytes1024Byte = Bytes.random(1024);

            hexEncoded8Byte = bytes8Byte.encodeHex();
            hexEncoded16Byte = bytes16Byte.encodeHex();
            hexEncoded1024Byte = bytes1024Byte.encodeHex();

            base32Encoded8Byte = bytes8Byte.encodeBase32();
            base32Encoded16Byte = bytes16Byte.encodeBase32();
            base32Encoded1024Byte = bytes1024Byte.encodeBase32();

            base64Encoded8Byte = bytes8Byte.encodeBase64();
            base64Encoded16Byte = bytes16Byte.encodeBase64();
            base64Encoded1024Byte = bytes1024Byte.encodeBase64();
        }
    }

    @Benchmark
    public void hexEncoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes8Byte.encodeHex());
    }

    @Benchmark
    public void hexDecoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseHex(state.hexEncoded8Byte));
    }

    @Benchmark
    public void hexEncoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes16Byte.encodeHex());
    }

    @Benchmark
    public void hexDecoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseHex(state.hexEncoded16Byte));
    }

    @Benchmark
    public void hexEncoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes1024Byte.encodeHex());
    }

    @Benchmark
    public void hexDecoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseHex(state.hexEncoded1024Byte));
    }

    @Benchmark
    public void base64Encoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes8Byte.encodeBase64());
    }

    @Benchmark
    public void base64Decoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase64(state.base64Encoded8Byte));
    }

    @Benchmark
    public void base64Encoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes16Byte.encodeBase64());
    }

    @Benchmark
    public void base64Decoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase64(state.base64Encoded16Byte));
    }

    @Benchmark
    public void base64Encoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes1024Byte.encodeBase64());
    }

    @Benchmark
    public void base64Decoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase64(state.base64Encoded1024Byte));
    }

    @Benchmark
    public void base32Encoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes8Byte.encodeBase32());
    }

    @Benchmark
    public void base32Decoding8Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase32(state.base32Encoded8Byte));
    }

    @Benchmark
    public void base32Encoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes16Byte.encodeBase32());
    }

    @Benchmark
    public void base32Decoding16Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase32(state.base32Encoded16Byte));
    }

    @Benchmark
    public void base32Encoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.bytes1024Byte.encodeBase32());
    }

    @Benchmark
    public void base32Decoding1024Byte(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Bytes.parseBase32(state.base32Encoded1024Byte));
    }
}
