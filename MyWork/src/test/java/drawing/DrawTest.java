package drawing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawTest {

    @Test
    void convertHexToRgb() {
        Assertions.assertEquals("0xe64dffff", Draw.convertHexToRgb("e6e64dff").toString());
    }

}