package Ventanas;

import Conexiones.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Permisos extends javax.swing.JFrame {
    
    // Variables de clase
    private String modo; // Para saber si es "agregar" o "modificar"
    private String usuarioIdAModificar; // Para guardar el ID del usuario si estamos modificando
    private Connection conn; // Conexión a la base de datos
    private javax.swing.ButtonGroup grupoRoles;
    
     public Permisos(String modo, String usuarioId) { // Eliminamos Frame owner y boolean modal
        this.modo = modo;
        this.usuarioIdAModificar = usuarioId;
        initComponents();
        setLocationRelativeTo(null); // Centra la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configuración de los JToggleButtons para que solo uno pueda ser seleccionado
        grupoRoles = new javax.swing.ButtonGroup(); // Inicializa la instancia
        grupoRoles.add(jToggleButton1); // ADMIN
        grupoRoles.add(jToggleButton5); // CAJERO

        // Ajustes para el modo "modificar" o "agregar"
        if ("modificar".equals(modo)) {
            setTitle("Modificar Usuario");
            jTextField1.setEditable(false); // No permitir cambiar el IdUsuario al modificar
            cargarDatosUsuarioParaModificar(usuarioIdAModificar);
        } else if ("agregar".equals(modo)) {
            setTitle("Agregar Usuario");
            // No hacer nada especial, campos vacíos para nuevo usuario
        } else {
            JOptionPane.showMessageDialog(this, "Modo de operación no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Cierra la ventana si el modo es inválido
            return;
        }
    }
     
     private void configurarVentanaSegunModo() {
        if ("agregar".equals(modo)) {
            this.setTitle("Agregar Nuevo Usuario");
            limpiarCampos();
            jTextField1.setEditable(true); // IdUsuario debe ser editable para un nuevo registro
            jToggleButton5.setSelected(true); // CAJERO por defecto
        } else if ("modificar".equals(modo)) {
            this.setTitle("Modificar Usuario"); // El texto "MODIFICAR" ya está en la UI
            cargarDatosUsuarioParaModificar(usuarioIdAModificar);
            jTextField1.setEditable(false); // IdUsuario no debe ser editable en modo modificación (es la clave primaria)
        }
    }
     
     private void limpiarCampos() {
        jTextField1.setText(""); // Nombre
        jTextField2.setText(""); // Contraseña
        jTextField4.setText(""); // Número
        jTextField5.setText(""); // Correo
        jTextField6.setText(""); // Información extra
        grupoRoles.clearSelection(); // Limpia la selección de los ToggleButtons
    }

     
     private void cargarDatosUsuarioParaModificar(String usuarioId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Conexion.conectar(); // Usando el método correcto
            String sql = "SELECT Nombre, Contraseña, Numero, Correo, InformacionExtra, Rol FROM Usuario WHERE IdUsuario = ?"; // Usando tabla 'Usuario' y todas las columnas
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuarioId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                jTextField1.setText(rs.getString("Nombre")); // IdUsuario
                jTextField2.setText(rs.getString("Contraseña")); // Contraseña. **ADVERTENCIA DE SEGURIDAD: NO HAGAS ESTO EN PRODUCCIÓN.**
                jTextField4.setText(rs.getString("Numero")); // Número
                jTextField5.setText(rs.getString("Correo")); // Correo
                jTextField6.setText(rs.getString("InformacionExtra")); // Información extra

                String rolGuardado = rs.getString("Rol");
                if ("Admin".equalsIgnoreCase(rolGuardado)) { // Ajusta el texto del rol según tu DB ('Admin' o 'Cajero')
                    jToggleButton1.setSelected(true); // Selecciona ADMIN
                } else if ("Cajero".equalsIgnoreCase(rolGuardado)) { // Ajusta el texto del rol según tu DB
                    jToggleButton5.setSelected(true); // Selecciona CAJERO
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el usuario para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del usuario: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // No cerrar la conexión aquí si es un Singleton y se reutiliza.
            // Si tu clase Conexion maneja el cierre, no es necesario llamar a Conexion.cerrarConexion() aquí.
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // Si la conexión es Singleton y se gestiona globalmente, NO la cierres aquí.
                // Si cada método abre y cierra su propia conexión, entonces:
                // if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método auxiliar para verificar si un usuario ya existe
    private boolean existeUsuario(Connection conn, String idUsuario) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM Usuario WHERE IdUsuario = ?"; // Usando tabla 'Usuario'
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idUsuario);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODIFICAR");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre de usuario:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Contraseña:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Numero (Opcional):");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Correo (Opcional):");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Informacion extra:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("ROLES");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ADMIN");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("CAJERO");

        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Guardar");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(43, 43, 43)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(77, 77, 77))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Obtener los datos de los campos de texto
        String Nombre = jTextField1.getText().trim(); // "Nombre usuario" de la interfaz -> va a la columna 'Nombre' en la BD
        String contraseña = jTextField2.getText().trim(); // Contraseña. **ADVERTENCIA DE SEGURIDAD: Considera usar hashing**
        String numero = jTextField4.getText().trim(); // Número
        String correo = jTextField5.getText().trim(); // Correo
        String infoExtra = jTextField6.getText().trim(); // Información extra

        String rolSeleccionado = "";
        if (jToggleButton1.isSelected()) { // Si el botón "ADMIN" está seleccionado
            rolSeleccionado = "Administrador";
        } else if (jToggleButton5.isSelected()) { // Si el botón "CAJERO" está seleccionado
            rolSeleccionado = "Cajero";
        }

        // Validaciones de campos obligatorios
        if (Nombre.isEmpty() || contraseña.isEmpty() || rolSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos 'Nombre de usuario', 'Contraseña' y 'Rol' son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection localConn = null; // Usar una variable local para la conexión para mejor manejo del try-with-resources
        PreparedStatement pstmt = null;

        try {
            localConn = Conexion.conectar(); // Obtener la conexión a la base de datos
            if (localConn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo establecer la conexión a la base de datos.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("agregar".equals(modo)) {
                // Verificar si el nombre de usuario (columna 'Nombre' en la BD) ya existe
                if (existeUsuario(localConn, Nombre)) {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario '" + Nombre + "' ya existe. Por favor, elija otro.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "INSERT INTO Usuario (Nombre, Correo, Contraseña, Rol, Numero, InformacionExtra) VALUES (?, ?, ?, ?, ?, ?)"; 
                pstmt = localConn.prepareStatement(sql);
                
                pstmt.setString(1, Nombre);     // Parámetro 1: Nombre (columna 'Nombre' en la BD)
                pstmt.setString(2, correo.isEmpty() ? null : correo);       // Parámetro 2: Correo
                pstmt.setString(3, contraseña);             // Parámetro 3: Contraseña
                pstmt.setString(4, rolSeleccionado);        // Parámetro 4: Rol
                pstmt.setString(5, numero.isEmpty() ? null : numero);       // Parámetro 5: Numero
                pstmt.setString(6, infoExtra.isEmpty() ? null : infoExtra); // Parámetro 6: Información Extra
                
                pstmt.executeUpdate(); // Ejecuta la inserción
                JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } else if ("modificar".equals(modo)) {
                String sql = "UPDATE Usuario SET Nombre = ?, Correo = ?, Contraseña = ?, Rol = ?, Numero = ?, InformacionExtra = ? WHERE IdUsuario = ?";
                pstmt = localConn.prepareStatement(sql);
                
                pstmt.setString(1, Nombre);     // Parámetro 1: Nombre (columna 'Nombre' en la BD)
                pstmt.setString(2, correo.isEmpty() ? null : correo);       // Parámetro 2: Correo
                pstmt.setString(3, contraseña);             // Parámetro 3: Contraseña
                pstmt.setString(4, rolSeleccionado);        // Parámetro 4: Rol
                pstmt.setString(5, numero.isEmpty() ? null : numero);       // Parámetro 5: Numero
                pstmt.setString(6, infoExtra.isEmpty() ? null : infoExtra); // Parámetro 6: Información Extra
                
                // El último parámetro es el IdUsuario del registro a modificar.
                // Asegúrate de que 'usuarioIdAModificar' contenga el valor entero del ID de la base de datos.
                pstmt.setInt(7, Integer.parseInt(usuarioIdAModificar)); // Parámetro 7: IdUsuario (para el WHERE)
                
                pstmt.executeUpdate(); // Ejecuta la actualización
                JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            this.dispose(); // Cierra el diálogo después de guardar
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al guardar usuario: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprime la traza completa del error en la consola
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Error de formato. El ID del usuario a modificar no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        } finally {
            // Asegúrate de cerrar el PreparedStatement
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton5;
    // End of variables declaration//GEN-END:variables
}
