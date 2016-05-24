package passgenerator.myapplication;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alberto on 30/04/2016.
 */
public class XerarPDF {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    //Metodo para engadir o encabezado
    public void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Listado de contrasinais", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated on: "+ new Date(), smallBold));
        addEmptyLine(preface, 1);
        addEmptyLine(preface, 1);

        document.add(preface);

    }

    //Metodo que engade lineas en branco
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    //Metodo que me permite xerar unha taboa con 3 columnas
    public void crearTabla(Document documento, ArrayList<Pares> arrayL)
            throws BadElementException {
        try {
            PdfPTable table = new PdfPTable(3);


            PdfPCell c1 = new PdfPCell(new Phrase("Servicio"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Usuario"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Contrasinal"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);

            for (int x = 0; x < arrayL.size(); x++) {
                table.addCell(arrayL.get(x).getServizo());
                table.addCell(Utilidades.Desencriptar(arrayL.get(x).getUsuario()));
                table.addCell(Utilidades.Desencriptar(arrayL.get(x).getContrasinal()));
            }
            documento.add(table);

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

}