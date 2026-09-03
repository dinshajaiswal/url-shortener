package com.url_shortener.demo.util;

import com.url_shortener.demo.utils.Base62Encoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base62EncoderTest {

    @Test
    void shouldEncodeZero(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("0", encoder.encode(0));
    }

    @Test
    void shouldEncodeSimpleNumber() {
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("Z", encoder.encode(61));
    }

    @Test
    void shouldEncodeNumberGreaterTahnBase(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("10", encoder.encode(62));
    }
}
