package Gneiss.PacketCompiler.Helpers

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import java.io.*;
import au.com.bytecode.opencsv.CSVReader;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.stream.Stream;

class CSVHelper() : ICSVHelper {
    fun writeFile(fileName: String, bytes: ByteArray) {
        var file = File(fileName)
        file.writeBytes(bytes)
    }
    fun getTextFromPDF(pdfBytes: ByteArray): String {
        var handler = BodyContentHandler()
        var metadata = Metadata()
        var inputStream = ByteArrayInputStream(pdfBytes)
        var pcontext = ParseContext()

        var pdfparser = PDFParser(); pdfparser.parse(inputStream, handler, metadata, pcontext)
        return handler.toString()
    }

    public static void readCsv2(final String fileName) throws IOException {
        var path = Paths.get(fileName);
        Stream<String> lines = Files.lines(path).skip(1); // skipping the header
    
        lines.forEach(line -> {
            String[] info = line.split(",");
    
            String sl = info[0];
            String id = info[1];
            String name = info[2];
            String manager = info[3];
    
            System.out.println("Sl No: " + sl + ", ID: " + id + ", Name: " + name + ", Manager: " + manager);
        });
    
        lines.close();
    }

    /*fun csvToPDF(fileName: String, csvFile: String) {
        var fileOutputStream = FileOutputStream(fileName)
        HtmlConverter.convertToPdf(csvFile, fileOutputStream)
    }*/
}