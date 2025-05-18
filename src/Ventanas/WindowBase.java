package Ventanas;

import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;


public class WindowBase extends javax.swing.JFrame {
    
    private Connection conexionBD;
    private VentanaPrincipal ventanaPrincipal; // Referencia a la ventana principal
    private String nombreCajero;
    
    public WindowBase(VentanaPrincipal ventanaPrincipal, Connection conexionBD, String nombreCajero) {
    this.ventanaPrincipal = ventanaPrincipal;
    this.conexionBD = conexionBD;
    this.nombreCajero = nombreCajero;
    initComponents();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void guardarBaseDeDatos(String nombreCajero, double valorBase) {
     PreparedStatement consulta = null;
    try {
        String sql = "INSERT INTO base_diaria (valor_base, usuario_registro, fecha) VALUES (?, ?, ?)";
        consulta = conexionBD.prepareStatement(sql);
        consulta.setDouble(1, valorBase);
        consulta.setString(2, nombreCajero);
        consulta.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        int filasAfectadas = consulta.executeUpdate();
        // ... (resto del código) ...
    } catch (SQLException e) {
        System.err.println("Error al guardar la base diaria: " + e.getMessage());
        JOptionPane.showMessageDialog(WindowBase.this, "Error al guardar la base diaria.", "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (consulta != null) consulta.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel1Base = new javax.swing.JPanel();
        Nombre_Programa = new javax.swing.JLabel();
        Panel2Base = new javax.swing.JPanel();
        btnIngresarPresupuesto = new javax.swing.JButton();
        Info = new javax.swing.JLabel();
        Panel3Base = new javax.swing.JPanel();
        Valor_info = new javax.swing.JLabel();
        txtPresupuestoInicial = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Panel1Base.setBackground(new java.awt.Color(255, 255, 255));
        Panel1Base.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 8));
        Panel1Base.setName(""); // NOI18N
        Panel1Base.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Nombre_Programa.setFont(new java.awt.Font("Algerian", 1, 48)); // NOI18N
        Nombre_Programa.setForeground(new java.awt.Color(0, 0, 204));
        Nombre_Programa.setText("ICARO");
        Panel1Base.add(Nombre_Programa, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, -1, -1));

        Panel2Base.setBackground(new java.awt.Color(204, 255, 255));
        Panel2Base.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnIngresarPresupuesto.setBackground(new java.awt.Color(51, 153, 255));
        btnIngresarPresupuesto.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnIngresarPresupuesto.setText("Ingresar");
        btnIngresarPresupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarPresupuestoActionPerformed(evt);
            }
        });

        Info.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Info.setText("INGRESE EL PRESUPUESTO INCIAL");

        Panel3Base.setBackground(new java.awt.Color(204, 255, 255));
        Panel3Base.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Valor_info.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Valor_info.setText("VALOR :");

        txtPresupuestoInicial.setBackground(new java.awt.Color(204, 255, 255));
        txtPresupuestoInicial.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtPresupuestoInicial.setToolTipText("");
        txtPresupuestoInicial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtPresupuestoInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPresupuestoInicialActionPerformed(evt);
            }
        });
        txtPresupuestoInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPresupuestoInicialKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout Panel3BaseLayout = new javax.swing.GroupLayout(Panel3Base);
        Panel3Base.setLayout(Panel3BaseLayout);
        Panel3BaseLayout.setHorizontalGroup(
            Panel3BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3BaseLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(Valor_info)
                .addGap(18, 18, 18)
                .addComponent(txtPresupuestoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        Panel3BaseLayout.setVerticalGroup(
            Panel3BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3BaseLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(Panel3BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Valor_info)
                    .addComponent(txtPresupuestoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Panel2BaseLayout = new javax.swing.GroupLayout(Panel2Base);
        Panel2Base.setLayout(Panel2BaseLayout);
        Panel2BaseLayout.setHorizontalGroup(
            Panel2BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2BaseLayout.createSequentialGroup()
                .addGroup(Panel2BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel2BaseLayout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(btnIngresarPresupuesto))
                    .addGroup(Panel2BaseLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(Panel2BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel3Base, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Info))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        Panel2BaseLayout.setVerticalGroup(
            Panel2BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2BaseLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(Info)
                .addGap(18, 18, 18)
                .addComponent(Panel3Base, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnIngresarPresupuesto)
                .addGap(39, 39, 39))
        );

        Panel1Base.add(Panel2Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 510, 310));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1Base, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1Base, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarPresupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarPresupuestoActionPerformed
        String presupuesto = txtPresupuestoInicial.getText().trim();
    try {
        double valorPresupuesto = Double.parseDouble(presupuesto);
        guardarBaseDeDatos(nombreCajero, valorPresupuesto);
        ventanaPrincipal.setPresupuestoBase(String.valueOf(valorPresupuesto));
        dispose();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnIngresarPresupuestoActionPerformed

    private void txtPresupuestoInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPresupuestoInicialActionPerformed
        
    }//GEN-LAST:event_txtPresupuestoInicialActionPerformed

    private void txtPresupuestoInicialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPresupuestoInicialKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPresupuestoInicialKeyTyped

   
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Info;
    private javax.swing.JLabel Nombre_Programa;
    private javax.swing.JPanel Panel1Base;
    private javax.swing.JPanel Panel2Base;
    private javax.swing.JPanel Panel3Base;
    private javax.swing.JLabel Valor_info;
    private javax.swing.JButton btnIngresarPresupuesto;
    private javax.swing.JTextField txtPresupuestoInicial;
    // End of variables declaration//GEN-END:variables
}
