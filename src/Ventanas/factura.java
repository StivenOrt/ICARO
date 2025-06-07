package Ventanas;

import javax.swing.JFrame;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection; // Si tu Form factura maneja la conexión a la BD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel; // Para la tabla de productos
import java.sql.SQLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle; // Para bordes sin bordes
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class factura extends javax.swing.JFrame {
    
    private Connection conn; // Conexión a la BD
    private String facturaIdVenta;
    private Date facturaFechaVenta;
    private double facturaTotalFactura;
    private String facturaClienteNombre; // <- Declarar esta variable
    private String facturaClienteCorreo; // <- Declarar esta variable
    private String facturaClienteTelefono; // <- Declarar esta variable
    private String facturaCajeroNombre; // <- Declarar esta variable
    private String facturaClienteIdentificacion;
    private String facturaClientePais; // <- Declarar esta variable
    private DefaultTableModel facturaProductosModel;

    public factura(Connection connection, String idVenta, Date fechaVenta, double totalFactura,
               String clienteNombre, String clienteCorreo, String clienteTelefono,
               String cajeroNombre, String clienteIdentificacion, 
               String clientePais, DefaultTableModel modeloTablaProductos) {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.conn = connection;

        // Asigna todos los datos recibidos a las variables de instancia
        this.conn = connection;
        this.facturaIdVenta = idVenta;
        this.facturaFechaVenta = fechaVenta;
        this.facturaTotalFactura = totalFactura;
        this.facturaClienteNombre = clienteNombre;
        this.facturaClienteCorreo = clienteCorreo;
        this.facturaClienteTelefono = clienteTelefono;
        this.facturaCajeroNombre = cajeroNombre;
        this.facturaClienteIdentificacion = clienteIdentificacion;
        this.facturaClientePais = clientePais;
        this.facturaProductosModel = modeloTablaProductos;
        
        setTitle("Factura - Venta " + idVenta);
        
        if (txtIdCliente != null) { // Si txtIdCliente es para "ID CLIENTE:"
            txtIdCliente.setText(this.facturaIdVenta); // A veces el ID de venta se pone aquí, o el ID Cliente de la DB
            txtIdCliente.setEditable(false);
        }
        if (txtIdentificacionCliente != null) { 
            txtIdentificacionCliente.setText(this.facturaClienteIdentificacion);
            txtIdentificacionCliente.setEditable(false);
        }

        // Campo "CORREO:"
        if (txtCorreo != null) {
            txtCorreo.setText(this.facturaClienteCorreo); // <-- Aquí se usa la variable de instancia
            txtCorreo.setEditable(false);
        }

        // Campo "CLIENTE:"
        if (txtNombreCliente != null) { // En el Navigator es txtNombreCliente
            txtNombreCliente.setText(this.facturaClienteNombre); // <-- Aquí se usa la variable de instancia
            txtNombreCliente.setEditable(false);
        }

        // Campo "PAIS:"
        if (txtPais != null) {
            txtPais.setText(this.facturaClientePais); // <-- Aquí se usa la variable de instancia
            txtPais.setEditable(false);
        }

        // Campo "TEL:"
        if (txtTelefono != null) {
            txtTelefono.setText(this.facturaClienteTelefono); // <-- Aquí se usa la variable de instancia
            txtTelefono.setEditable(false);
        }

        // Campo "CAJERO:"
        if (txtCajero != null) {
            txtCajero.setText(this.facturaCajeroNombre); // <-- Aquí se usa la variable de instancia
            txtCajero.setEditable(false);
        }

        // Campo "FECHA" (Día, Mes, Año)
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
        SimpleDateFormat sdfMes = new SimpleDateFormat("MM");
        SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy");

        if (txtDia != null) {
            txtDia.setText(sdfDia.format(facturaFechaVenta));
            txtDia.setEditable(false);
        }
        if (txtMes != null) {
            txtMes.setText(sdfMes.format(facturaFechaVenta));
            txtMes.setEditable(false);
        }
        if (txtAno != null) {
            txtAno.setText(sdfAno.format(facturaFechaVenta));
            txtAno.setEditable(false);
        }


        // Establecer el modelo de la tabla de productos
        if (jTableProductosFactura != null) { // Usamos jTable1 según el Navigator
            jTableProductosFactura.setModel(this.facturaProductosModel); // Asignar el modelo de tabla
            jTableProductosFactura.setEnabled(false); // Deshabilita la interacción del usuario
        } else {
            jTableProductosFactura.setModel(modeloTablaProductos);
            jTableProductosFactura.setEnabled(false);
        }
    }
    
    private void reimprimirUltimoTicket() {
        String idVenta = null;

        // Consulta para obtener el ID de la última venta registrada
        String sql = "SELECT IdVenta FROM venta ORDER BY Fecha DESC, IdVenta DESC LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                idVenta = rs.getString("IdVenta");
                // Directamente llama al método para generar y abrir el PDF con los datos de esa venta
                generarYAbrirPDFDeVenta(idVenta);

            } else {
                JOptionPane.showMessageDialog(this, "No hay tickets registrados para imprimir.", "Sin Tickets", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al obtener el último ticket: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado al imprimir el ticket: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Este método contendrá toda la lógica de obtención de datos y generación del PDF
    private void generarYAbrirPDFDeVenta(String idVenta) {
        if (idVenta == null || idVenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el ID de la venta para generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Consultas SQL para obtener los datos de la venta, cliente, cajero y detalles de productos
        String sqlVenta = "SELECT v.Fecha, v.Total, c.Nombre AS NombreCliente, c.Correo AS CorreoCliente, c.Telefono AS TelefonoCliente, u.Nombre AS NombreCajero " +
                          "FROM venta v " +
                          "JOIN cliente c ON v.IdCliente = c.IdCliente " +
                          "JOIN usuario u ON v.IdUsuario = u.IdUsuario " +
                          "WHERE v.IdVenta = ?";

        String sqlDetalle = "SELECT p.Nombre AS NombreProducto, dv.Cantidad, p.Precio AS PrecioUnitario, (dv.Cantidad * p.Precio) AS Subtotal " +
                            "FROM detalleventa dv " +
                            "JOIN producto p ON dv.IdProducto = p.IdProducto " +
                            "WHERE dv.IdVenta = ?";

        try {
            // 1. Obtener datos de la venta principal
            PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta);
            pstmtVenta.setString(1, idVenta);
            ResultSet rsVenta = pstmtVenta.executeQuery();

            if (rsVenta.next()) {
                // Recolectar todos los datos necesarios para la factura
                Date fechaVenta = rsVenta.getTimestamp("Fecha");
                double totalFactura = rsVenta.getDouble("Total");
                String clienteNombre = rsVenta.getString("NombreCliente");
                String clienteCorreo = rsVenta.getString("CorreoCliente");
                String clienteTelefono = rsVenta.getString("TelefonoCliente");
                String cajeroNombre = rsVenta.getString("NombreCajero");

                // Preparar el modelo de tabla para los productos
                DefaultTableModel modeloTablaProductos = new DefaultTableModel();
                modeloTablaProductos.addColumn("Cantidad");
                modeloTablaProductos.addColumn("Descripcion");
                modeloTablaProductos.addColumn("Valor unitario");
                modeloTablaProductos.addColumn("Total");

                // 2. Obtener detalles de los productos vendidos
                PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle);
                pstmtDetalle.setString(1, idVenta);
                ResultSet rsDetalle = pstmtDetalle.executeQuery();

                while (rsDetalle.next()) {
                    modeloTablaProductos.addRow(new Object[]{
                    rsDetalle.getInt("Cantidad"),
                    rsDetalle.getString("NombreProducto"),
                    String.format("%,.2f", rsDetalle.getDouble("PrecioUnitario")),
                    String.format("%,.2f", rsDetalle.getDouble("Subtotal"))
                });
            }

                // --- 3. INICIO DE LA LÓGICA DE GENERACIÓN DEL PDF DENTRO DE ESTA CLASE ---
                String nombreArchivoPDF = "Factura_Venta_" + idVenta + ".pdf";
                File outputFolder = new File("tickets_generados");
                if (!outputFolder.exists()) {
                    outputFolder.mkdirs();
                }
                String rutaGuardado = outputFolder.getAbsolutePath() + File.separator + nombreArchivoPDF;

                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(rutaGuardado));
                document.open();

                // --- Estilos y Fuentes (DEBEN ESTAR AQUÍ DENTRO DE ESTE MÉTODO O COMO MIEMBROS DE LA CLASE) ---
                Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
                Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font fontNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                Font fontNegrita = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD); // Esta es la que NetBeans no encuentra
                Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
                
                

                // --- Encabezado General (ICARO y Factura) ---
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setSpacingAfter(20);
                headerTable.setWidths(new float[]{3f, 1f});

                Phrase icaroPhrase = new Phrase("ICARO", new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(0, 0, 139)));
                PdfPCell icaroCell = new PdfPCell(icaroPhrase);
                icaroCell.setBorder(Rectangle.NO_BORDER);
                icaroCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(icaroCell);

                Phrase facturaPhrase = new Phrase("Factura", new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(0, 0, 139)));
                PdfPCell facturaCell = new PdfPCell(facturaPhrase);
                facturaCell.setBorder(Rectangle.NO_BORDER);
                facturaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headerTable.addCell(facturaCell);

                document.add(headerTable);


                // --- Información del Cliente y Fecha (Diseño similar al de la imagen) ---
                PdfPTable infoTable = new PdfPTable(2); // Ajusta el '2' si tu tabla de información del cliente tiene más columnas
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(10f);
                infoTable.setSpacingAfter(10f);
                float[] clienteColumnWidths = {2f, 4f}; // Ajusta según tus necesidades
                infoTable.setWidths(clienteColumnWidths);

                // Fila 1: Etiquetas
                infoTable.addCell(createCell("ID CLIENTE:", fontBold, Element.ALIGN_LEFT, 1, BaseColor.LIGHT_GRAY, BaseColor.BLACK));
                infoTable.addCell(createCell("CORREO:", fontBold, Element.ALIGN_LEFT, 1, BaseColor.LIGHT_GRAY, BaseColor.BLACK));
                infoTable.addCell(createCell("FECHA", fontBold, Element.ALIGN_CENTER, 1, new BaseColor(0, 0, 139), BaseColor.WHITE));

                // Fila 2: Valores
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String fechaStr = sdf.format(fechaVenta);

                infoTable.addCell(createValueCell(idVenta, fontNormal));
                infoTable.addCell(createValueCell(clienteCorreo, fontNormal));
                infoTable.addCell(createValueCell(fechaStr, fontNormal));


                // Fila 3: CLIENTE: (Etiqueta)
                addClientInfoRow(infoTable, "CLIENTE:", facturaClienteNombre, fontNegrita, fontNormal);
                // Fila 4: Valor Cliente
                infoTable.addCell(createValueCell(clienteNombre, fontNormal, 3));

                // Fila 5: IDENTIFICACION FISCAL: (Etiqueta)
                addClientInfoRow(infoTable, "IDENTIFICACIÓN FISCAL:", facturaClienteIdentificacion, fontNegrita, fontNormal);

                // Fila 7: PAIS, TEL: (Etiquetas)
                infoTable.addCell(createCell("PAIS:", fontBold, Element.ALIGN_LEFT, 2, BaseColor.LIGHT_GRAY, BaseColor.BLACK));
                infoTable.addCell(createCell("TEL:", fontBold, Element.ALIGN_LEFT, 1, BaseColor.LIGHT_GRAY, BaseColor.BLACK));
                
                // Fila 8: Valores País y Teléfono     
                infoTable.addCell(createValueCell("Colombia", fontNormal, 2));
                if (clienteTelefono != null && !clienteTelefono.isEmpty()) {
                    infoTable.addCell(createValueCell(clienteTelefono, fontNormal, 1));
                } else {
                    infoTable.addCell(createValueCell("", fontNormal, 1));
                }

                document.add(infoTable);

                // --- Tabla de Productos Vendidos ---
                PdfPTable productTable = new PdfPTable(modeloTablaProductos.getColumnCount());
                productTable.setWidthPercentage(100);
                productTable.setSpacingBefore(20);
                productTable.setWidths(new float[]{1f, 3f, 1.5f, 1.5f});

                // Encabezados de la tabla de productos
                for (int i = 0; i < modeloTablaProductos.getColumnCount(); i++) {
                    productTable.addCell(createCell(modeloTablaProductos.getColumnName(i), fontBold, Element.ALIGN_CENTER, 1, BaseColor.LIGHT_GRAY, BaseColor.BLACK));
                }

                // Datos de la tabla de productos
                for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
                    for (int j = 0; j < modeloTablaProductos.getColumnCount(); j++) {
                        productTable.addCell(createValueCell(modeloTablaProductos.getValueAt(i, j).toString(), fontNormal));
                    }
                }
                document.add(productTable);

                // --- Total a Cobrar (Inferior derecha) ---
                PdfPTable totalTable = new PdfPTable(2);
                totalTable.setWidthPercentage(40);
                totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalTable.setSpacingBefore(10);

                totalTable.addCell(createCell("Total a cobrar:", fontBold, Element.ALIGN_LEFT, 1, BaseColor.WHITE, BaseColor.BLACK));
                totalTable.addCell(createValueCell(String.format("$,.2f", totalFactura), fontBold, Element.ALIGN_RIGHT));

                document.add(totalTable);


                // --- Información del Cajero (Inferior izquierda, sin teléfono del cajero) ---
                PdfPTable cajeroInfoTable = new PdfPTable(2);
                cajeroInfoTable.setWidthPercentage(60);
                cajeroInfoTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                cajeroInfoTable.setSpacingBefore(30);

                cajeroInfoTable.addCell(createCell("CAJERO:", fontBold, Element.ALIGN_LEFT, 1, BaseColor.WHITE, BaseColor.BLACK));
                cajeroInfoTable.addCell(createValueCell(cajeroNombre, fontNormal, 1));

                document.add(cajeroInfoTable);

                document.close();

                // 4. Abrir el PDF e imprimir
                abrirPDFEImprimir(rutaGuardado);
                JOptionPane.showMessageDialog(this, "PDF generado y enviado a la impresora.", "Impresión Exitosa", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron datos completos para la venta: " + idVenta, "Datos no encontrados", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al obtener datos de venta para impresión: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (DocumentException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage(), "Error de PDF", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado al imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void abrirPDFEImprimir(String rutaPdf) {
        try {
            File pdfFile = new File(rutaPdf);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.PRINT)) {
                        desktop.print(pdfFile);
                    } else {
                        JOptionPane.showMessageDialog(this, "La impresión directa no está soportada en este sistema.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        desktop.open(pdfFile);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "La función de escritorio no está soportada en este sistema. No se puede abrir/imprimir el PDF.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo PDF no existe en la ruta especificada: " + rutaPdf, "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir o imprimir el PDF: " + ex.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- MÉTODOS AUXILIARES DE CREACIÓN DE CELDAS PARA PDF (DEBEN ESTAR AQUÍ) ---
    
    private void addClientInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
    PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
    labelCell.setBorder(Rectangle.NO_BORDER);
    labelCell.setPadding(3);
    table.addCell(labelCell);

    PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
    valueCell.setBorder(Rectangle.BOTTOM); // Solo borde inferior para el valor
    valueCell.setPadding(3);
    table.addCell(valueCell);
}
    
    private PdfPCell createCell(String content, Font font, int alignment, BaseColor bgColor) {
    PdfPCell cell = new PdfPCell(new Phrase(content, font));
    cell.setHorizontalAlignment(alignment);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setBorder(Rectangle.NO_BORDER); // Sin bordes
    cell.setBackgroundColor(bgColor);
    cell.setPadding(5);
    return cell;
}

    private PdfPCell createValueCell(String content, Font font, int alignment) {
    PdfPCell cell = new PdfPCell(new Phrase(content, font));
    cell.setHorizontalAlignment(alignment);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setBorder(Rectangle.BOTTOM); // Solo borde inferior
    cell.setPadding(5);
    return cell;
}

    private static PdfPCell createCell(String content, Font font, int alignment, int colspan, BaseColor backgroundColor, BaseColor foregroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setColspan(colspan);
        cell.setBackgroundColor(backgroundColor);
        cell.setBorderColor(foregroundColor);
        cell.setBorderWidth(0.5f);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createValueCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidth(0.5f);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createValueCell(String content, Font font, int alignment, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderWidth(0.5f);
        cell.setPadding(5);
        return cell;
    }
    
    public factura() { 
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        BtnImprimir = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtCajero = new javax.swing.JTextField();
        txtNumCajero = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProductosFactura = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtDia = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        txtMes = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        txtAno = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtIdentificacionCliente = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtPais = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 32, 96));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        BtnImprimir.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        BtnImprimir.setText("Imprimir");
        BtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnImprimirActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("CAJERO:");

        txtCajero.setBackground(new java.awt.Color(0, 32, 96));
        txtCajero.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtCajero.setForeground(new java.awt.Color(255, 255, 255));
        txtCajero.setBorder(null);

        txtNumCajero.setBackground(new java.awt.Color(0, 32, 96));
        txtNumCajero.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtNumCajero.setForeground(new java.awt.Color(255, 255, 255));
        txtNumCajero.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(txtNumCajero, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCajero, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(BtnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel10)
                        .addGap(8, 8, 8)
                        .addComponent(txtCajero, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumCajero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 32, 96));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setText("Factura");

        jLabel2.setFont(new java.awt.Font("Algerian", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(242, 242, 242));
        jLabel2.setText("ICARO");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTableProductosFactura.setBackground(new java.awt.Color(242, 242, 242));
        jTableProductosFactura.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableProductosFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "Producto", "Valor unitario", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableProductosFactura.setGridColor(new java.awt.Color(0, 0, 0));
        jTableProductosFactura.setShowGrid(true);
        jScrollPane1.setViewportView(jTableProductosFactura);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("ID CLIENTE:");

        txtIdCliente.setBackground(new java.awt.Color(242, 242, 242));
        txtIdCliente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtIdCliente.setBorder(null);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));

        txtCorreo.setBackground(new java.awt.Color(242, 242, 242));
        txtCorreo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCorreo.setBorder(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("CORREO:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153), 2));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("FECHA");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtDia.setBackground(new java.awt.Color(242, 242, 242));
        txtDia.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtDia.setBorder(null);
        txtDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDia)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDia, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtMes.setBackground(new java.awt.Color(242, 242, 242));
        txtMes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtMes.setBorder(null);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMes)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtAno.setBackground(new java.awt.Color(242, 242, 242));
        txtAno.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        txtAno.setBorder(null);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAno, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("CLIENTE:");

        txtNombreCliente.setBackground(new java.awt.Color(242, 242, 242));
        txtNombreCliente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombreCliente.setBorder(null);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreCliente)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtNombreCliente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("IDENTIFICACION FISCAL:");

        txtIdentificacionCliente.setBackground(new java.awt.Color(242, 242, 242));
        txtIdentificacionCliente.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtIdentificacionCliente.setBorder(null);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdentificacionCliente)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtIdentificacionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setText("PAIS:");

        txtPais.setBackground(new java.awt.Color(242, 242, 242));
        txtPais.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPais.setBorder(null);
        txtPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPais, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("TEL:");

        txtTelefono.setBackground(new java.awt.Color(242, 242, 242));
        txtTelefono.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTelefono.setBorder(null);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefono)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(14, 14, 14))
                                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaisActionPerformed

    private void txtDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaActionPerformed

    private void BtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnImprimirActionPerformed
        reimprimirUltimoTicket();
    }//GEN-LAST:event_BtnImprimirActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnImprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableProductosFactura;
    private javax.swing.JTextField txtAno;
    private javax.swing.JTextField txtCajero;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDia;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdentificacionCliente;
    private javax.swing.JTextField txtMes;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNumCajero;
    private javax.swing.JTextField txtPais;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
