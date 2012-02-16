package name.nirav.excel.streaming.parser;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;


public class StreamingParserTest {
    @Test
    public void testHandlesBadExcel() throws Throwable{
        List<List<String>> data = new StreamingParser().parse(getClass().getResource("sample.xlsx").getFile());
        assertEquals(19, data.size());
        for (List<String> list : data) {
            assertEquals(4, list.size());
            System.out.println(list);
        }
        
    }
}
