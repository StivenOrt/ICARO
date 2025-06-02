package Ventanas;

import javax.swing.JFrame;
import Conexiones.Conexion; // Asegúrate de que esta clase exista y funcione
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Importaciones para Apache POI (Excel y Word)
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Para .xlsx (Excel)
import org.apache.poi.xwpf.usermodel.XWPFDocument; // Para .docx (Word)
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.IOException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

// Importaciones para iText (PDF)
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk; // Para espaciados o elementos simples
import com.itextpdf.text.Font; // Para fuentes
import com.itextpdf.text.FontFactory; // Para crear fuentes
import com.itextpdf.text.BaseColor; // Para colores de fuente o fondo


public class GenerarReportes extends javax.swing.JFrame {
    
    private String formatoSeleccionado = "";
    
    public GenerarReportes (java.awt.Frame parent) {
        super();
        initComponents(); // NetBeans genera el código aquí y los ActionPerformed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setTitle("GENERAR REPORTES EN:"); // Título de la ventana
    }
    
    private void generarReporteWord(File archivo) {
        XWPFDocument document = new XWPFDocument();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            // Asegúrate de que la query seleccione todas las columnas necesarias
            String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // Título del reporte
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("REPORTE DE INVENTARIO - WORD");
            titleRun.setBold(true);
            titleRun.setFontSize(18);
            titleRun.addBreak(); // Salto de línea

            // Encabezados de la tabla como párrafos
            XWPFParagraph header = document.createParagraph();
            XWPFRun headerRun = header.createRun();
            headerRun.setText("Código | Nombre | P. Venta | P. Compra | Stock | Marca | Descuento | Descripción");
            headerRun.setBold(true);
            headerRun.addBreak();

            // Datos del inventario
            while (rs.next()) {
                XWPFParagraph productPara = document.createParagraph();
                XWPFRun productRun = productPara.createRun();
                productRun.setText(
                    String.format("%s | %s | %.2f | %.2f | %d | %s | %.2f%% | %s",
                        rs.getString("IdProducto"),
                        rs.getString("Nombre"),
                        rs.getDouble("Precio"),
                        rs.getDouble("PrecioCompra"),
                        rs.getInt("Stock"),
                        rs.getString("Marca"),
                        rs.getDouble("Descuento"),
                        rs.getString("Descripcion")
                    )
                );
                productRun.addBreak();
            }

            try (FileOutputStream out = new FileOutputStream(archivo)) {
                document.write(out);
            }
            JOptionPane.showMessageDialog(this, "Reporte WORD generado exitosamente en:\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte WORD: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void generarReporteExcel(File archivo) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Inventario");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            // --- Definir estilos de celda ---
             // Estilo para todas las celdas (bordes finos)
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // Estilo para el texto en negrita (para la cabecera)
            XSSFFont boldFont = workbook.createFont();
            boldFont.setBold(true);

            // Estilo para el encabezado (bordes finos y texto en negrita, SIN color de fondo)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(borderStyle); // Copia los bordes finos
            headerStyle.setFont(boldFont); // Aplica la fuente en negrita

            // Encabezados de la tabla
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"IdProducto", "Nombre", "Precio", "PrecioCompra", "Stock", "Marca", "Descuento", "Descripción"};
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle); // Aplicar estilo al encabezado
            }

             // Datos
            int rowNum = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(rowNum);

                row.createCell(0).setCellValue(rs.getInt("IdProducto"));
                row.createCell(1).setCellValue(rs.getString("Nombre"));
                row.createCell(2).setCellValue(rs.getDouble("Precio"));
                row.createCell(3).setCellValue(rs.getDouble("PrecioCompra"));
                row.createCell(4).setCellValue(rs.getInt("Stock"));
                row.createCell(5).setCellValue(rs.getString("Marca"));
                row.createCell(6).setCellValue(rs.getDouble("Descuento"));
                row.createCell(7).setCellValue(rs.getString("Descripcion"));

                // Aplicar estilo de borde fino a todas las celdas de datos
                for (int i = 0; i < headers.length; i++) {
                    XSSFCell cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellStyle(borderStyle); // Aplica el estilo con bordes finos
                    }
                }
                rowNum++;
            }

            // Autoajustar columnas para que el contenido sea visible
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // --- Guardar el archivo ---
            try (FileOutputStream out = new FileOutputStream(archivo)) {
                workbook.write(out);
                System.out.println("Reporte EXCEL generado exitosamente en:\n" + archivo); // Usar 'archivo' directamente
                JOptionPane.showMessageDialog(null, "Reporte EXCEL generado exitosamente en:\n" + archivo, "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                System.err.println("Error de E/S al generar el reporte Excel: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al generar reporte EXCEL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al generar el reporte Excel: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar reporte EXCEL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el reporte Excel: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inesperado al generar reporte EXCEL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos DB: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void generarReportePdf(File archivo) {
        Document document = new Document();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();
            
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("REPORTE DE INVENTARIO - PDF", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            conn = Conexion.conectar();
            String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            PdfPTable table = new PdfPTable(8); // 8 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            float[] columnWidths = {1f, 2f, 1f, 1f, 0.8f, 1.2f, 0.8f, 2f};
            table.setWidths(columnWidths);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            BaseColor headerBgColor = new BaseColor(60, 141, 188); 

            // Celdas de encabezado para PDF
            com.itextpdf.text.Phrase phrase; 
            com.itextpdf.text.pdf.PdfPCell headerCell; 

            phrase = new com.itextpdf.text.Phrase("ID", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            phrase = new com.itextpdf.text.Phrase("Nombre", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            phrase = new com.itextpdf.text.Phrase("P. Venta", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            phrase = new com.itextpdf.text.Phrase("P. Compra", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            phrase = new com.itextpdf.text.Phrase("Stock", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            phrase = new com.itextpdf.text.Phrase("Marca", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
            
            phrase = new com.itextpdf.text.Phrase("Desc. (%)", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            phrase = new com.itextpdf.text.Phrase("Descripción", headerFont);
            headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);

            // Datos de la tabla PDF
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            while (rs.next()) {
                table.addCell(new Paragraph(rs.getString("IdProducto"), dataFont));
                table.addCell(new Paragraph(rs.getString("Nombre"), dataFont));
                table.addCell(new Paragraph(String.format("%,.2f", rs.getDouble("Precio")), dataFont));
                table.addCell(new Paragraph(String.format("%,.2f", rs.getDouble("PrecioCompra")), dataFont));
                table.addCell(new Paragraph(String.valueOf(rs.getInt("Stock")), dataFont));
                table.addCell(new Paragraph(rs.getString("Marca"), dataFont));
                table.addCell(new Paragraph(String.format("%,.2f", rs.getDouble("Descuento")), dataFont));
                table.addCell(new Paragraph(rs.getString("Descripcion"), dataFont));
            }
            document.add(table); // Añade la tabla al documento

            document.close(); // Cierra el documento
            JOptionPane.showMessageDialog(this, "Reporte PDF generado exitosamente en:\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    private void generarReporte() {
        if (formatoSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un formato de reporte (WORD, EXCEL o PDF).", "Formato No Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        // Establecer el nombre de archivo por defecto con la extensión correcta
        fileChooser.setSelectedFile(new File("ReporteInventario." + formatoSeleccionado.toLowerCase()));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Asegurarse de que el archivo tenga la extensión correcta si el usuario no la puso
            String filePath = fileToSave.getAbsolutePath();
            String extension = "." + formatoSeleccionado.toLowerCase();
            if (!filePath.toLowerCase().endsWith(extension)) {
                fileToSave = new File(filePath + extension);
            }

            switch (formatoSeleccionado) {
                case "DOCX":
                    generarReporteWord(fileToSave);
                    break;
                case "XLSX":
                    generarReporteExcel(fileToSave);
                    break;
                case "PDF":
                    generarReportePdf(fileToSave);
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnWord = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnGenerar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("GENERAR REPORTES EN:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        btnWord.setBackground(new java.awt.Color(0, 51, 255));
        btnWord.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        btnWord.setForeground(new java.awt.Color(255, 255, 255));
        btnWord.setText("WORD");
        btnWord.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        btnWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWordActionPerformed(evt);
            }
        });

        btnPDF.setBackground(new java.awt.Color(204, 0, 0));
        btnPDF.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        btnPDF.setForeground(new java.awt.Color(255, 255, 255));
        btnPDF.setText("PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });

        btnExcel.setBackground(new java.awt.Color(0, 82, 0));
        btnExcel.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        btnExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnExcel.setText("EXCEL");
        btnExcel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        btnGenerar.setBackground(new java.awt.Color(204, 204, 204));
        btnGenerar.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        btnGenerar.setText("Generar");
        btnGenerar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnWord, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(231, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnWord, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnGenerar)))
                .addGap(18, 18, 18)
                .addComponent(btnPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        generarReporte(); // Llama al método principal de generación de reporte
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void btnWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWordActionPerformed
        formatoSeleccionado = "DOCX";
        JOptionPane.showMessageDialog(this, "Has seleccionado WORD.", "Formato", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnWordActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        formatoSeleccionado = "XLSX";
        JOptionPane.showMessageDialog(this, "Has seleccionado EXCEL.", "Formato", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnExcelActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        formatoSeleccionado = "PDF";
        JOptionPane.showMessageDialog(this, "Has seleccionado PDF.", "Formato", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnPDFActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnWord;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
