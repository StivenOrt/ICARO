package Ventanas;

import Conexiones.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import java.util.regex.PatternSyntaxException;

public class Inventario extends javax.swing.JFrame {
    
    private DefaultTableModel modeloTablaInventario;
    private TableRowSorter<DefaultTableModel> sorter;
    private Connection conn;
    

    public Inventario(java.awt.Frame parent, Connection conexion) {
        initComponents();
        this.conn = conexion;
        inicializarTabla();
        setupTable(); // Configura el modelo de la tabla y el sorter
        cargarInventario(); // Carga los datos iniciales de los productos
        calcularTotales(); // Calcula y muestra los totales de inversión y venta.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        pack();
        setLocationRelativeTo(null);
        setupBusqueda();
    }
    
    private void inicializarTabla() {
        modeloTablaInventario = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };
        tablaInventario.setModel(modeloTablaInventario);
        
        // Configurar las columnas de la tabla (ajusta estos nombres y orden según tu imagen)
        modeloTablaInventario.addColumn("Código");
        modeloTablaInventario.addColumn("Nombre");
        modeloTablaInventario.addColumn("Precio de venta");
        modeloTablaInventario.addColumn("Cantidad"); // Esto es Stock en tu BD, pero "Cantidad" en la UI
        modeloTablaInventario.addColumn("Marca");
        modeloTablaInventario.addColumn("Descuento");
        modeloTablaInventario.addColumn("Descripción");
        // Asegúrate de que la cantidad de columnas aquí coincida con la cantidad de campos que lees en cargarInventario()

        // Inicializar el TableRowSorter
        sorter = new TableRowSorter<>(modeloTablaInventario);
        tablaInventario.setRowSorter(sorter);
    }
    
    private void setupBusqueda() {
        // Listener para el campo de texto de búsqueda (filtro en tiempo real)
        if (txtBuscar != null) { // Asegurarse de que txtBuscar esté inicializado
            txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    aplicarFiltro();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    aplicarFiltro();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    // No se usa para JTextFields planos
                }
            });
        }
    }
    
    private void aplicarFiltro() {
        String textoBusqueda = txtBuscar.getText().trim();
        if (textoBusqueda.isEmpty()) {
            sorter.setRowFilter(null); // Si el campo está vacío, no hay filtro (mostrar todo)
        } else {
            try {
                // RowFilter.regexFilter(regex, columna1, columna2, ...)
                // Esto busca el texto en todas las columnas (se puede especificar)
                // "(?i)" para que sea case-insensitive (ignorando mayúsculas/minúsculas)
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textoBusqueda));
            } catch (PatternSyntaxException e) {
                // Manejar errores si la expresión regular es inválida 
                System.err.println("Error en la expresión regular de búsqueda: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Carácter de búsqueda inválido. Por favor, revisa tu entrada.", "Error de Búsqueda", JOptionPane.WARNING_MESSAGE);
                sorter.setRowFilter(null); // Borrar el filtro en caso de error
            }
        }
    }
    
    public void cargarInventario() {
        modeloTablaInventario.setRowCount(0); // Limpiar tabla antes de cargar
        
        Connection currentConnection = Conexion.conectar();
        if (currentConnection == null) {
            JOptionPane.showMessageDialog(this, "No hay conexión a la base de datos.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT IdProducto, Nombre, Precio, Stock, Marca, Descripcion, PrecioCompra, Descuento, Activo FROM producto WHERE Activo = TRUE";
            pstmt = currentConnection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[7]; // Ajusta el tamaño a 7 columnas según tu UI (Código, Nombre, Precio de ventas, Cantidad, Marca, Descuento, Descripción)
                fila[0] = rs.getString("IdProducto");
                fila[1] = rs.getString("Nombre");
                fila[2] = rs.getDouble("Precio"); // Precio de venta
                fila[3] = rs.getInt("Stock");    // Cantidad
                fila[4] = rs.getString("Marca");
                fila[5] = rs.getDouble("Descuento");
                fila[6] = rs.getString("Descripcion");
                
                modeloTablaInventario.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage(), "Error de SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        calcularTotales(); // Vuelve a calcular los totales después de cargar
    }
    
    private void setupTable() {
        modeloTablaInventario = (DefaultTableModel) tablaInventario.getModel();
        modeloTablaInventario.setColumnCount(0);
        modeloTablaInventario.addColumn("Código"); // IdProducto
        modeloTablaInventario.addColumn("Nombre");
        modeloTablaInventario.addColumn("Precio de ventas"); // Precio
        modeloTablaInventario.addColumn("Cantidad"); // Stock
        modeloTablaInventario.addColumn("Marca");
        modeloTablaInventario.addColumn("Descuento"); // Si tienes un campo de descuento en producto
        modeloTablaInventario.addColumn("Descripción");

        // Configuramos el sorter con el modelo que acabamos de obtener/configurar.
        sorter = new TableRowSorter<>(modeloTablaInventario);
        tablaInventario.setRowSorter(sorter);
    }
    
    
    
    private void modificarProducto() {
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProducto = modeloTablaInventario.getValueAt(filaSeleccionada, 0).toString(); 
    }
    
    private void eliminarProducto() {
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProducto = modeloTablaInventario.getValueAt(filaSeleccionada, 0).toString(); 

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar el producto con Código: " + idProducto + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            Connection currentConnection = Conexion.conectar();
            if (currentConnection == null) {
                JOptionPane.showMessageDialog(this, "No hay conexión a la base de datos para eliminar el producto.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "UPDATE producto SET Activo = FALSE WHERE IdProducto = ?";
            
            PreparedStatement pstmt = null;
            try { 
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, idProducto); 

        int filasAfectadas = pstmt.executeUpdate();
        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Producto marcado como inactivo exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el producto o no se pudo marcar como inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al marcar el producto como inactivo: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        }
    }
    
    private void agregarMercancia() {
        JOptionPane.showMessageDialog(this, "Funcionalidad para 'Agregar mercancía' se manejará en FacturaInventario.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void ordenarInventario(String tipoOrden) {
        int columnaCantidad = 3; 

        if (tipoOrden.equals("MayorExistencias")) {
            sorter.setSortKeys(List.of(new RowSorter.SortKey(columnaCantidad, SortOrder.DESCENDING)));
        } else if (tipoOrden.equals("MenorExistencias")) {
            sorter.setSortKeys(List.of(new RowSorter.SortKey(columnaCantidad, SortOrder.ASCENDING)));
        }
        sorter.sort(); 
    }
    
    private void calcularTotales() {
        double totalInversion = 0.0;
        double totalVenta = 0.0;

        Connection currentConnection = Conexion.conectar(); // Obtiene la conexión
        if (currentConnection == null) {
            JOptionPane.showMessageDialog(this, "No hay conexión a la base de datos para calcular totales.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            txtDineroTotalInversion.setText("N/A"); // Actualiza tu txtDineroTotalInversion
            txtDineroTotalVenta.setText("N/A"); // Actualiza tu txtDineroTotalVenta
            return;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // ¡IMPORTANTE! La query ahora selecciona Precio y PrecioCompra
            String sql = "SELECT Precio, PrecioCompra, Stock FROM producto"; 
            pstmt = currentConnection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                double precioVenta = rs.getDouble("Precio");
                double precioCompra = rs.getDouble("PrecioCompra"); // Obtiene el precio de compra
                int stock = rs.getInt("Stock");

                totalVenta += (precioVenta * stock);
                totalInversion += (precioCompra * stock); // Calcula la inversión con PrecioCompra
            }

        } catch (SQLException e) {
            System.err.println("Error al calcular totales de inventario: " + e.getMessage());
            totalInversion = 0.0;
            totalVenta = 0.0;
            // Asegúrate de que los JTextFields reflejen un error o cero si hay un problema
            txtDineroTotalInversion.setText("Error");
            txtDineroTotalVenta.setText("Error");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Formatear los valores a moneda y mostrarlos en los JTextFields
        txtDineroTotalInversion.setText(String.format("%,.2f", totalInversion)); 
        txtDineroTotalVenta.setText(String.format("%,.2f", totalVenta)); 
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnActualizarTabla = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnAgregarProducto = new javax.swing.JButton();
        btnModificarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel5 = new javax.swing.JPanel();
        btnOrdenarMayorExistencias = new javax.swing.JButton();
        btnOrdenarMenorExistencias = new javax.swing.JButton();
        btnGenerarReporte = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDineroTotalInversion = new javax.swing.JTextField();
        txtDineroTotalVenta = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("INVENTARIO");
        jLabel1.setPreferredSize(new java.awt.Dimension(203, 50));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));

        txtBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 2));
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(298, 298, 298))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre", "Precio de ventas", "Cantidad", "Marca", "Descuento", "Descripción"
            }
        ));
        jScrollPane1.setViewportView(tablaInventario);

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        btnActualizarTabla.setBackground(new java.awt.Color(0, 0, 102));
        btnActualizarTabla.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarTabla.setText("Actualizar tabla");
        btnActualizarTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        btnActualizarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarTablaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(309, 309, 309))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnActualizarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnAgregarProducto.setBackground(new java.awt.Color(0, 0, 51));
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar un producto");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnModificarProducto.setBackground(new java.awt.Color(0, 0, 51));
        btnModificarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarProducto.setText("Modificar un producto");
        btnModificarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setBackground(new java.awt.Color(0, 0, 51));
        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setText("Eliminar un producto");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(btnModificarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto)
                    .addComponent(btnModificarProducto)
                    .addComponent(btnEliminarProducto))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 0, 51));

        btnOrdenarMayorExistencias.setBackground(new java.awt.Color(0, 0, 51));
        btnOrdenarMayorExistencias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdenarMayorExistencias.setForeground(new java.awt.Color(255, 255, 255));
        btnOrdenarMayorExistencias.setText("Ordenar mayor existencias");
        btnOrdenarMayorExistencias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        btnOrdenarMayorExistencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdenarMayorExistenciasActionPerformed(evt);
            }
        });

        btnOrdenarMenorExistencias.setBackground(new java.awt.Color(0, 0, 51));
        btnOrdenarMenorExistencias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnOrdenarMenorExistencias.setForeground(new java.awt.Color(255, 255, 255));
        btnOrdenarMenorExistencias.setText("Ordenar menor existencias");
        btnOrdenarMenorExistencias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        btnOrdenarMenorExistencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdenarMenorExistenciasActionPerformed(evt);
            }
        });

        btnGenerarReporte.setBackground(new java.awt.Color(0, 0, 51));
        btnGenerarReporte.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGenerarReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarReporte.setText("Generar reporte de inventario");
        btnGenerarReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Dinero total en inventario (Inversión):");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dinero total en inventario (a la venta):");

        txtDineroTotalInversion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 2));

        txtDineroTotalVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 2));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGenerarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnOrdenarMayorExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOrdenarMenorExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDineroTotalInversion))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDineroTotalVenta)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOrdenarMayorExistencias)
                    .addComponent(btnOrdenarMenorExistencias)
                    .addComponent(jLabel3)
                    .addComponent(txtDineroTotalInversion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerarReporte)
                    .addComponent(jLabel4)
                    .addComponent(txtDineroTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 863, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGap(339, 339, 339)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(280, 280, 280)))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnActualizarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarTablaActionPerformed
        cargarInventario();
    }//GEN-LAST:event_btnActualizarTablaActionPerformed

    private void btnModificarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarProductoActionPerformed
        int filaSeleccionada = tablaInventario.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }
    String idProducto = tablaInventario.getValueAt(tablaInventario.convertRowIndexToModel(filaSeleccionada), 0).toString(); 
    Modificar modificarProductoFrame = new Modificar(this, this, idProducto);
    modificarProductoFrame.setVisible(true);
    }//GEN-LAST:event_btnModificarProductoActionPerformed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        GenerarReportes reporteFrame = new GenerarReportes(this, conn, "inventario", null);
        reporteFrame.setVisible(true);
    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        new NuevoProducto().setVisible(true);
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        eliminarProducto();
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnOrdenarMayorExistenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenarMayorExistenciasActionPerformed
        ordenarInventario("MayorExistencias");
    }//GEN-LAST:event_btnOrdenarMayorExistenciasActionPerformed

    private void btnOrdenarMenorExistenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenarMenorExistenciasActionPerformed
        ordenarInventario("MenorExistencias");
    }//GEN-LAST:event_btnOrdenarMenorExistenciasActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarTabla;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JButton btnModificarProducto;
    private javax.swing.JButton btnOrdenarMayorExistencias;
    private javax.swing.JButton btnOrdenarMenorExistencias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDineroTotalInversion;
    private javax.swing.JTextField txtDineroTotalVenta;
    // End of variables declaration//GEN-END:variables
}
