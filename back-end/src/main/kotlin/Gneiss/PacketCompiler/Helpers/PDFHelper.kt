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
import kotlin.collections.List;

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter

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
    override fun csvToPDF(filename: String): String {
        //READ: This function is meant to take a file path to the CSV and convert it to the PDF. We found
        //an existing endpoint, but it's in java. We've refactored a lot of it to Kotlin, but there is
        //still more work to do. Here is the endpoint for reference: https://github.com/roytuts/java/blob/master/java-csv-to-pdf/src/main/java/com/roytuts/java/csv/to/pdf/CsvToPdfConverter.java
        //More files from that may need to be added, you'd need to do research in that.

            var path = Paths.get(filename);
            var bytes = Files.readAllBytes(path);
            var str = String(bytes);

            //String[] splitted = Arrays.stream(str.split("\n")).map(String::trim).toArray(String[]::new);
    
            /* List<String> list = Arrays.asList(splitted);
    
            Document document = new Document(PageSize.A4, 25, 25, 25, 25);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("student.pdf"));
    
            document.open();
    
            Paragraph heading = new Paragraph("~: Student Details :~",
                    FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new BaseColor(0, 255, 0)));
    
            document.add(heading);
    
            PdfPTable t = new PdfPTable(4);
            t.setSpacingBefore(25);
            t.setSpacingAfter(25);
    
            boolean isHeader = true;
            for (String record : list) {
                String[] line = Arrays.stream(record.split(",")).map(String::trim).toArray(String[]::new);*/
                var line = []
                var invoiceNumber = line[0];
                var billingDate = line[1];
                var billTo = line[2];
                var cO = line[3];
                var mailOrEmail = line[4];
                var contactInfo = line[5];
                var addressLine1 = line[6];
                var addressLine2 = line[7];
                var city = line[8];
                var state = line[9];
                var zipCode = line[10];
                var zipCodeExtension = line[11];
                var dateOfService = line[12];
                var description = line[13];
                var accountingString = line[14];
                var amount = line[15];
                var total = line[16];
    
                /* if (isHeader) {
                    PdfPCell c1 = new PdfPCell(new Phrase(id));
                    t.addCell(c1);
    
                    PdfPCell c2 = new PdfPCell(new Phrase(dob));
                    t.addCell(c2);
    
                    PdfPCell c3 = new PdfPCell(new Phrase(email));
                    t.addCell(c3);
    
                    PdfPCell c4 = new PdfPCell(new Phrase(address));
                    t.addCell(c4);
                } else {
                    PdfPCell c1 = new PdfPCell(new Phrase(id));
                    t.addCell(c1);
    
                    PdfPCell c2 = new PdfPCell(new Phrase(dob));
                    t.addCell(c2);
    
                    PdfPCell c3 = new PdfPCell(new Phrase(email));
                    t.addCell(c3);
    
                    PdfPCell c4 = new PdfPCell(new Phrase(address));
                    t.addCell(c4);
                }
    
            }
    
            document.add(t);
    
            document.close();
            pdfWriter.close(); */
            //Not sure if below should be here or not
            return str
                
        };
}
