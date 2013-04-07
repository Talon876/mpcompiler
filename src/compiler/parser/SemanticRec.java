package compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;

public class SemanticRec {

    private RecordType recType;
    private ArrayList<String> data;

    public SemanticRec(RecordType type, String... data)
    {
        setRecType(type);
        this.data = new ArrayList<String>();
        this.data.addAll(Arrays.asList(data));
    }

    public RecordType getRecType() {
        return recType;
    }

    public void setRecType(RecordType recType) {
        this.recType = recType;
    }

    public String getDatum(int idx)
    {
        return data.get(idx);
    }
}
