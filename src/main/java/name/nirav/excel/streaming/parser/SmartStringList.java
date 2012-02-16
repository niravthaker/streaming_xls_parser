package name.nirav.excel.streaming.parser;

import java.util.ArrayList;

public class SmartStringList extends ArrayList<String> {
    private static final long serialVersionUID = -3646599559909817934L;

    public SmartStringList() {
        super();
    }

    public SmartStringList(int smartSize) {
        super(smartSize);
    }

    public boolean isEmptyRow() {
        for (String str : this) {
            if (str != null)
                return false;
        }
        return true;
    }

    public boolean isMostlyEmptyRow(int nullTolerance) {
        int nonNullCount = 0;
        for (String str : this) {
            if (str != null)
                nonNullCount++;
        }
        return nullTolerance > nonNullCount;
    }
}