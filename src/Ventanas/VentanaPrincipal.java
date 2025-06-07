package Ventanas;

import Conexiones.Conexion;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.ResultSet;
import Ventanas.factura;
import java.util.Date;

public class VentanaPrincipal extends javax.swing.JFrame {

    private Connection conn;
    private String nombreCajero;
    private int numeroProductoCarrito = 1;
    private double totalVentaActual = 0.0;
    private int idCajeroActual;
    private String rolUsuarioActual;
    
    public VentanaPrincipal(Connection conexion, String nombre, int idUsuarioLogeado, String rolDelUsuarioLogueado) {
        this.conn = conexion;
        this.nombreCajero = nombre;
        this.idCajeroActual = idUsuarioLogeado;
        this.rolUsuarioActual = rolDelUsuarioLogueado;
        initComponents();
        lblCajeroEnTurno.setText(this.nombreCajero);
        ajustarBotones();
        jTextFieldTotalVenta.setEditable(false);
        
        // 🔧 Muy importante: asegurarte que el content pane tiene BorderLayout
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        // Ajusta tamaño inicial
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // centrado
        setVisible(true);
}
    
    public void setPresupuestoBase(String presupuesto) {
    if (lblPresupuestoBase != null) {
        lblPresupuestoBase.setText(presupuesto);
    } else {
        System.err.println("lblPresupuestoBase no está inicializado en VentanaPrincipal.");
    }
    }
    
    private void agregarProductoACarrito() {
        String codigoProducto = jTextFieldBusqueda.getText().trim();
        String cantidadTexto = jTextFieldCantidad.getText().trim();

        if (codigoProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa el código del producto.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número positivo.", "Cantidad Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una cantidad válida (solo números).", "Cantidad Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT Nombre, Precio, Stock FROM producto WHERE IdProducto = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, codigoProducto);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String nombreProducto = rs.getString("Nombre");
                double precioUnitario = rs.getDouble("Precio");
                int stockDisponible = rs.getInt("Stock");

                if (cantidad > stockDisponible) {
                    JOptionPane.showMessageDialog(this, "No hay suficiente stock para " + nombreProducto + ".\nStock disponible: " + stockDisponible, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
                double subtotal = precioUnitario * cantidad;

                boolean productoEncontradoEnCarrito = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String codigoEnTabla = model.getValueAt(i, 1).toString(); // Columna "Código" (Índice 1)
                    if (codigoEnTabla.equals(codigoProducto)) {
                        int cantidadActual = (int) model.getValueAt(i, 3);
                        double subtotalActual = (double) model.getValueAt(i, 5);

                        int nuevaCantidad = cantidadActual + cantidad;
                        double nuevoSubtotal = subtotalActual + subtotal;

                        model.setValueAt(nuevaCantidad, i, 3);
                        model.setValueAt(nuevoSubtotal, i, 5);
                        productoEncontradoEnCarrito = true;
                        
                        // Solo actualiza el total de la venta con el subtotal del nuevo producto
                        totalVentaActual += subtotal; 
                        break;
                    }
                }

                if (!productoEncontradoEnCarrito) {
                    model.addRow(new Object[]{numeroProductoCarrito, codigoProducto, nombreProducto, cantidad, precioUnitario, subtotal});
                    numeroProductoCarrito++;
                    totalVentaActual += subtotal;
                }
                
                String updateStockSql = "UPDATE producto SET Stock = Stock - ? WHERE IdProducto = ?";
                try (PreparedStatement updatePst = conn.prepareStatement(updateStockSql)) {
                    updatePst.setInt(1, cantidad);
                    updatePst.setString(2, codigoProducto);
                    updatePst.executeUpdate();
                }

                jTextFieldTotalVenta.setText(String.format("%.2f", totalVentaActual));
                jTextFieldBusqueda.setText("");
                jTextFieldCantidad.setText("1");
                jTextFieldBusqueda.requestFocusInWindow();

            } else {
                JOptionPane.showMessageDialog(this, "Producto con código '" + codigoProducto + "' no encontrado en el inventario.", "Producto No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldBusqueda.setText("");
                jTextFieldCantidad.setText("1");
                jTextFieldBusqueda.requestFocusInWindow();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar o actualizar el producto en la base de datos: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    
    
    private void ajustarBotones() {
    
    ImageIcon originalIconCaja = new ImageIcon(getClass().getResource("/imagenes/CajaAbierta.png"));
    int iconSize2 = jButton2.getHeight();
    Image scaledImageCaja = originalIconCaja.getImage().getScaledInstance(iconSize2, iconSize2, Image.SCALE_SMOOTH);
    ImageIcon scaledIconCaja = new ImageIcon(scaledImageCaja);

    jButton2.setIcon(scaledIconCaja);
    jButton2.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton2.setVerticalTextPosition(SwingConstants.CENTER);
    
    
    ImageIcon originalIconPersona = new ImageIcon(getClass().getResource("/imagenes/persona.png"));
    int iconSize3 = jButton3.getHeight(); // Escalar en base al tamaño del botón
    Image scaledImagePersona = originalIconPersona.getImage().getScaledInstance(iconSize3, iconSize3, Image.SCALE_SMOOTH);
    ImageIcon scaledIconPersona = new ImageIcon(scaledImagePersona);

    jButton3.setIcon(scaledIconPersona);
    jButton3.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton3.setVerticalTextPosition(SwingConstants.CENTER);

    
    // Ajuste para jButton4 (Camión Proveedor)
    ImageIcon originalIconCamion = new ImageIcon(getClass().getResource("/imagenes/CamionProveedor.png"));
    int iconSize4 = jButton4.getHeight(); // Escalar en base al tamaño del botón
    Image scaledImageCamion = originalIconCamion.getImage().getScaledInstance(iconSize4, iconSize4, Image.SCALE_SMOOTH);
    ImageIcon scaledIconCamion = new ImageIcon(scaledImageCamion);

    jButton4.setIcon(scaledIconCamion);
    jButton4.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton4.setVerticalTextPosition(SwingConstants.CENTER);

    
    // Ajuste para jButton5 (Reloj Historial)
    ImageIcon originalIconReloj = new ImageIcon(getClass().getResource("/imagenes/RelojHistorial.png"));
    int iconSize5 = jButton5.getHeight(); // Escalar en base al tamaño del botón
    Image scaledImageReloj = originalIconReloj.getImage().getScaledInstance(iconSize5, iconSize5, Image.SCALE_SMOOTH);
    ImageIcon scaledIconReloj = new ImageIcon(scaledImageReloj);

    jButton5.setIcon(scaledIconReloj);
    jButton5.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton5.setVerticalTextPosition(SwingConstants.CENTER);

    
    // Ajuste para jButton6 (Moneda)
    ImageIcon originalIconMoneda = new ImageIcon(getClass().getResource("/imagenes/Moneda.png"));
    int iconSize6 = jButton6.getHeight(); // Escalar en base al tamaño del botón
    Image scaledImageMoneda = originalIconMoneda.getImage().getScaledInstance(iconSize6, iconSize6, Image.SCALE_SMOOTH);
    ImageIcon scaledIconMoneda = new ImageIcon(scaledImageMoneda);

    jButton6.setIcon(scaledIconMoneda);
    jButton6.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton6.setVerticalTextPosition(SwingConstants.CENTER);
    
    // Ajuste para jButton6 (Moneda)
    ImageIcon originalIconCancelar = new ImageIcon(getClass().getResource("/imagenes/Cancelar.png"));
    int iconSize9 = jButton9.getHeight() / 2; // Escalar en base al tamaño del botón
    Image scaledImageCancelar = originalIconCancelar.getImage().getScaledInstance(iconSize9, iconSize9, Image.SCALE_SMOOTH);
    ImageIcon scaledIconCancelar = new ImageIcon(scaledImageCancelar);

    jButton9.setIcon(scaledIconCancelar);
    jButton9.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton9.setVerticalTextPosition(SwingConstants.CENTER);
    
    
        // Configurar jButton10 (Cancelar2)
    ImageIcon originalIconCancelar2 = new ImageIcon(getClass().getResource("/imagenes/Cancelar2.png"));
    int iconSize10 = jButton10.getHeight() / 2;
    Image scaledImageCancelar2 = originalIconCancelar2.getImage().getScaledInstance(iconSize10, iconSize10, Image.SCALE_SMOOTH);
    ImageIcon scaledIconCancelar2 = new ImageIcon(scaledImageCancelar2);

    jButton10.setIcon(scaledIconCancelar2);
    jButton10.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton10.setVerticalTextPosition(SwingConstants.CENTER);

    // Configurar jButton11 (FajoDinero)
    ImageIcon originalIconFajoDinero = new ImageIcon(getClass().getResource("/imagenes/FajoDinero.png"));
    int iconSize11 = jButton11.getHeight() / 2;
    Image scaledImageFajoDinero = originalIconFajoDinero.getImage().getScaledInstance(iconSize11, iconSize11, Image.SCALE_SMOOTH);
    ImageIcon scaledIconFajoDinero = new ImageIcon(scaledImageFajoDinero);

    jButton11.setIcon(scaledIconFajoDinero);
    jButton11.setHorizontalTextPosition(SwingConstants.RIGHT);
    jButton11.setVerticalTextPosition(SwingConstants.CENTER);
}
    private void cerrarVentana() {
        Conexion.cerrarConexion(); 
        dispose();
        System.exit(0);
    }
    
    private void reajustarNumeroProductoCarrito() {
    DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        model.setValueAt(i + 1, i, 0);
    }
    numeroProductoCarrito = model.getRowCount() + 1;
    }
    
    public void vaciarCarritoYReiniciar() {
    DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
    model.setRowCount(0); // Vaciar la tabla del carrito
    totalVentaActual = 0.0; // Resetear el total de la venta
    jTextFieldTotalVenta.setText(String.format("%.2f", totalVentaActual)); // Actualizar el campo de total visual
    numeroProductoCarrito = 1; // Resetear el contador para la columna "No. Producto"
    jTextFieldBusqueda.setText("");
    jTextFieldBusqueda.requestFocusInWindow(); // Volver a enfocar el campo de búsqueda
}    
    
    private void cargarUltimaVentaYAbrirFactura() {
    String idVenta = null;

    // 1. Consulta para obtener el ID de la última venta registrada
    String sqlLastSale = "SELECT IdVenta FROM venta ORDER BY Fecha DESC, IdVenta DESC LIMIT 1";

    try (PreparedStatement pstmtLastSale = conn.prepareStatement(sqlLastSale);
         ResultSet rsLastSale = pstmtLastSale.executeQuery()) {

        if (rsLastSale.next()) {
            idVenta = rsLastSale.getString("IdVenta");
            // Una vez que tenemos el ID, obtenemos todos los detalles y abrimos la factura
            obtenerDetallesVentaYAbrirFactura(idVenta);
        } else {
            JOptionPane.showMessageDialog(this, "No hay ventas registradas para cargar la factura.", "Sin Ventas", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error de base de datos al obtener el último ticket: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error inesperado al cargar la última venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    
    private String obtenerRolDelUsuarioLogueado() {
        return this.rolUsuarioActual; 
    }
    
    public void obtenerDetallesVentaYAbrirFactura(String idVenta) {
        
        PreparedStatement pstmtVenta = null;
        ResultSet rsVenta = null;
        PreparedStatement pstmtDetalles = null;
        ResultSet rsDetalles = null;

    // Consulta para obtener todos los datos de la venta, cliente y cajero
    String sqlVenta = "SELECT v.Fecha, v.Total, c.Nombre AS NombreCliente, c.Identificacion AS IdentificacionCliente, " +
                      "c.Correo AS CorreoCliente, c.Telefono AS TelefonoCliente, " +
                      "u.Nombre AS NombreCajero " +
                      "FROM venta v " +
                      "JOIN cliente c ON v.IdCliente = c.IdCliente " +
                      "JOIN usuario u ON v.IdUsuario = u.IdUsuario " +
                      "WHERE v.IdVenta = ?";

        try {
            
            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(this, "La conexión a la base de datos no está activa.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            pstmtVenta = conn.prepareStatement(sqlVenta);
            pstmtVenta.setString(1, idVenta);
            rsVenta = pstmtVenta.executeQuery();

        if (rsVenta.next()) {
            // Recolectar todos los datos necesarios para la factura
            Date fechaVenta = rsVenta.getTimestamp("Fecha");
            double totalFactura = rsVenta.getDouble("Total");
            String clienteNombre = rsVenta.getString("NombreCliente");
            String clienteCorreo = rsVenta.getString("CorreoCliente");
            String clienteTelefono = rsVenta.getString("TelefonoCliente"); // <-- Ojo: `rsVulta` debe ser `rsVenta` aquí!
            String cajeroNombre = rsVenta.getString("NombreCajero");
            String clienteIdentificacion = rsVenta.getString("IdentificacionCliente");
            String clientePais = "Colombia";

            // Preparar el DefaultTableModel para los productos
            DefaultTableModel modeloTablaProductosFactura = new DefaultTableModel();
            modeloTablaProductosFactura.addColumn("Cantidad");
            modeloTablaProductosFactura.addColumn("Nombre del Producto");
            modeloTablaProductosFactura.addColumn("Valor unitario");
            modeloTablaProductosFactura.addColumn("Total");
            
            String sqlDetalle = "SELECT p.Nombre AS NombreDelProducto, dv.Cantidad, dv.PrecioUnitario, dv.Subtotal " + 
                                "FROM detalleventa dv " +
                                "JOIN producto p ON dv.IdProducto = p.IdProducto " +
                                "WHERE dv.IdVenta = ?";

            // 2. Obtener detalles de los productos vendidos
            pstmtDetalles = conn.prepareStatement(sqlDetalle);
            pstmtDetalles.setString(1, idVenta);
            rsDetalles = pstmtDetalles.executeQuery();

            while (rsDetalles.next()) {
                    modeloTablaProductosFactura.addRow(new Object[]{ // <--- **USANDO `modeloTablaProductosFactura`**
                    rsDetalles.getInt("Cantidad"),
                    rsDetalles.getString("NombreDelProducto"),
                    rsDetalles.getDouble("PrecioUnitario"), // Pasa como double, la factura formateará
                    rsDetalles.getDouble("Subtotal") // Pasa como double, la factura formateará
                });
            }

            // 3. Crear y abrir la ventana 'factura', pasando TODOS los datos
            factura facturaFrame = new factura(
                    this.conn,
                    idVenta,
                    fechaVenta, totalFactura,
                    clienteNombre,
                    clienteCorreo,
                    clienteTelefono,
                    cajeroNombre,
                    clienteIdentificacion,
                    clientePais,
                    modeloTablaProductosFactura
                );
            facturaFrame.setVisible(true);
            facturaFrame.setLocationRelativeTo(this);

        } else {
                JOptionPane.showMessageDialog(this, "No se encontraron detalles para la venta ID: " + idVenta, "Error de Factura", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener detalles de la venta para la factura: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rsDetalles != null) rsDetalles.close();
                if (pstmtDetalles != null) pstmtDetalles.close();
                if (rsVenta != null) rsVenta.close();
                if (pstmtVenta != null) pstmtVenta.close();
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar recursos en obtenerDetallesVentaYAbrirFactura: " + closeEx.getMessage());
            }
        }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrincipal = new javax.swing.JPanel();
        PanelCentral = new javax.swing.JPanel();
        PanelSuperior = new javax.swing.JPanel();
        AlmacenRopa = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        CodigoProducto = new javax.swing.JLabel();
        jTextFieldBusqueda = new javax.swing.JTextField();
        Cantidad = new javax.swing.JLabel();
        VentaRopa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblPresupuestoBase = new javax.swing.JLabel();
        Minipanel = new javax.swing.JPanel();
        Agregar = new javax.swing.JButton();
        PanelInferior = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCajeroEnTurno = new javax.swing.JLabel();
        jTextFieldTotalVenta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelPrincipal.setBackground(new java.awt.Color(204, 204, 255));
        jPanelPrincipal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanelPrincipal.setLayout(new java.awt.BorderLayout());

        PanelCentral.setBackground(new java.awt.Color(204, 204, 255));
        PanelCentral.setLayout(new java.awt.BorderLayout());

        PanelSuperior.setBackground(new java.awt.Color(204, 204, 255));

        AlmacenRopa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AlmacenRopa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/carrito.png"))); // NOI18N
        AlmacenRopa.setText("ALMACEN DE ROPA");

        jButton2.setBackground(new java.awt.Color(0, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Productos");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 102, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Personal");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 102, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Proveedores");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 102, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Historial");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 102, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Ventas");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 51, 51));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Cerrar turno");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        CodigoProducto.setText("Código producto");

        jTextFieldBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBusquedaActionPerformed(evt);
            }
        });

        Cantidad.setText("Cantidad");

        VentaRopa.setBackground(new java.awt.Color(0, 0, 153));
        VentaRopa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("VENTA DE ROPA");

        javax.swing.GroupLayout VentaRopaLayout = new javax.swing.GroupLayout(VentaRopa);
        VentaRopa.setLayout(VentaRopaLayout);
        VentaRopaLayout.setHorizontalGroup(
            VentaRopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VentaRopaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VentaRopaLayout.setVerticalGroup(
            VentaRopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jTextFieldCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCantidadActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setText("BASE:");

        lblPresupuestoBase.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        Minipanel.setBackground(new java.awt.Color(0, 0, 51));
        Minipanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout MinipanelLayout = new javax.swing.GroupLayout(Minipanel);
        Minipanel.setLayout(MinipanelLayout);
        MinipanelLayout.setHorizontalGroup(
            MinipanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        MinipanelLayout.setVerticalGroup(
            MinipanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        Agregar.setBackground(new java.awt.Color(0, 102, 255));
        Agregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Agregar.setForeground(new java.awt.Color(255, 255, 255));
        Agregar.setText("Agregar");
        Agregar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VentaRopa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Minipanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(AlmacenRopa, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(CodigoProducto)
                        .addGap(12, 12, 12)
                        .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(Cantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(435, 435, 435)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPresupuestoBase, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(671, Short.MAX_VALUE))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(AlmacenRopa))
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(VentaRopa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPresupuestoBase, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CodigoProducto))
                            .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Cantidad)
                                .addComponent(jTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Minipanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelCentral.add(PanelSuperior, java.awt.BorderLayout.CENTER);

        jPanelPrincipal.add(PanelCentral, java.awt.BorderLayout.PAGE_START);

        PanelInferior.setBackground(new java.awt.Color(204, 204, 204));
        PanelInferior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton9.setBackground(new java.awt.Color(0, 102, 255));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Cancelar Venta");
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        PanelInferior.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 130, 34));

        jButton10.setBackground(new java.awt.Color(0, 102, 255));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Cancelar Producto");
        jButton10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        PanelInferior.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, 130, 34));

        jButton11.setBackground(new java.awt.Color(0, 102, 255));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Revisar Precio");
        jButton11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        PanelInferior.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 130, 34));

        jButton12.setBackground(new java.awt.Color(0, 102, 255));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Reimprimir Ultimo Ticket");
        jButton12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        PanelInferior.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 160, 34));

        jButton13.setBackground(new java.awt.Color(0, 0, 102));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Cobrar");
        jButton13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jButton13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton13KeyPressed(evt);
            }
        });
        PanelInferior.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, 180, 70));

        jLabel2.setText("Total a Cobrar:");
        PanelInferior.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 70, -1, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Cajero en turno:");
        PanelInferior.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 60));

        lblCajeroEnTurno.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblCajeroEnTurno.setText("jLabel4");
        PanelInferior.add(lblCajeroEnTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 164, 50));

        jTextFieldTotalVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTotalVentaActionPerformed(evt);
            }
        });
        PanelInferior.add(jTextFieldTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 30, 190, 80));

        jPanelPrincipal.add(PanelInferior, java.awt.BorderLayout.PAGE_END);

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Producto", "Código", "Nombre", "Cantidad", "Precio unitario", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Byte.class, java.lang.Byte.class, java.lang.String.class, java.lang.Byte.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabla.setDoubleBuffered(true);
        jScrollPane1.setViewportView(Tabla);

        jPanelPrincipal.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCantidadActionPerformed
        agregarProductoACarrito();
    }//GEN-LAST:event_jTextFieldCantidadActionPerformed

    private void jTextFieldBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBusquedaActionPerformed
        agregarProductoACarrito();
    }//GEN-LAST:event_jTextFieldBusquedaActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new Window().setVisible(true); // Abrir la ventana de inicio de sesión
        dispose(); // Cerrar la VentanaPrincipal
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String rolDelUsuarioLogueado = obtenerRolDelUsuarioLogueado();

    if (!rolDelUsuarioLogueado.equalsIgnoreCase("Administrador")) {
        JOptionPane.showMessageDialog(this, "No tienes permisos para acceder a la ventana de Ventas.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Ventas ventanaVentas = new Ventas(this, conn);
    ventanaVentas.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String rolDelUsuarioLogueado = obtenerRolDelUsuarioLogueado();

    if (!rolDelUsuarioLogueado.equalsIgnoreCase("Administrador")) {
        JOptionPane.showMessageDialog(this, "No tienes permisos para acceder al Historial de Ventas.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    HistorialVentas ventanaHistorial = new HistorialVentas(conn);
    ventanaHistorial.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Proveedores proveedoresVentana = new Proveedores(this.conn); // ¡Pasa la conexión!
        proveedoresVentana.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String rolDelUsuarioLogueado = obtenerRolDelUsuarioLogueado();

    if (!rolDelUsuarioLogueado.equalsIgnoreCase("Administrador")) {
        JOptionPane.showMessageDialog(this, "No tienes permisos para acceder a la Lista de Usuarios.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        return;
    }

        ListaUsuarios ventanaUsuarios = new ListaUsuarios(conn);
        ventanaUsuarios.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Inventario inventarioFrame = new Inventario(this, this.conn);
        inventarioFrame.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldTotalVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTotalVentaActionPerformed
        
    }//GEN-LAST:event_jTextFieldTotalVentaActionPerformed

    private void jButton13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton13KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F12) {
            jButton13.doClick(); }
    }//GEN-LAST:event_jButton13KeyPressed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        DefaultTableModel model = (DefaultTableModel) Tabla.getModel();

// 1. Verificar si hay productos en el carrito (ESTO SE CONSERVA)
if (model.getRowCount() == 0) {
    JOptionPane.showMessageDialog(this, "El carrito de compras está vacío. Agrega productos para cobrar.", "Carrito Vacío", JOptionPane.WARNING_MESSAGE);
    return; // Salir del método si no hay productos
}

if (idCajeroActual == -1) { // -1 es el valor por defecto si no se encontró el cajero
    JOptionPane.showMessageDialog(this, "No se pudo identificar al cajero en turno. La venta no puede ser registrada. Asegúrate de que el usuario 'admin' exista en la base de datos.", "Error de Cajero", JOptionPane.ERROR_MESSAGE);
    return; // Salir del método si el cajero no es válido
}

int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres finalizar la venta y proceder al cobro?", "Confirmar Venta", JOptionPane.YES_NO_OPTION);

if (confirm == JOptionPane.YES_OPTION) {
    Interfazcobro cobroFrame = new Interfazcobro(this, true, totalVentaActual, idCajeroActual, nombreCajero, conn, (DefaultTableModel)Tabla.getModel(), this);
    cobroFrame.setVisible(true);
}

if (confirm == JOptionPane.YES_OPTION) {
    Interfazcobro cobroFrame = new Interfazcobro(this, true, totalVentaActual, idCajeroActual, nombreCajero, conn, (DefaultTableModel)Tabla.getModel(), this);

    cobroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            vaciarCarritoYReiniciar();
        }
    });

    cobroFrame.setVisible(true);
}
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        cargarUltimaVentaYAbrirFactura();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        DefaultTableModel model = (DefaultTableModel) Tabla.getModel();
    int selectedRow = Tabla.getSelectedRow(); // Obtener la fila seleccionada

    // 1. Verificar si hay una fila seleccionada en la tabla
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un producto de la tabla para revisar su precio.", "Producto No Seleccionado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // 2. Obtener el código del producto de la fila seleccionada (columna "Código" - índice 1)
        // El valor de la columna "Código" en tu tabla del carrito es el IdProducto de la DB.
        String codigoProductoStr = model.getValueAt(selectedRow, 1).toString(); 
        int idProducto = Integer.parseInt(codigoProductoStr);

        // 3. Crear y mostrar la ventana de RevisarProducto, pasándole el IdProducto
        RevisarProducto revisarProductoFrame = new RevisarProducto(idProducto);
        revisarProductoFrame.setVisible(true);

    } catch (NumberFormatException e) {
        // Esto podría ocurrir si la columna "Código" en la tabla no contiene un número válido
        JOptionPane.showMessageDialog(this, "Error: El código del producto en la tabla no es un número válido. " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (Exception e) {
        // Captura cualquier otra excepción inesperada
        JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar revisar el producto: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        DefaultTableModel model = (DefaultTableModel) Tabla.getModel();

    // Obtener la fila seleccionada
    int selectedRow = Tabla.getSelectedRow();

    // Verificar si hay una fila seleccionada
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona el producto a cancelar de la tabla.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Confirmar la eliminación con el usuario
    int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres cancelar este producto?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Obtener los datos del producto de la fila seleccionada
            String codigoProducto = model.getValueAt(selectedRow, 1).toString(); // Columna "Código" (Índice 1)
            int cantidadCancelada = (int) model.getValueAt(selectedRow, 3);    // Columna "Cantidad" (Índice 3)
            double subtotalCancelado = (double) model.getValueAt(selectedRow, 5); // Columna "Subtotal" (Índice 5)

            // Devolver el stock a la base de datos
            String updateStockSql = "UPDATE producto SET Stock = Stock + ? WHERE IdProducto = ?";
            try (PreparedStatement updatePst = conn.prepareStatement(updateStockSql)) {
                updatePst.setInt(1, cantidadCancelada);
                updatePst.setString(2, codigoProducto);
                updatePst.executeUpdate();
            }

            // Restar el subtotal del producto cancelado del total de la venta
            totalVentaActual -= subtotalCancelado;
            jTextFieldTotalVenta.setText(String.format("%.2f", totalVentaActual));

            // Eliminar la fila de la tabla
            model.removeRow(selectedRow);
            
            // Reajustar los "No. Producto" después de eliminar una fila (opcional, pero mejora la vista)
            reajustarNumeroProductoCarrito();

            JOptionPane.showMessageDialog(this, "Producto cancelado y stock devuelto exitosamente.", "Producto Cancelado", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al devolver el stock a la base de datos: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al procesar datos del producto: La cantidad o subtotal no son válidos.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al cancelar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
   
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        DefaultTableModel model = (DefaultTableModel) Tabla.getModel();

    // Verificar si hay productos en el carrito para cancelar
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "El carrito de compras ya está vacío.", "Carrito Vacío", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres cancelar la venta completa?\nSe devolverá todo el stock de los productos al inventario.", "Confirmar Cancelación de Venta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String updateStockSql = "UPDATE producto SET Stock = Stock + ? WHERE IdProducto = ?";
            
            // Iterar sobre cada fila de la tabla para devolver el stock
            for (int i = 0; i < model.getRowCount(); i++) {
                String codigoProducto = model.getValueAt(i, 1).toString(); // Columna "Código"
                int cantidadDevuelta = (int) model.getValueAt(i, 3);    // Columna "Cantidad"

                try (PreparedStatement updatePst = conn.prepareStatement(updateStockSql)) {
                    updatePst.setInt(1, cantidadDevuelta);
                    updatePst.setString(2, codigoProducto);
                    updatePst.executeUpdate();
                }
            }

            // Limpiar todas las filas de la tabla
            model.setRowCount(0); // Esto elimina todas las filas

            // Reiniciar el total de la venta
            totalVentaActual = 0.0;
            jTextFieldTotalVenta.setText(String.format("%.2f", totalVentaActual));

            // Reiniciar el contador de productos en el carrito
            numeroProductoCarrito = 1;

            // Limpiar campos de entrada
            jTextFieldBusqueda.setText("");
            jTextFieldCantidad.setText("1");
            jTextFieldBusqueda.requestFocusInWindow();

            JOptionPane.showMessageDialog(this, "Venta cancelada y stock devuelto exitosamente.", "Venta Cancelada", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cancelar la venta y devolver stock: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarActionPerformed
        agregarProductoACarrito();
    }//GEN-LAST:event_AgregarActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Agregar;
    private javax.swing.JLabel AlmacenRopa;
    private javax.swing.JLabel Cantidad;
    private javax.swing.JLabel CodigoProducto;
    private javax.swing.JPanel Minipanel;
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JTable Tabla;
    private javax.swing.JPanel VentaRopa;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldBusqueda;
    private javax.swing.JTextField jTextFieldCantidad;
    private javax.swing.JTextField jTextFieldTotalVenta;
    private javax.swing.JLabel lblCajeroEnTurno;
    private javax.swing.JLabel lblPresupuestoBase;
    // End of variables declaration//GEN-END:variables
}
