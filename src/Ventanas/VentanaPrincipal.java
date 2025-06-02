package Ventanas;

import Conexiones.Conexion;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import java.sql.Connection;

public class VentanaPrincipal extends javax.swing.JFrame {

    private Connection conn;
    private String nombreCajero;
    
    public void setPresupuestoBase(String presupuesto) {
    if (lblPresupuestoBase != null) {
        lblPresupuestoBase.setText(presupuesto);
    } else {
        System.err.println("lblPresupuestoBase no está inicializado en VentanaPrincipal.");
    }
}

    public VentanaPrincipal(Connection conexion, String nombre) {
        this.conn = conexion;
        this.nombreCajero = nombre;
        initComponents();
        lblCajeroEnTurno.setText(this.nombreCajero);
        ajustarBotones();

        // 🔧 Muy importante: asegurarte que el content pane tiene BorderLayout
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        // Ajusta tamaño inicial
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // centrado
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
        jTextField1 = new javax.swing.JTextField();
        Cantidad = new javax.swing.JLabel();
        VentaRopa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblPresupuestoBase = new javax.swing.JLabel();
        Minipanel = new javax.swing.JPanel();
        PanelInferior = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCajeroEnTurno = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(Cantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(602, 602, 602)
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
                .addContainerGap(668, Short.MAX_VALUE))
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
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CodigoProducto))
                            .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Cantidad)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
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
        PanelInferior.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, 180, 70));

        jLabel2.setText("Total a Cobrar:");
        PanelInferior.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 70, -1, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Cajero en turno:");
        PanelInferior.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 60));

        lblCajeroEnTurno.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblCajeroEnTurno.setText("jLabel4");
        PanelInferior.add(lblCajeroEnTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 164, 50));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        PanelInferior.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 30, 190, 80));

        jPanelPrincipal.add(PanelInferior, java.awt.BorderLayout.PAGE_END);

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
        Tabla.setDoubleBuffered(true);
        jScrollPane1.setViewportView(Tabla);

        jPanelPrincipal.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new Window().setVisible(true); // Abrir la ventana de inicio de sesión
        dispose(); // Cerrar la VentanaPrincipal
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new Ventas().setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new HistorialVentas().setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Proveedores proveedoresVentana = new Proveedores(this.conn); // ¡Pasa la conexión!
        proveedoresVentana.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new ListaUsuarios().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Inventario().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton13KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F12) {
            jButton13.doClick(); }
    }//GEN-LAST:event_jButton13KeyPressed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        new Interfazcobro().setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        new factura().setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        new RevisarProducto().setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new EliminarProdcuto().setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblCajeroEnTurno;
    private javax.swing.JLabel lblPresupuestoBase;
    // End of variables declaration//GEN-END:variables
}
