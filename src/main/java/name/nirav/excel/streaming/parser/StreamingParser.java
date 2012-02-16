package name.nirav.excel.streaming.parser;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class StreamingParser {
    private static final class CountingListener implements RowListener{
        List<List<String>> listOfListOfString = new ArrayList<List<String>>();

        public void onNewRow(SmartStringList cells) {
            if(!cells.isEmpty())
                listOfListOfString.add(cells);
        }
    }

    public static final RowListener PrintingListener = new RowListener() {
        public void onNewRow(SmartStringList cells) {
                if (cells.isEmptyRow())
                    System.out.println("<empty row>");
                else
                    System.out.println(cells);
            }
    };

    public List<List<String>> parse(String file) throws Exception {
        CountingListener listener = new CountingListener();
        readSheet(file, 1, listener);
        return listener.listOfListOfString;
    }

    protected void readSheet(String file, int sheetNo, RowListener listener) throws ParseException {
        OPCPackage pkg = null;
        InputStream sheet = null;
        try {
            pkg = OPCPackage.open(file);
            XSSFReader reader = new XSSFReader(pkg);
            SharedStringsTable sst = reader.getSharedStringsTable();
            XMLReader parser = newSheetParser(sst, new OOXmlSheetHandler(sst, listener));
            sheet = reader.getSheet(format("rId%d", sheetNo));
            parser.parse(new InputSource(sheet));
            sheet.close();
        } catch (Exception e) {
            throw new ParseException(e);
        } finally {
            if(sheet != null)
                try {
                    sheet.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            if (pkg != null)
                try {
                    pkg.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public XMLReader newSheetParser(SharedStringsTable sst, OOXmlSheetHandler oOXmlSheetHandler) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(oOXmlSheetHandler);
        return parser;
    }

}
