package com.ibm.bbva.util.cvs;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.Writer;
import java.util.List;

public class CSVWriterIBM extends CSVWriter {

    public CSVWriterIBM(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        super(writer, separator, quotechar, escapechar, lineEnd);
    }

    public CSVWriterIBM(Writer writer, char separator, char quotechar, String lineEnd) {
        super(writer, separator, quotechar, lineEnd);
    }

    public CSVWriterIBM(Writer writer, char separator, char quotechar, char escapechar) {
        super(writer, separator, quotechar, escapechar);
    }

    public CSVWriterIBM(Writer writer, char separator, char quotechar) {
        super(writer, separator, quotechar);
    }

    public CSVWriterIBM(Writer writer, char separator) {
        super(writer, separator);
    }

    public CSVWriterIBM(Writer writer) {
        super(writer);
    }

    public void writeAll(List<Object[]> allLines, FormatoFactory factory)  {
        if (!allLines.isEmpty()) {
            int columnas = allLines.get(0).length;
            String[] strCols = new String[columnas];
            for (int i=0, n=allLines.size(); i<n; i++) {
                Object[] datos = allLines.get(i);
                for (int j=0; j<columnas; j++) {
                    strCols[j] = factory.formato(j, datos[j]);
                }
                writeNext(strCols);
            }
        }
    }
}
