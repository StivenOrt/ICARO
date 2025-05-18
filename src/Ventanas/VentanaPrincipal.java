package Ventanas;

import Conexiones.Conexion;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import java.sql.Connection;



public class VentanaPrincipal extends javax.swing.JFrame {

    private Connection conexionBD;
    private String nombreCajero;
    
    public void setPresupuestoBase(String presupuesto) {

        if (lblPresupuestoBase != null) {
        lblPresupuestoBase.setText(presupuesto); // Ajusta el texto para que coincida con tu etiqueta
    } else {
        System.err.println("lblPresupuestoBase no está inicializado en VentanaPrincipal.");
    }
    }

    public VentanaPrincipal(String nombre) {
        this.nombreCajero = nombre;
        initComponents();
        lblCajeroEnTurno.setText(this.nombreCajero);
        ajustarBotones();
        setLocationRelativeTo(null);
    }

    public VentanaPrincipal() {
        initComponents();
        ajustarBotones();
        setLocationRelativeTo(null);
    }
    
    public VentanaPrincipal(Connection conexion, String nombre) {
    super("Almacén de Ropa");
    this.conexionBD = conexion;
    this.nombreCajero = nombre;
    
    initComponents();
    lblCajeroEnTurno.setText(this.nombreCajero);
    ajustarBotones();
    
    pack();
    
    Rectangle maxBounds = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getMaximumWindowBounds();
    
    setBounds(maxBounds);    
    setResizable(true);
    SwingUtilities.invokeLater(() ->
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH)
    );
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);

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
        Conexion.cerrarConexion(); // Cerrar la conexión al cerrar la ventana principal
        dispose();
        System.exit(0);
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelSuperior = new javax.swing.JPanel();
        AlmacenRopa = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        CodigoProducto = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        Cantidad = new javax.swing.JLabel();
        VentaRopa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblPresupuestoBase = new javax.swing.JLabel();
        Minipanel = new javax.swing.JPanel();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        PanelInferior = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Total = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        NombreCajero = new javax.swing.JLabel();
        lblCajeroEnTurno = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VentaRopaLayout.setVerticalGroup(
            VentaRopaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setText("BASE:");

        lblPresupuestoBase.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(CodigoProducto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(Cantidad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(310, 310, 310)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPresupuestoBase, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 552, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addComponent(AlmacenRopa)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(VentaRopa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AlmacenRopa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)
                        .addComponent(jButton6)
                        .addComponent(jButton8)
                        .addComponent(jButton5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VentaRopa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Cantidad)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CodigoProducto)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelSuperiorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPresupuestoBase, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel1.add(PanelSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 2, 1225, 140));

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

        jPanel1.add(Minipanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 146, 1225, -1));

        PanelCentral.setBackground(new java.awt.Color(204, 204, 255));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No. Producto", "Código", "Nombre", "Cantidad", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Byte.class, java.lang.Byte.class, java.lang.String.class, java.lang.Byte.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Tabla);

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCentralLayout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(PanelCentral, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 177, 1225, -1));

        PanelInferior.setBackground(new java.awt.Color(204, 204, 204));

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

        jButton13.setBackground(new java.awt.Color(0, 0, 102));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("F12 - Cobrar");
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

        jLabel2.setText("Total a Cobrar:");

        Total.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Cajero en turno:");

        NombreCajero.setBackground(new java.awt.Color(204, 255, 255));

        lblCajeroEnTurno.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblCajeroEnTurno.setText("jLabel4");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelInferiorLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCajeroEnTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(NombreCajero, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(PanelInferiorLayout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)
                        .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NombreCajero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelInferiorLayout.createSequentialGroup()
                        .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCajeroEnTurno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        jPanel1.add(PanelInferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 597, 1219, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 710, 1237, -1));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        new Interfazcobro().setVisible(true);  
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
       new factura().setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    Proveedores proveedoresVentana = new Proveedores(this.conexionBD); // ¡Pasa la conexión!
    proveedoresVentana.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new EliminarProdcuto().setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    new Window().setVisible(true); // Abrir la ventana de inicio de sesión
    dispose(); // Cerrar la VentanaPrincipal
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new ListaUsuarios().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new Ventas().setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new HistorialVentas().setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Inventario().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        new RevisarProducto().setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton13KeyPressed
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F12) {
        jButton13.doClick(); }
    }//GEN-LAST:event_jButton13KeyPressed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AlmacenRopa;
    private javax.swing.JLabel Cantidad;
    private javax.swing.JLabel CodigoProducto;
    private javax.swing.JPanel Minipanel;
    private javax.swing.JLabel NombreCajero;
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JTable Tabla;
    private javax.swing.JLabel Total;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblCajeroEnTurno;
    private javax.swing.JLabel lblPresupuestoBase;
    // End of variables declaration//GEN-END:variables
}
