package name.nirav.excel.streaming.parser;


import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OOXmlSheetHandler extends DefaultHandler {
    private static final String EMPTY         = "";
    private static final String TAG_CELL      = "c";
    private static final String TAG_ROW       = "row";
    private static final String TAG_VAL       = "v";
    private static final String ATTR_TYPE     = "t";
    private static final String ATTR_SHARED_STRING = "s";
    private final SharedStringsTable  sst;
    private final RowListener   rowListener;

    //mutables
    private String              lastContents;
    private boolean             isSharedString;
    private SmartStringList     cells         = new SmartStringList();

    public OOXmlSheetHandler(SharedStringsTable sst) {
        this.sst = sst;
        rowListener = RowListener.noop;
    }

    public OOXmlSheetHandler(SharedStringsTable sst, RowListener rowListener) {
        this.sst = sst;
        this.rowListener = rowListener;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equals(TAG_CELL)) {
            String cellType = attributes.getValue(ATTR_TYPE);
            isSharedString = ATTR_SHARED_STRING.equals(cellType);
        }
        lastContents = EMPTY;
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        if (isSharedString) {
            int idx = Integer.valueOf(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            isSharedString = false;
        }
        if (name.equals(TAG_CELL) && lastContents.isEmpty()) {
            cells.add(null);
        }
        if (name.equals(TAG_VAL)) {
            cells.add(lastContents);
        }
        if (name.equals(TAG_ROW)) {
            rowListener.onNewRow(cells);
            int smartSize = cells.size();
            cells = new SmartStringList(smartSize);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length == 0)
            lastContents = EMPTY;
        else
            lastContents = new String(ch, start, length);
    }
}