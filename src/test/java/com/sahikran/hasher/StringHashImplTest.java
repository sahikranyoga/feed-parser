package com.sahikran.hasher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringHashImplTest {
    
    private Hash<String> hash;

    @BeforeEach
    void init(){
        hash = new StringHashImpl();
    }

    @RepeatedTest(10)
    @DisplayName("verify deterministic value of hashing for an input string")
    public void whenStringHashed_generateSameHashValue(){
        long hashValue = hash.generateHash("hello");
        assertEquals(99162322L, hashValue);
    }
}
