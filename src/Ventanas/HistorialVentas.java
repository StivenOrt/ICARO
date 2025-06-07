package Ventanas;

import Ventanas.GenerarReportes;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date; 
import java.text.SimpleDateFormat; 
import java.text.ParseException; 
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class HistorialVentas extends javax.swing.JFrame {
    
    private DefaultTableModel modeloTablaVentas;
    private DefaultTableModel modeloTablaDetalleVentas;
    private Connection conn;
    private TableRowSorter<DefaultTableModel> sorter;
    private DefaultTableModel modeloVentas; 
    private DefaultTableModel modeloProductosVendidos; 
    private TableRowSorter<DefaultTableModel> sorterVentas;

    public HistorialVentas() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public HistorialVentas(Connection conexion) {
        this.conn = conexion;
        initComponents();
        
        if (txtTotalEnVentas != null) {
            txtTotalEnVentas.setEditable(false);
        } else {
            System.err.println("Advertencia: txtTotalEnVentas es null. No se pudo configurar como no editable.");
        }
        
        String[] columnNamesVentas = {
    "Número de compra", "ID Cliente", "Cajero en Turno", "Fecha", "Total de Venta"
};

        this.modeloVentas = new DefaultTableModel(columnNamesVentas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hace todas las celdas de 'tablaVentas' no editables
    }
};
        this.tablaVentas.setModel(this.modeloVentas);

        this.sorterVentas = new TableRowSorter<>(this.modeloVentas);
        this.tablaVentas.setRowSorter(this.sorterVentas);
        
        String[] columnNamesProductosVendidos = {
        "ID Venta", "Código Producto", "Nombre Producto", "Cantidad", "Precio Unitario", "Subtotal", "Fecha Venta"
    };

    this.modeloProductosVendidos = new DefaultTableModel(columnNamesProductosVendidos, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Hace todas las celdas de 'tablaProductosVendidos' no editables
    }
};
this.tablaProductosVendidos.setModel(this.modeloProductosVendidos);
        
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modeloTablaVentas = (DefaultTableModel) tablaVentas.getModel();
        modeloTablaDetalleVentas = (DefaultTableModel) tablaProductosVendidos.getModel();
        
        sorter = new TableRowSorter<>(modeloTablaVentas);
        tablaVentas.setRowSorter(sorter);
        
        setupRealtimeSearch();

        configurarColumnasTablas();

        cargarTodasLasVentas();

        tablaVentas.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tablaVentas.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) { 
                    cargarDetalleVentasSeleccionadas();
                }
            }
        });
    }
    
    private void setupRealtimeSearch() {
    txtBusquedaVenta.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    });
}
    
    private void filterTable() {
    String text = txtBusquedaVenta.getText().trim(); 

    if (text.length() == 0) {
        sorter.setRowFilter(null); 
    } else {
        try {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0)); // (?i) para ignorar mayúsculas/minúsculas
        } catch (java.util.regex.PatternSyntaxException e) {
            System.err.println("Error en el patrón de búsqueda: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Patrón de búsqueda inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private void configurarColumnasTablas() {
        // Tabla de Ventas (Superior)
        modeloTablaVentas.setColumnCount(0); // Limpia columnas existentes
        modeloTablaVentas.addColumn("Número de compra");
        modeloTablaVentas.addColumn("ID Cliente"); // Corregido: Es ID Cliente, no Producto
        modeloTablaVentas.addColumn("Cajero en Turno"); // O ID de Cajero
        modeloTablaVentas.addColumn("Fecha");
        modeloTablaVentas.addColumn("Total de Venta");

        // Tabla de Productos Vendidos (Inferior)
        modeloTablaDetalleVentas.setColumnCount(0); // Limpia columnas existentes
        modeloTablaDetalleVentas.addColumn("ID Venta"); // Num_Venta de la imagen
        modeloTablaDetalleVentas.addColumn("Código Producto"); // Clase_Venta de la imagen (podría ser ID Producto)
        modeloTablaDetalleVentas.addColumn("Nombre Producto");
        modeloTablaDetalleVentas.addColumn("Cantidad");
        modeloTablaDetalleVentas.addColumn("Precio Unitario"); // Nuevo: Útil para factura
        modeloTablaDetalleVentas.addColumn("Subtotal"); // Nuevo: Útil para factura
        modeloTablaDetalleVentas.addColumn("Fecha Venta"); // Fecha de la venta
    }
    
    private void cargarTodasLasVentas() {
    // Limpiar tabla antes de cargar nuevos datos
    modeloTablaVentas.setRowCount(0);
    txtTotalEnVentas.setText(""); // Limpiar el total también
    modeloTablaDetalleVentas.setRowCount(0); // Limpiar la tabla de detalles

    String sql = "SELECT v.IdVenta, v.IdCliente, u.Nombre AS NombreCajero, v.Fecha, v.Total " +
                 "FROM venta v " +
                 "JOIN usuario u ON v.IdUsuario = u.IdUsuario " +
                 "ORDER BY v.Fecha DESC"; // Las más recientes primero

    double totalGeneralDeVentas = 0.0;

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int idVenta = rs.getInt("IdVenta");
            String idCliente = rs.getString("IdCliente");
            String nombreCajero = rs.getString("NombreCajero");
            Timestamp fechaVenta = rs.getTimestamp("Fecha");
            double totalVenta = rs.getDouble("Total");

            // Formatear la fecha para mostrarla amigable
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String fechaFormateada = sdf.format(new Date(fechaVenta.getTime()));

            modeloTablaVentas.addRow(new Object[]{
                idVenta,
                idCliente,
                nombreCajero,
                fechaFormateada,
                String.format("%.2f", totalVenta) // Formatear el total a 2 decimales
            });

            totalGeneralDeVentas += totalVenta;
        }

        txtTotalEnVentas.setText(String.format("%.2f", totalGeneralDeVentas));

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar las ventas: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    
    private void cargarDetalleVentasSeleccionadas() {
    modeloTablaDetalleVentas.setRowCount(0); // Limpiar la tabla de detalles antes de cargar nuevos

    int[] selectedRows = tablaVentas.getSelectedRows(); // Obtener todas las filas seleccionadas

    if (selectedRows.length == 0) {
        // No hay ventas seleccionadas, limpiar la tabla de detalles
        return;
    }

    // Construir la cláusula IN para la consulta SQL (ej. WHERE IdVenta IN (1, 5, 8))
    StringBuilder idVentasInClause = new StringBuilder();
    for (int i = 0; i < selectedRows.length; i++) {
        // Obtener el ID de Venta de la columna "Número de compra" de la tabla superior
        int idVenta = (int) modeloTablaVentas.getValueAt(selectedRows[i], 0); // La columna 0 es "Número de compra" (IdVenta)
        idVentasInClause.append(idVenta);
        if (i < selectedRows.length - 1) {
            idVentasInClause.append(",");
        }
    }

    String sql = "SELECT dv.IdVenta, p.IdProducto, p.Nombre AS NombreProducto, dv.Cantidad, dv.PrecioUnitario, dv.Subtotal, v.Fecha " + // <-- ¡CAMBIO AQUÍ! de p.CodigoProducto a p.IdProducto
             "FROM detalleventa dv " +
             "JOIN producto p ON dv.IdProducto = p.IdProducto " +
             "JOIN venta v ON dv.IdVenta = v.IdVenta " +
             "WHERE dv.IdVenta IN (" + idVentasInClause.toString() + ") " +
             "ORDER BY dv.IdVenta, p.Nombre";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {

    while (rs.next()) {
        int idVenta = rs.getInt("IdVenta");
        // String codigoProducto = rs.getString("CodigoProducto"); // <--- Esta línea también cambiará
        String idProductoObtenido = rs.getString("IdProducto"); // <--- AHORA LEES EL IdProducto
        String nombreProducto = rs.getString("NombreProducto");
        int cantidad = rs.getInt("Cantidad");
        double precioUnitario = rs.getDouble("PrecioUnitario");
        double subtotal = rs.getDouble("Subtotal");
        Timestamp fechaVenta = rs.getTimestamp("Fecha");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
        String fechaFormateada = sdf.format(new Date(fechaVenta.getTime()));

        modeloTablaDetalleVentas.addRow(new Object[]{
            idVenta,
            idProductoObtenido, // <--- Aquí pasas el IdProducto
            nombreProducto,
            cantidad,
            String.format("%.2f", precioUnitario),
            String.format("%.2f", subtotal),
            fechaFormateada
        });
    }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar los detalles de las ventas: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    
    private void aplicarFiltros() {
    modeloTablaVentas.setRowCount(0); // Limpiar tabla antes de cargar nuevos datos
    txtTotalEnVentas.setText("");
    modeloTablaDetalleVentas.setRowCount(0); // Limpiar detalles

    StringBuilder sql = new StringBuilder("SELECT v.IdVenta, v.IdCliente, u.Nombre AS NombreCajero, v.Fecha, v.Total " +
                                           "FROM venta v " +
                                           "JOIN usuario u ON v.IdUsuario = u.IdUsuario " +
                                           "WHERE 1=1 "); // Cláusula base para añadir filtros

    // Parámetros para el PreparedStatement
    java.util.List<Object> parametros = new java.util.ArrayList<>();
    double totalGeneralDeVentas = 0.0;

    // Filtro por número de venta
    String idVentaBusqueda = txtBusquedaVenta.getText().trim();
    if (!idVentaBusqueda.isEmpty()) {
        try {
            int idVenta = Integer.parseInt(idVentaBusqueda);
            sql.append(" AND v.IdVenta = ?");
            parametros.add(idVenta);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número de venta válido.", "Entrada Inválida", JOptionPane.WARNING_MESSAGE);
            cargarTodasLasVentas(); // Recargar todas las ventas si el ID es inválido
            return;
        }
    }

    // Filtro por rango de fechas
    String fechaInicioStr = txtFechaInicio.getText().trim();
    String fechaFinStr = txtFechaFin.getText().trim();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); 
    sdf.setLenient(false); 

    try {
        if (!fechaInicioStr.isEmpty()) {
            Date fechaInicio = sdf.parse(fechaInicioStr);
            sql.append(" AND v.Fecha >= ?");
            parametros.add(new Timestamp(fechaInicio.getTime()));
        }
        if (!fechaFinStr.isEmpty()) {
            Date fechaFin = sdf.parse(fechaFinStr);
            // Para incluir todo el día de la fecha fin, sumamos un día y restamos un milisegundo
            Date fechaFinMasUnDia = new Date(fechaFin.getTime() + (1000 * 60 * 60 * 24) - 1);
            sql.append(" AND v.Fecha <= ?");
            parametros.add(new Timestamp(fechaFinMasUnDia.getTime()));
        }
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese fechas en formato DD/MM/AA.", "Formato de Fecha Inválido", JOptionPane.WARNING_MESSAGE);
        cargarTodasLasVentas(); // Recargar todas las ventas si la fecha es inválida
        return;
    }

    sql.append(" ORDER BY v.Fecha DESC");

    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
        for (int i = 0; i < parametros.size(); i++) {
            if (parametros.get(i) instanceof Integer) {
                pstmt.setInt(i + 1, (int) parametros.get(i));
            } else if (parametros.get(i) instanceof Timestamp) {
                pstmt.setTimestamp(i + 1, (Timestamp) parametros.get(i));
            }
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idVenta = rs.getInt("IdVenta");
                String idCliente = rs.getString("IdCliente");
                String nombreCajero = rs.getString("NombreCajero");
                Timestamp fechaVenta = rs.getTimestamp("Fecha");
                double totalVenta = rs.getDouble("Total");

                SimpleDateFormat displaySDF = new SimpleDateFormat("dd/MM/yy HH:mm");
                String fechaFormateada = displaySDF.format(new Date(fechaVenta.getTime()));

                modeloTablaVentas.addRow(new Object[]{
                    idVenta,
                    idCliente,
                    nombreCajero,
                    fechaFormateada,
                    String.format("%.2f", totalVenta)
                });
                totalGeneralDeVentas += totalVenta;
            }
            txtTotalEnVentas.setText(String.format("%.2f", totalGeneralDeVentas));
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al filtrar las ventas: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtBusquedaVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFechaFin = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductosVendidos = new javax.swing.JTable();
        txtTotalEnVentas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnGenerarReporte = new javax.swing.JButton();
        btnActualizarDatos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("HISTORIAL DE VENTAS TOTALES");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(140, 140, 140))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Busqueda por no. de venta:");

        txtBusquedaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaVentaActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Busqueda por fecha del:");

        txtFechaInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaInicioActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Al:");

        txtFechaFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaFinActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(0, 0, 51));
        btnBuscar.setText("Filtrar");
        btnBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 255), 3));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtBusquedaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBusquedaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Ventas del:");

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Número de compra", "Producto", "Cajero_En_Turno", "Fecha", "Total_De_Venta"
            }
        ));
        jScrollPane1.setViewportView(tablaVentas);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Productos vendidos:");

        tablaProductosVendidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id_Producto", "Num_Venta", "Cantidad", "Nombre_Producto", "Fecha"
            }
        ));
        jScrollPane2.setViewportView(tablaProductosVendidos);

        txtTotalEnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalEnVentasActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total en ventas:");

        btnGenerarReporte.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGenerarReporte.setForeground(new java.awt.Color(0, 0, 51));
        btnGenerarReporte.setText("Generar reportes de los datos actuales");
        btnGenerarReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 3));
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });

        btnActualizarDatos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnActualizarDatos.setForeground(new java.awt.Color(0, 0, 51));
        btnActualizarDatos.setText("Actualizar datos");
        btnActualizarDatos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 255), 3));
        btnActualizarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jLabel5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalEnVentas))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnGenerarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(259, 259, 259))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnActualizarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(323, 323, 323))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalEnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnGenerarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaVentaActionPerformed

    private void txtTotalEnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalEnVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalEnVentasActionPerformed

    private void btnActualizarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosActionPerformed
        cargarTodasLasVentas(); // Recargar todas las ventas
        JOptionPane.showMessageDialog(this, "Datos de ventas actualizados.", "Actualización Completa", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnActualizarDatosActionPerformed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        if (modeloTablaVentas.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No hay datos de ventas para generar un reporte.", "Sin Datos", JOptionPane.WARNING_MESSAGE);
        return;
    }
    GenerarReportes reporteFrame = new GenerarReportes(this, conn, "ventas", modeloTablaVentas);
    reporteFrame.setVisible(true);
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        aplicarFiltros();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtFechaInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaInicioActionPerformed
        txtFechaInicio.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                aplicarFiltros(); 
            }
        }
    });
    }//GEN-LAST:event_txtFechaInicioActionPerformed

    private void txtFechaFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaFinActionPerformed
        txtFechaFin.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                aplicarFiltros();
            }
        }
    });
    }//GEN-LAST:event_txtFechaFinActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HistorialVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarDatos;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaProductosVendidos;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField txtBusquedaVenta;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    private javax.swing.JTextField txtTotalEnVentas;
    // End of variables declaration//GEN-END:variables
}
