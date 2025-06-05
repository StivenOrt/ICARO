package Ventanas;

import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel; // Importar DefaultTableModel
import java.sql.Connection; // Importar Connection
import java.sql.PreparedStatement; // Importar PreparedStatement
import java.sql.ResultSet; // Importar ResultSet
import java.sql.SQLException; // Importar SQLException
import java.sql.Timestamp; // Importar Timestamp
import java.util.Date; // Importar Date

public class Interfazcobro extends javax.swing.JFrame {
    
   private double totalVentaActual;
    private int idCajeroActual; // Añadido
    private Connection conn; // Añadido: Para la conexión a la base de datos
    private DefaultTableModel tablaProductosModel; // Añadido: Para los productos en el carrito
    private String nombreCajeroActual;
    
    private int idVentaExitosa = -1; // -1 indica que no se ha registrado una venta aún
    private String idClienteExitosa = "N/A"; // "N/A" por defecto
    
    public Interfazcobro(double totalAPagar, int idCajero, String nombreCajero, Connection conexion, DefaultTableModel productosModel) {
        initComponents(); // Inicializa los componentes de la UI
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana

        // Asigna los valores recibidos a los atributos de la clase
        this.totalVentaActual = totalAPagar;
        this.idCajeroActual = idCajero;
        this.conn = conexion; // Asigna la conexión pasada desde VentanaPrincipal
        this.tablaProductosModel = productosModel;
        this.nombreCajeroActual = nombreCajero;

        // Configura los campos de texto
        txtTotalAPagar.setText(String.format("%.2f", totalAPagar));
        txtTotalAPagar.setEditable(false); // No permite editar el total
        txtCambio.setEditable(false);      // No permite editar el cambio

        // Enfoca el campo de entrada de pago
        txtPagoCon.requestFocusInWindow();

        // Añade listeners para calcular el cambio automáticamente
        txtPagoCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcularCambio();
            }
        });
        txtPagoCon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularCambio();
            }
        });
    }
    
    public Interfazcobro() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.totalVentaActual = 0.0;
        this.idCajeroActual = -1;
        this.nombreCajeroActual = "Desconocido";
        this.conn = null; // O establece una conexión de prueba
        this.tablaProductosModel = new DefaultTableModel(); // O un modelo vacío
    }
    
    private void calcularCambio() {
        try {
            double pagoCliente = Double.parseDouble(txtPagoCon.getText());
            double cambio = pagoCliente - totalVentaActual;
            txtCambio.setText(String.format("%.2f", cambio));
        } catch (NumberFormatException e) {
            txtCambio.setText("Inválido");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        canvas1 = new java.awt.Canvas();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtPagoCon = new java.awt.TextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTotalAPagar = new javax.swing.JTextField();
        txtCambio = new java.awt.TextField();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(63, 154, 255), 3));

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jButton1.setBackground(new java.awt.Color(217, 217, 217));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Pagar a credito");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(217, 217, 217));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Pagar con tarjeta");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(217, 217, 217));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Cobrar");
        jButton3.setPreferredSize(new java.awt.Dimension(100, 23));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(217, 217, 217));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("Cancelar");
        jButton4.setMaximumSize(new java.awt.Dimension(100, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(100, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(100, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(217, 217, 217));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setText("Crear Factura");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(217, 217, 217));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setText("Otra compra");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Total a cobrar:");

        txtPagoCon.setBackground(new java.awt.Color(204, 204, 204));
        txtPagoCon.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtPagoCon.setName(""); // NOI18N
        txtPagoCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagoConActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("$");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Cambio: ");

        txtTotalAPagar.setBackground(new java.awt.Color(228, 228, 228));
        txtTotalAPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAPagarActionPerformed(evt);
            }
        });

        txtCambio.setBackground(new java.awt.Color(204, 204, 204));
        txtCambio.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtCambio.setName(""); // NOI18N
        txtCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCambioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(124, 124, 124))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(24, 24, 24))
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTotalAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPagoCon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPagoCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPagoConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagoConActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagoConActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                            "Desea descartar la venta actual y comenzar una nueva compra?",
                            "Confirmar Nueva Compra",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this,
                            "Venta actual descartada. Se ha reiniciado el carrito en la ventana principal.",
                            "Nueva Compra Iniciada",
                            JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); // Cierra la ventana de Interfazcobro
        // El carrito en VentanaPrincipal se limpiará automáticamente
        // debido al WindowListener que agregamos al cobroFrame.
    }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idVentaGenerado = -1; // Usaremos INT porque en la DB es INT AUTO_INCREMENT

        try {
            double pagoCliente = Double.parseDouble(txtPagoCon.getText());
            if (pagoCliente < totalVentaActual) {
                JOptionPane.showMessageDialog(this, "El pago es insuficiente. Faltan: " + String.format("%.2f", (totalVentaActual - pagoCliente)), "Pago Insuficiente", JOptionPane.WARNING_MESSAGE);
                return; // No proceder si el pago es insuficiente
            }

            conn.setAutoCommit(false); // Deshabilita el auto-commit

            // 1. Obtener o Registrar el IdCliente
            String idClienteParaVenta;
            String nombreCliente = JOptionPane.showInputDialog(this, "Ingrese el nombre del cliente (dejar vacío para cliente genérico):");

            if (nombreCliente == null || nombreCliente.trim().isEmpty()) {

                idClienteParaVenta = "CLIENTE_GENERICO";
            } else {
                // Opción B: Buscar cliente por nombre, si no existe, registrarlo
                String selectClienteSQL = "SELECT IdCliente FROM cliente WHERE Nombre = ?";
                pstmt = conn.prepareStatement(selectClienteSQL);
                pstmt.setString(1, nombreCliente.trim());
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Cliente existente, obtener su IdCliente
                    idClienteParaVenta = rs.getString("IdCliente");
                } else {
                    // Cliente no existe, REGISTRARLO
                    // Genera un nuevo IdCliente (Ejemplo: con un prefijo y timestamp para asegurar unicidad)
                    idClienteParaVenta = "CLI_" + System.currentTimeMillis();
                    String insertClienteSQL = "INSERT INTO cliente (IdCliente, Nombre) VALUES (?, ?)";
                    PreparedStatement pstmtInsertCliente = conn.prepareStatement(insertClienteSQL);
                    pstmtInsertCliente.setString(1, idClienteParaVenta);
                    pstmtInsertCliente.setString(2, nombreCliente.trim());
                    pstmtInsertCliente.executeUpdate();
                    pstmtInsertCliente.close(); // Cerrar el PreparedStatement específico para insertar cliente
                    JOptionPane.showMessageDialog(this, "Nuevo cliente '" + nombreCliente + "' registrado con ID: " + idClienteParaVenta, "Cliente Registrado", JOptionPane.INFORMATION_MESSAGE);
                }
                rs.close();
                pstmt.close(); // Cerrar el PreparedStatement de selección de cliente
            }

            // 2. Insertar la venta en la tabla 'venta'
            String insertVentaSQL = "INSERT INTO venta (Fecha, IdCliente, Total, IdUsuario) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertVentaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime())); // Fecha y hora actuales
            pstmt.setString(2, idClienteParaVenta); // Usamos el IdCliente obtenido/registrado
            pstmt.setDouble(3, totalVentaActual);
            pstmt.setInt(4, idCajeroActual);
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idVentaGenerado = rs.getInt(1); // Obtener el ID de la venta generada
                this.idVentaExitosa = idVentaGenerado;
            } else {
                throw new SQLException("No se pudo obtener el IdVenta generado después de insertar la venta.");
            }
            rs.close();
            pstmt.close(); // Cerrar el PreparedStatement de venta

            // 3. Insertar los detalles de la venta en la tabla 'detalleventa'
            String insertDetalleSQL = "INSERT INTO detalleventa (IdVenta, IdProducto, Cantidad, PrecioUnitario, Subtotal) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertDetalleSQL);

            for (int i = 0; i < tablaProductosModel.getRowCount(); i++) {

                String idProducto = tablaProductosModel.getValueAt(i, 1).toString(); // "Código"
                int cantidad = Integer.parseInt(tablaProductosModel.getValueAt(i, 3).toString()); // "Cantidad"
                double precioUnitario = Double.parseDouble(tablaProductosModel.getValueAt(i, 4).toString()); // "Precio unitario"
                double subtotal = Double.parseDouble(tablaProductosModel.getValueAt(i, 5).toString()); // "Subtotal"

                pstmt.setInt(1, idVentaGenerado);
                pstmt.setString(2, idProducto); // IdProducto es VARCHAR(50)
                pstmt.setInt(3, cantidad);
                pstmt.setDouble(4, precioUnitario);
                pstmt.setDouble(5, subtotal);
                pstmt.addBatch(); // Añadir al lote
            }
            pstmt.executeBatch(); // Ejecutar todas las inserciones de detalles
            pstmt.close(); // Cerrar el PreparedStatement de detalle de venta

            // 4. Actualizar el stock de productos
            String updateStockSQL = "UPDATE producto SET Stock = Stock - ? WHERE IdProducto = ?";
            pstmt = conn.prepareStatement(updateStockSQL);
            for (int i = 0; i < tablaProductosModel.getRowCount(); i++) {
                String idProducto = tablaProductosModel.getValueAt(i, 1).toString();
                int cantidadVendida = Integer.parseInt(tablaProductosModel.getValueAt(i, 3).toString());
                pstmt.setInt(1, cantidadVendida);
                pstmt.setString(2, idProducto);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close(); // Cerrar el PreparedStatement de actualización de stock

            JOptionPane.showMessageDialog(this, "Venta registrada exitosamente. ID de Venta: " + idVentaGenerado + ". Cambio: " + txtCambio.getText(), "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            // Si ocurre algún error en la base de datos, intentar revertir la transacción
            try {
                if (conn != null) {
                    conn.rollback(); // Deshacer todos los cambios de la transacción
                    JOptionPane.showMessageDialog(this, "Error al registrar la venta. La transacción fue revertida: " + ex.getMessage(), "Error de Transacción", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error al realizar rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error en Interfazcobro al registrar la venta: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un valor numérico válido para el pago.", "Pago Inválido", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Asegurarse de cerrar los PreparedStatements y restaurar el auto-commit
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true); // Restaurar el modo auto-commit de la conexión
                // ¡IMPORTANTE!: No cierres la conexión 'conn' aquí, porque la recibiste de VentanaPrincipal
                // y es probable que VentanaPrincipal la necesite para otras operaciones.
                // La conexión debe cerrarse al finalizar la aplicación.
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar recursos en Interfazcobro: " + closeEx.getMessage());
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtTotalAPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalAPagarActionPerformed

    private void txtCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCambioActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Muestra un mensaje de confirmación antes de cancelar
    int confirm = JOptionPane.showConfirmDialog(this,
                            "Esta seguro que desea cancelar el cobro? La venta no será registrada.",
                            "Confirmar Cancelación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        this.dispose(); // Cierra la ventana de Interfazcobro
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (idVentaExitosa == -1) {
        JOptionPane.showMessageDialog(this, "Debe registrar una venta exitosa antes de crear una factura.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Definir el nombre del archivo de la factura
    // Usaremos el ID de la venta y un timestamp para asegurar unicidad
    String rutaEscritorio = System.getProperty("user.home") + "/Desktop/";
    String rutaFacturas = rutaEscritorio + "Facturas_Ventas/";
    java.io.File directorioFacturas = new java.io.File(rutaFacturas);
    if (!directorioFacturas.exists()) {
        directorioFacturas.mkdirs(); // Crea el directorio si no existe
    }

    String nombreArchivo = rutaFacturas + "Factura_Venta_" + idVentaExitosa + "_" + System.currentTimeMillis() + ".txt";

    try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(nombreArchivo))) {
        writer.println("--- FACTURA DE VENTA ---");
        writer.println("Fecha: " + new Date()); // Fecha actual
        writer.println("Cajero en turno: " + nombreCajeroActual); // Nombre del cajero

        writer.println("\n-------------------------");
        writer.println("ID Venta: " + idVentaExitosa);
        writer.println("ID Cliente: " + idClienteExitosa);
        writer.println("-------------------------");
        writer.println("PRODUCTOS:");
        writer.println("-------------------------");
        writer.printf("%-5s %-25s %-10s %-10s %-10s\n", "No.", "Producto", "Cant.", "P. Unit", "Subtotal");
        writer.println("-------------------------");

        for (int i = 0; i < tablaProductosModel.getRowCount(); i++) {
            // Asegúrate de que los índices de columna coincidan con tu tabla
            // Columna 0: No. Producto (no se usa directamente en la DB)
            String codigoProducto = tablaProductosModel.getValueAt(i, 1).toString(); // "Código"
            String nombreProducto = tablaProductosModel.getValueAt(i, 2).toString(); // "Nombre"
            int cantidad = Integer.parseInt(tablaProductosModel.getValueAt(i, 3).toString()); // "Cantidad"
            double precioUnitario = Double.parseDouble(tablaProductosModel.getValueAt(i, 4).toString()); // "Precio unitario"
            double subtotalProducto = Double.parseDouble(tablaProductosModel.getValueAt(i, 5).toString()); // "Subtotal"

            writer.printf("%-5d %-25s %-10d %-10.2f %-10.2f\n",
                          (i + 1), // Número de línea
                          nombreProducto,
                          cantidad,
                          precioUnitario,
                          subtotalProducto);
        }

        writer.println("-------------------------");
        writer.printf("TOTAL A PAGAR: $%.2f\n", totalVentaActual);
        writer.printf("PAGÓ CON: $%.2f\n", Double.parseDouble(txtPagoCon.getText()));
        writer.printf("CAMBIO: $%.2f\n", Double.parseDouble(txtCambio.getText()));
        writer.println("-------------------------");
        writer.println("¡GRACIAS POR SU COMPRA!");
        writer.println("-------------------------");

        JOptionPane.showMessageDialog(this, "Factura creada exitosamente en: " + nombreArchivo, "Factura Creada", JOptionPane.INFORMATION_MESSAGE);

        // Cierra la ventana de Interfazcobro después de crear la factura
        this.dispose();

    } catch (java.io.IOException | NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Error al crear la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_jButton5ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfazcobro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Canvas canvas1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToggleButton jToggleButton1;
    private java.awt.TextField txtCambio;
    private java.awt.TextField txtPagoCon;
    private javax.swing.JTextField txtTotalAPagar;
    // End of variables declaration//GEN-END:variables
}
