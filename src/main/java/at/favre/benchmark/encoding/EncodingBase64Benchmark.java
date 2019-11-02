/*
 * Copyright 2018 Patrick Favre-Bulle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package at.favre.benchmark.encoding;

import at.favre.lib.bytes.BinaryToTextEncoding;
import at.favre.lib.bytes.Bytes;
import org.openjdk.jmh.annotations.*;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
# JMH version: 1.21
# VM version: JDK 1.8.0_172, Java HotSpot(TM) 64-Bit Server VM, 25.172-b11
# i7 7700K / 24G

Benchmark                                (byteLength)   Mode  Cnt         Score         Error  Units
EncodingBase64Benchmark.encodeBase64Apache            16  thrpt       1026017,030          ops/s
EncodingBase64Benchmark.encodeBase64Apache           128  thrpt        480232,057          ops/s
EncodingBase64Benchmark.encodeBase64Apache       1000000  thrpt           103,982          ops/s
EncodingBase64Benchmark.encodeBase64Bytes             16  thrpt       6364980,550          ops/s
EncodingBase64Benchmark.encodeBase64Bytes            128  thrpt       1387777,471          ops/s
EncodingBase64Benchmark.encodeBase64Bytes        1000000  thrpt           105,544          ops/s
EncodingBase64Benchmark.encodeBase64Guava             16  thrpt       5629388,958          ops/s
EncodingBase64Benchmark.encodeBase64Guava            128  thrpt       1320255,472          ops/s
EncodingBase64Benchmark.encodeBase64Guava        1000000  thrpt           226,190          ops/s
EncodingBase64Benchmark.encodeBase64Jdk8              16  thrpt       7116995,789          ops/s
EncodingBase64Benchmark.encodeBase64Jdk8             128  thrpt       1913779,098          ops/s
EncodingBase64Benchmark.encodeBase64Jdk8         1000000  thrpt           245,037          ops/s
 */

@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 3, time = 10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class EncodingBase64Benchmark {

    @Param({"4", "8", "16", "32", "128", "512", "1000000"})
    private int byteLength;
    private Map<Integer, Bytes[]> rndMap;

    private BinaryToTextEncoding.EncoderDecoder base64Bytes;
    private BinaryToTextEncoding.EncoderDecoder base64Guava;
    private BinaryToTextEncoding.EncoderDecoder base64Apache;
    private BinaryToTextEncoding.EncoderDecoder base64Java8;
    private Random random;

    @Setup(Level.Trial)
    public void setup() {
        random = new Random();
        base64Bytes = new BytesBase64();
        base64Apache = new ApacheCommonCodecBase64();
        base64Guava = new GuavaBase64();
        base64Java8 = new Jdk8Base64();

        rndMap = new HashMap<>();
        int[] lengths = new int[]{4, 8, 16, 32, 128, 512, 1000000};
        for (int length : lengths) {
            int count = 10;
            rndMap.put(length, new Bytes[count]);
            for (int i = 0; i < count; i++) {
                rndMap.get(length)[i] = Bytes.random(length);
            }
        }
    }

    @Benchmark
    public byte[] encodeBase64Bytes() {
        return encodeDecode(base64Bytes);
    }

    @Benchmark
    public byte[] encodeBase64Apache() {
        return encodeDecode(base64Apache);
    }

    @Benchmark
    public byte[] encodeBase64Guava() {
        return encodeDecode(base64Guava);
    }

    @Benchmark
    public byte[] encodeBase64Jdk8() {
        return encodeDecode(base64Java8);
    }

    private byte[] encodeDecode(BinaryToTextEncoding.EncoderDecoder encoder) {
        Bytes[] bytes = rndMap.get(byteLength);
        int rndNum = random.nextInt(bytes.length);

        String encoded = encoder.encode(bytes[rndNum].array(), ByteOrder.BIG_ENDIAN);
        return encoder.decode(encoded);
    }

    static final class BytesBase64 implements BinaryToTextEncoding.EncoderDecoder {
        @Override
        public String encode(byte[] array, ByteOrder byteOrder) {
            return Bytes.wrap(array).encodeBase64();
        }

        @Override
        public byte[] decode(CharSequence encoded) {
            return Bytes.parseBase64(encoded).array();
        }
    }

    static final class ApacheCommonCodecBase64 implements BinaryToTextEncoding.EncoderDecoder {
        @Override
        public String encode(byte[] array, ByteOrder byteOrder) {
            return org.apache.commons.codec.binary.Base64.encodeBase64String(array);
        }

        @Override
        public byte[] decode(CharSequence encoded) {
            return org.apache.commons.codec.binary.Base64.decodeBase64(encoded.toString());
        }
    }

    static final class GuavaBase64 implements BinaryToTextEncoding.EncoderDecoder {
        @Override
        public String encode(byte[] array, ByteOrder byteOrder) {
            return com.google.common.io.BaseEncoding.base64().encode(array);
        }

        @Override
        public byte[] decode(CharSequence encoded) {
            return com.google.common.io.BaseEncoding.base64().decode(encoded);
        }
    }

    static final class Jdk8Base64 implements BinaryToTextEncoding.EncoderDecoder {
        @Override
        public byte[] decode(CharSequence charSequence) {
            return java.util.Base64.getDecoder().decode(charSequence.toString());
        }

        @Override
        public String encode(byte[] bytes, ByteOrder byteOrder) {
            return java.util.Base64.getEncoder().encodeToString(bytes);
        }
    }
}
