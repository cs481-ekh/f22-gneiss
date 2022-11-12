package Gneiss.PacketCompiler.Helpers

import com.itextpdf.html2pdf.HtmlConverter
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.File
import java.io.*;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.stream.Stream;
//import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
class PDFHelper() : IPDFHelper {
    override fun writeFile(fileName: String, bytes: ByteArray) {
        var file = File(fileName)
        file.writeBytes(bytes)
    }
    override fun getTextFromPDF(pdfBytes: ByteArray): String {
        var handler = BodyContentHandler()
        var metadata = Metadata()
        var inputStream = ByteArrayInputStream(pdfBytes)
        var pcontext = ParseContext()

        var pdfparser = PDFParser(); pdfparser.parse(inputStream, handler, metadata, pcontext)
        return handler.toString()
    }
    override fun htmlToPDF(fileName: String, htmlText: String) {
        var fileOutputStream = FileOutputStream(fileName)
        HtmlConverter.convertToPdf(htmlText, fileOutputStream)
    }
    override fun csvToPDF(filename: String) {
            //var path = Paths.get(fileName);
            //Stream<String> lines = Files.lines(path).skip(1); // skipping the header
            //Need to decide how much this function carries, if I want it to both read and convert
            //make sure to return
        
            var path = Paths.get(filename);
    
            var bytes = Files.readAllBytes(path);
    
            var str = String(bytes);
    
            println(str);
    
            // String[] splitted = Arrays.stream(str.split("\n")).map(String::trim).toArray(String[]::new);
    
            // // System.out.println(Arrays.toString(splitted));
    
            // List<String> list = Arrays.asList(splitted);
    
            // var document = Document(PageSize.A4, 25, 25, 25, 25);
            // var pdfWriter = PdfWriter.getInstance(document, FileOutputStream("student.pdf"));
    
            // document.open();
    
            // var heading = Paragraph("~: Student Details :~",
            // 		FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new BaseColor(0, 255, 0)));
    
            // document.add(heading);
    
            // PdfPTable t = new PdfPTable(4);
            // t.setSpacingBefore(25);
            // t.setSpacingAfter(25);
    
            // boolean isHeader = true;
    
            //for line in lines {
                //Keep var info, rest Phillip has commented out in local changes
                //var info = listOf(line.split(","));
        
                // var sl = info.get(0);
                // var id = info.get(1);
                // var name = info.get(2);
                // var manager = info.get(3);
        
                // System.out.println("Sl No: " + sl + ", ID: " + id + ", Name: " + name + ", Manager: " + manager);
                
        };
        
            //lines.close();
            //no returning, just convertin
}
