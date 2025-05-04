package Ventanas;

import Conexiones.Conexion;
import java.sql.Connection;
import java.time.LocalTime;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JTextField;


public class WindowBase extends javax.swing.JFrame {

    public WindowBase() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel1Base = new javax.swing.JPanel();
        Nombre_Programa = new javax.swing.JLabel();
        Panel2Base = new javax.swing.JPanel();
        Boton_Ingresar = new javax.swing.JButton();
        Info = new javax.swing.JLabel();
        Panel3Base = new javax.swing.JPanel();
        Valor_info = new javax.swing.JLabel();
        Valor_Ingresar = new javax.swing.JTextField();

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

        Boton_Ingresar.setBackground(new java.awt.Color(51, 153, 255));
        Boton_Ingresar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Boton_Ingresar.setText("Ingresar");
        Boton_Ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_IngresarActionPerformed(evt);
            }
        });

        Info.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Info.setText("INGRESE EL PRESUPUESTO INCIAL");

        Panel3Base.setBackground(new java.awt.Color(204, 255, 255));
        Panel3Base.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Valor_info.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Valor_info.setText("VALOR :");

        Valor_Ingresar.setBackground(new java.awt.Color(204, 255, 255));
        Valor_Ingresar.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Valor_Ingresar.setToolTipText("");
        Valor_Ingresar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Valor_Ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Valor_IngresarActionPerformed(evt);
            }
        });
        Valor_Ingresar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Valor_IngresarKeyTyped(evt);
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
                .addComponent(Valor_Ingresar, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        Panel3BaseLayout.setVerticalGroup(
            Panel3BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3BaseLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(Panel3BaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Valor_info)
                    .addComponent(Valor_Ingresar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(Boton_Ingresar))
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
                .addComponent(Boton_Ingresar)
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

    private void Boton_IngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_IngresarActionPerformed
        Boton_Ingresar.addActionListener(e -> {
    try {
        Connection conexion = Conexion.conectar(); // Conectar a la base de datos

        String sql = "INSERT INTO base_diaria (id_base, fecha, valor_base, usuario_registro, hora_registro, observaciones) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement consulta = conexion.prepareStatement(sql);

        // Obtener los valores ingresados
        String fecha = LocalDate.now().toString(); // Fecha actual en formato YYYY-MM-DD
        double valorBase = Double.parseDouble(Valor_Ingresar.getText()); // Convertir a decimal
        String usuario = "Cajero"; // Puedes obtener el usuario dinámicamente si es necesario
        String horaActual = LocalTime.now().toString(); // Hora actual en formato HH:MM:SS
        String observaciones = "Registro automático"; // Puedes obtenerlo desde un campo si es necesario

        // Asignar valores a la consulta SQL
        consulta.setString(1, fecha);
        consulta.setDouble(2, valorBase);
        consulta.setString(3, usuario);
        consulta.setString(4, horaActual);
        consulta.setString(5, observaciones);

        // Ejecutar la consulta
        int filasAfectadas = consulta.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Base diaria registrada correctamente!");
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar la base diaria.");
        }

        // Cerrar conexión
        consulta.close();
        conexion.close();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        ex.printStackTrace();
    }
});
    }//GEN-LAST:event_Boton_IngresarActionPerformed

    private void Valor_IngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Valor_IngresarActionPerformed
        
    }//GEN-LAST:event_Valor_IngresarActionPerformed

    private void Valor_IngresarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Valor_IngresarKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_Valor_IngresarKeyTyped

   
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WindowBase().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Ingresar;
    private javax.swing.JLabel Info;
    private javax.swing.JLabel Nombre_Programa;
    private javax.swing.JPanel Panel1Base;
    private javax.swing.JPanel Panel2Base;
    private javax.swing.JPanel Panel3Base;
    private javax.swing.JTextField Valor_Ingresar;
    private javax.swing.JLabel Valor_info;
    // End of variables declaration//GEN-END:variables
}
