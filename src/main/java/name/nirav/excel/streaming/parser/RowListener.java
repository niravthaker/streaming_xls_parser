package name.nirav.excel.streaming.parser;



interface RowListener{
    NoOp noop = new NoOp();
    public class NoOp implements RowListener{ 
        public void onNewRow(SmartStringList cells) {}
    }
    void onNewRow(SmartStringList cells);
}