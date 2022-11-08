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
//import java.nio.file.Path;
import java.nio.file.Paths;

class CSVHelper {
    //
    fun readCSV(filename: String) {
        var path = Paths.get(fileName);
        Stream<String> lines = Files.lines(path).skip(1); // skipping the header
    
        for line in lines {
            var info = listOf(line.split(","));
    
            var sl = info.get(0);
            var id = info.get(1);
            var name = info.get(2);
            var manager = info.get(3);
    
            System.out.println("Sl No: " + sl + ", ID: " + id + ", Name: " + name + ", Manager: " + manager);
        };
    
        lines.close();
    }
}