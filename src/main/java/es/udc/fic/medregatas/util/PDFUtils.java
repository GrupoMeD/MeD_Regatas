/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author iago
 */
public class PDFUtils {

    public static String PDFS_FOLDER = "pdfs";

    public static void createPdfDirectoryIfNotExists() {
        File theDir = new File(PDFS_FOLDER);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                //handle it
            }
        }
    }

    public static void printPDF(String docName)
            throws IOException, COSVisitorException {

        createPdfDirectoryIfNotExists();

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define a text content stream using the selected font, moving the 
        // cursor and drawing the text "Hello World"
        contentStream.beginText();

        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(100, 700);
        contentStream.drawString("Hello World");
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the results and ensure that the document is properly closed:
        document.save(PDFS_FOLDER + "/" + docName);
        document.close();
    }

    public static void printPDFTable(String docName, TableModel model, String title, String subtitulo)
            throws IOException, COSVisitorException {

        (new GenerateTableThread(docName, model, title, subtitulo)).start();

    }

    static class GenerateTableThread extends Thread {

        private static float TEXT_SIZE = 8;

        private String docName;
        private String title;
        private String subtitulo;
        private TableModel model;

        public GenerateTableThread(String docName, TableModel model,
                String title, String subtitulo) {
            this.docName = docName;
            this.model = model;
            this.title = title;
            this.subtitulo = subtitulo;
        }

        public void run() {

            createPdfDirectoryIfNotExists();

            // Create a document and add a page to it
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a new font object selecting one of the PDF base fonts
            PDFont font = PDType1Font.HELVETICA_BOLD;

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = null;
            try {

                contentStream = new PDPageContentStream(document, page);

                int marginTop = 30; // Or whatever margin you want.

                int fontSize = 16; // Or whatever font size you want.
                float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
                float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2,
                        page.getMediaBox().getHeight() - marginTop - titleHeight);
                contentStream.drawString(title);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(font, 10);
                contentStream.moveTextPositionByAmount(25, 710);
                contentStream.drawString(subtitulo);
                contentStream.endText();

                //Generamos el contenido de la tabla
                String[][] content = new String[model.getRowCount() + 1][model.getColumnCount()];

                for (int i = 0; i < model.getRowCount() + 1; i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        //Cabezeira
                        if (i == 0) {
                            content[i][j] = model.getColumnName(j);
                        } else {
                            Object o = model.getValueAt(i-1, j);
                            content[i][j] = o.toString();
                        }
                    }
                }

                drawTable(page, contentStream, 700, 10, content);
                contentStream.close();
                // Save the results and ensure that the document is properly closed:
                document.save(PDFS_FOLDER + "/" + docName);
                document.close();

                File file = new File(PDFS_FOLDER);
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                Logger.getLogger(PDFUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (COSVisitorException ex) {
                Logger.getLogger(PDFUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /**
         * @param page
         * @param contentStream
         * @param y the y-coordinate of the first row
         * @param margin the padding on left and right of table
         * @param content a 2d array containing the table data
         * @throws IOException
         */
        public static void drawTable(PDPage page, PDPageContentStream contentStream,
                float y, float margin,
                String[][] content) throws IOException {
            final int rows = content.length;
            final int cols = content[0].length;
            final float rowHeight = 20f;
            final float tableWidth = page.findMediaBox().getWidth() - (2 * margin);
            final float tableHeight = rowHeight * rows;
            final float colWidth = tableWidth / (float) cols;
            final float cellMargin = 5f;

            //draw the rows
            float nexty = y;
            for (int i = 0; i <= rows; i++) {
                contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
                nexty -= rowHeight;
            }

            //draw the columns
            float nextx = margin;
            for (int i = 0; i <= cols; i++) {
                contentStream.drawLine(nextx, y, nextx, y - tableHeight);
                nextx += colWidth;
            }

            //now add the text
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, TEXT_SIZE);

            float textx = margin + cellMargin;
            float texty = y - 15;
            for (int i = 0; i < content.length; i++) {
                for (int j = 0; j < content[i].length; j++) {
                    String text = content[i][j];
                    contentStream.beginText();
                    contentStream.moveTextPositionByAmount(textx, texty);
                    contentStream.drawString(text);
                    contentStream.endText();
                    textx += colWidth;
                }
                texty -= rowHeight;
                textx = margin + cellMargin;
            }
        }
    }
}
