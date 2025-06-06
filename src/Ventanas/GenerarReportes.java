package Ventanas;

import javax.swing.JFrame;
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

import javax.swing.table.DefaultTableModel;


public class GenerarReportes extends javax.swing.JFrame {
    
    private String formatoSeleccionado = "";
    private Connection conn;
    private String tipoDeReporte;
    private DefaultTableModel datosTablaReporte;
    
    public GenerarReportes(java.awt.Frame parent, Connection conexion, String tipoReporte, DefaultTableModel datosTabla) {
        super();
        this.conn = conexion;
        this.tipoDeReporte = tipoReporte;
        this.datosTablaReporte = datosTabla; // Esto será null para inventario, y el modelo de la tabla de ventas para ventas

        initComponents(); // NetBeans genera el código aquí y los ActionPerformed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Adaptar el título de la ventana según el tipo de reporte
        if ("inventario".equals(tipoReporte)) {
            setTitle("GENERAR REPORTE DE INVENTARIO EN:");
        } else if ("ventas".equals(tipoReporte)) {
            setTitle("GENERAR REPORTE DE VENTAS EN:");
        } else {
            setTitle("GENERAR REPORTE EN:"); // Título genérico si no se especifica tipo
        }
    }
    
    private void generarReporteWord(File archivo) {
        XWPFDocument document = new XWPFDocument();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Título del reporte
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(18);

            // Encabezados de la tabla como párrafos
            XWPFParagraph header = document.createParagraph();
            XWPFRun headerRun = header.createRun();
            headerRun.setBold(true);

            if ("inventario".equals(tipoDeReporte)) {
                titleRun.setText("REPORTE DE INVENTARIO - WORD");
                headerRun.setText("ID | Nombre | P. Venta | P. Compra | Stock | Marca | Descuento | Descripción");
                String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

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
            } else if ("ventas".equals(tipoDeReporte) && datosTablaReporte != null) {
                titleRun.setText("REPORTE DE VENTAS - WORD");
                // Construir encabezados dinámicamente desde el DefaultTableModel
                StringBuilder headersBuilder = new StringBuilder();
                for (int i = 0; i < datosTablaReporte.getColumnCount(); i++) {
                    headersBuilder.append(datosTablaReporte.getColumnName(i));
                    if (i < datosTablaReporte.getColumnCount() - 1) {
                        headersBuilder.append(" | ");
                    }
                }
                headerRun.setText(headersBuilder.toString());

                // Recorrer los datos del DefaultTableModel
                for (int row = 0; row < datosTablaReporte.getRowCount(); row++) {
                    XWPFParagraph dataPara = document.createParagraph();
                    XWPFRun dataRun = dataPara.createRun();
                    StringBuilder rowData = new StringBuilder();
                    for (int col = 0; col < datosTablaReporte.getColumnCount(); col++) {
                        rowData.append(datosTablaReporte.getValueAt(row, col).toString());
                        if (col < datosTablaReporte.getColumnCount() - 1) {
                            rowData.append(" | ");
                        }
                    }
                    dataRun.setText(rowData.toString());
                    dataRun.addBreak();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de reporte no válido o datos no proporcionados para WORD.", "Error de Reporte", JOptionPane.ERROR_MESSAGE);
                return; // Salir si no hay tipo o datos
            }

            titleRun.addBreak(); // Salto de línea después del título
            headerRun.addBreak(); // Salto de línea después de los encabezados

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
                // No cerrar 'conn' aquí, ya que se pasa desde fuera
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void generarReporteExcel(File archivo) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Reporte"); // Nombre de la hoja
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // --- Definir estilos de celda ---
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            XSSFFont boldFont = workbook.createFont();
            boldFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(borderStyle);
            headerStyle.setFont(boldFont);

            // Encabezados y datos
            String[] headers;
            int rowNum = 0;

            if ("inventario".equals(tipoDeReporte)) {
                headers = new String[]{"IdProducto", "Nombre", "Precio", "PrecioCompra", "Stock", "Marca", "Descuento", "Descripción"};
                String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                // Escribir encabezados
                XSSFRow headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    XSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Escribir datos
                while (rs.next()) {
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rs.getInt("IdProducto"));
                    row.createCell(1).setCellValue(rs.getString("Nombre"));
                    row.createCell(2).setCellValue(rs.getDouble("Precio"));
                    row.createCell(3).setCellValue(rs.getDouble("PrecioCompra"));
                    row.createCell(4).setCellValue(rs.getInt("Stock"));
                    row.createCell(5).setCellValue(rs.getString("Marca"));
                    row.createCell(6).setCellValue(rs.getDouble("Descuento"));
                    row.createCell(7).setCellValue(rs.getString("Descripcion"));

                    for (int i = 0; i < headers.length; i++) {
                        XSSFCell cell = row.getCell(i);
                        if (cell != null) {
                            cell.setCellStyle(borderStyle);
                        }
                    }
                }
            } else if ("ventas".equals(tipoDeReporte) && datosTablaReporte != null) {
                // Obtener encabezados del modelo de tabla
                headers = new String[datosTablaReporte.getColumnCount()];
                for (int i = 0; i < datosTablaReporte.getColumnCount(); i++) {
                    headers[i] = datosTablaReporte.getColumnName(i);
                }

                // Escribir encabezados
                XSSFRow headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    XSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Escribir datos del modelo de tabla
                for (int row = 0; row < datosTablaReporte.getRowCount(); row++) {
                    XSSFRow excelRow = sheet.createRow(rowNum++);
                    for (int col = 0; col < datosTablaReporte.getColumnCount(); col++) {
                        XSSFCell cell = excelRow.createCell(col);
                        Object value = datosTablaReporte.getValueAt(row, col);
                        if (value != null) {
                            cell.setCellValue(value.toString()); // Convertir todo a String por simplicidad
                        }
                        cell.setCellStyle(borderStyle);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de reporte no válido o datos no proporcionados para EXCEL.", "Error de Reporte", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Autoajustar columnas
            if (headers != null) {
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // Guardar el archivo
            try (FileOutputStream out = new FileOutputStream(archivo)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Reporte EXCEL generado exitosamente en:\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error de E/S al generar el reporte Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error SQL al generar el reporte Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado al generar el reporte Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void generarReportePdf(File archivo) {
        Document document = new Document();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("", titleFont); // Título vacío por ahora
            title.setAlignment(Paragraph.ALIGN_CENTER);
            //document.add(title);
            //document.add(Chunk.NEWLINE);

            PdfPTable table;
            String[] headers;

            if ("inventario".equals(tipoDeReporte)) {
                title = new Paragraph("REPORTE DE INVENTARIO - PDF", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);// Asignar el contenido aquí
                document.add(title); // Añadir el título con el contenido actualizado
                document.add(Chunk.NEWLINE);

                headers = new String[]{"ID", "Nombre", "P. Venta", "P. Compra", "Stock", "Marca", "Desc. (%)", "Descripción"};
                table = new PdfPTable(headers.length); // 8 columnas
                float[] columnWidths = {1f, 2f, 1f, 1f, 0.8f, 1.2f, 0.8f, 2f};
                table.setWidths(columnWidths);

                String sql = "SELECT IdProducto, Nombre, Precio, PrecioCompra, Stock, Marca, Descuento, Descripcion FROM producto";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                // Llenar encabezados de tabla PDF (para inventario)
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
                BaseColor headerBgColor = new BaseColor(60, 141, 188);
                for (String headerText : headers) {
                    com.itextpdf.text.Phrase phrase = new com.itextpdf.text.Phrase(headerText, headerFont);
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
                    headerCell.setBackgroundColor(headerBgColor);
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(5);
                    table.addCell(headerCell);
                }

                // Llenar datos de tabla PDF (para inventario)
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
            } else if ("ventas".equals(tipoDeReporte) && datosTablaReporte != null) {
                title = new Paragraph("REPORTE DE VENTAS - PDF", titleFont);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title); // Añadir el título con el contenido actualizado
                document.add(Chunk.NEWLINE);

                // Obtener encabezados y datos del modelo de tabla
                headers = new String[datosTablaReporte.getColumnCount()];
                for (int i = 0; i < datosTablaReporte.getColumnCount(); i++) {
                    headers[i] = datosTablaReporte.getColumnName(i);
                }
                table = new PdfPTable(headers.length);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Asignar anchos de columna (puedes ajustar estos valores)
                float[] dynamicWidths = new float[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    dynamicWidths[i] = 1.5f; // Un ancho base, puedes afinarlo
                    if (headers[i].contains("Descripción") || headers[i].contains("Nombre")) dynamicWidths[i] = 2.5f;
                    else if (headers[i].contains("ID")) dynamicWidths[i] = 0.8f;
                }
                table.setWidths(dynamicWidths);

                // Llenar encabezados de tabla PDF (para ventas)
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
                BaseColor headerBgColor = new BaseColor(60, 141, 188);
                for (String headerText : headers) {
                    com.itextpdf.text.Phrase phrase = new com.itextpdf.text.Phrase(headerText, headerFont);
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(phrase);
                    headerCell.setBackgroundColor(headerBgColor);
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(5);
                    table.addCell(headerCell);
                }

                // Llenar datos de tabla PDF (para ventas desde el modelo de tabla)
                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
                for (int row = 0; row < datosTablaReporte.getRowCount(); row++) {
                    for (int col = 0; col < datosTablaReporte.getColumnCount(); col++) {
                        Object value = datosTablaReporte.getValueAt(row, col);
                        table.addCell(new Paragraph(value != null ? value.toString() : "", dataFont));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de reporte no válido o datos no proporcionados para PDF.", "Error de Reporte", JOptionPane.ERROR_MESSAGE);
                return;
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

        // Determinar el nombre base del archivo según el tipo de reporte
        String nombreBase = "Reporte";
        if ("inventario".equals(tipoDeReporte)) {
            nombreBase = "ReporteInventario";
        } else if ("ventas".equals(tipoDeReporte)) {
            nombreBase = "ReporteVentas";
        }
        
        fileChooser.setSelectedFile(new File(nombreBase + "." + formatoSeleccionado.toLowerCase()));

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
