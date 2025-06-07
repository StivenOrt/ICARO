package Ventanas;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Ventas extends javax.swing.JFrame {
    
    private Connection conn;
    private DefaultTableModel modeloTablaDatos;
    
    public Ventas(java.awt.Frame parent, Connection conexion) {
        super();
        this.conn = conexion;
        initComponents(); // Inicializa los componentes de NetBeans
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setTitle("PROYECCIÓN DE VENTAS Y GANANCIAS");

        // Inicializar modelos de tabla y comboboxes
        configurarTablaDatos();
        inicializarComboBoxesFecha();

        // Enlazar el botón Calcular al método de acción
        btnCalcular.addActionListener(e -> calcularProyeccion());
    }
    
    private void configurarTablaDatos() {
        modeloTablaDatos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla de datos no son editables
            }
        };
        tablaDatos.setModel(modeloTablaDatos);

        // Define las columnas que quieres mostrar en la tabla de datos
        // Recomendación: Agrupar por día/mes para las proyecciones
        modeloTablaDatos.addColumn("Periodo"); // Ej: "2024-01-15" o "Enero 2024"
        modeloTablaDatos.addColumn("Total Ventas");
        modeloTablaDatos.addColumn("Total Ganancias"); // Si puedes calcularlas (Precio Venta - Precio Compra)
        modeloTablaDatos.addColumn("Cantidad Productos Vendidos");
        // Puedes añadir más columnas relevantes para tu análisis
    }
    
    private void inicializarComboBoxesFecha() {
        
        cmbDiaDel.removeAllItems();
        cmbMesDel.removeAllItems();
        cmbAnioDel.removeAllItems();
        cmbDiaAl.removeAllItems();
        cmbMesAl.removeAllItems();
        cmbAnioAl.removeAllItems();
        
        // Poblar ComboBoxes para Días, Meses, Años
        for (int i = 1; i <= 31; i++) {
            cmbDiaDel.addItem(String.valueOf(i));
            cmbDiaAl.addItem(String.valueOf(i));
        }

        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}; // CORRECCIÓN: "Junio" en el lugar correcto
    
        for (String mes : nombresMeses) {
            cmbMesDel.addItem(mes);
            cmbMesAl.addItem(mes);
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 5; i++) { // Rango de +/- 5 años del año actual
            cmbAnioDel.addItem(String.valueOf(i));
            cmbAnioAl.addItem(String.valueOf(i));
    }

        // Seleccionar la fecha actual por defecto (opcional)
        Calendar cal = Calendar.getInstance();
        cmbDiaDel.setSelectedItem(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        cmbMesDel.setSelectedIndex(cal.get(Calendar.MONTH)); // Calendar.MONTH es 0-index
        cmbAnioDel.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));

        cmbDiaAl.setSelectedItem(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        cmbMesAl.setSelectedIndex(cal.get(Calendar.MONTH));
        cmbAnioAl.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
    }
    
    private void calcularProyeccion() {
        // 1. Obtener las fechas seleccionadas de los ComboBoxes
        int diaDel = Integer.parseInt(cmbDiaDel.getSelectedItem().toString());
        int mesDel = cmbMesDel.getSelectedIndex();
        int anioDel = Integer.parseInt(cmbAnioDel.getSelectedItem().toString());

        int diaAl = Integer.parseInt(cmbDiaAl.getSelectedItem().toString());
        int mesAl = cmbMesAl.getSelectedIndex();
        int anioAl = Integer.parseInt(cmbAnioAl.getSelectedItem().toString());

        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.set(anioDel, mesDel, diaDel, 0, 0, 0);
        fechaInicio.set(Calendar.MILLISECOND, 0);

        Calendar fechaFin = Calendar.getInstance();
        fechaFin.set(anioAl, mesAl, diaAl, 23, 59, 59);
        fechaFin.set(Calendar.MILLISECOND, 999);

        if (fechaInicio.after(fechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha 'Del' no puede ser posterior a la fecha 'Al'.", "Error de Fecha", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Limpiar la tabla de datos
        modeloTablaDatos.setRowCount(0);

        // 3. Consultar la base de datos para obtener los datos de ventas en el rango de fechas
        String sql = "SELECT DATE(v.Fecha) AS FechaVenta, SUM(v.Total) AS TotalDiario, " +
                     "SUM(dv.Cantidad * (p.Precio - p.PrecioCompra)) AS GananciaDiaria, " + // Calculo de ganancia
                     "SUM(dv.Cantidad) AS CantidadProductosVendidos " +
                     "FROM venta v " +
                     "JOIN detalleventa dv ON v.IdVenta = dv.IdVenta " +
                     "JOIN producto p ON dv.IdProducto = p.IdProducto " +
                     "WHERE v.Fecha BETWEEN ? AND ? " +
                     "GROUP BY DATE(v.Fecha) " + // Agrupar por día para ver el progreso diario
                     "ORDER BY DATE(v.Fecha) ASC";

        SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Formato para la DB

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new java.sql.Timestamp(fechaInicio.getTimeInMillis()));
            pstmt.setTimestamp(2, new java.sql.Timestamp(fechaFin.getTimeInMillis()));

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                boolean hayDatos = false;

                while (rs.next()) {
                    hayDatos = true;
                    String fecha = rs.getString("FechaVenta");
                    double totalVentas = rs.getDouble("TotalDiario");
                    double totalGanancias = rs.getDouble("GananciaDiaria");
                    int cantidadProductos = rs.getInt("CantidadProductosVendidos");

                    modeloTablaDatos.addRow(new Object[]{fecha,
                                                          String.format("%,.2f", totalVentas),
                                                          String.format("%,.2f", totalGanancias),
                                                          cantidadProductos});

                    // Añadir datos al dataset para la gráfica
                    dataset.addValue(totalVentas, "Ventas Diarias", fecha);
                    dataset.addValue(totalGanancias, "Ganancias Diarias", fecha);
                }

                if (!hayDatos) {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos de ventas para el rango de fechas seleccionado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                }

                // 4. Generar la gráfica
                generarGrafica(dataset);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar datos de ventas: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al calcular la proyección: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void generarGrafica(DefaultCategoryDataset dataset) {
        // Limpiar el panel de la gráfica antes de añadir una nueva
        panelGrafico.removeAll();
        panelGrafico.revalidate();
        panelGrafico.repaint();

        if (dataset.getRowCount() == 0) {
            JLabel noDataLabel = new JLabel("No hay datos suficientes para generar la gráfica.", SwingConstants.CENTER);
            panelGrafico.add(noDataLabel, BorderLayout.CENTER);
            return;
        }

        JFreeChart chart = ChartFactory.createLineChart(
            "Proyección de Ventas y Ganancias", // Título del gráfico
            "Periodo",                        // Etiqueta del eje X
            "Monto ($)",                      // Etiqueta del eje Y
            dataset,                          // Datos del gráfico
            PlotOrientation.VERTICAL,         // Orientación del gráfico
            true,                             // Incluir leyenda
            true,                             // Incluir tooltips
            false                             // Incluir URLs
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(panelGrafico.getWidth(), panelGrafico.getHeight())); // Ajustar al tamaño del panel
        panelGrafico.add(chartPanel, BorderLayout.CENTER);
        panelGrafico.revalidate();
        panelGrafico.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnCalcular = new javax.swing.JButton();
        cmbDiaDel = new javax.swing.JComboBox<>();
        cmbDiaAl = new javax.swing.JComboBox<>();
        cmbMesDel = new javax.swing.JComboBox<>();
        cmbMesAl = new javax.swing.JComboBox<>();
        cmbAnioDel = new javax.swing.JComboBox<>();
        cmbAnioAl = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDatos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelGrafico = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Del:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Al:");

        btnCalcular.setText("Cálcular");
        btnCalcular.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        cmbDiaDel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDiaDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDiaDelActionPerformed(evt);
            }
        });

        cmbDiaAl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMesDel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMesAl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbAnioDel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbAnioAl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbAnioDel, 0, 83, Short.MAX_VALUE)
                    .addComponent(cmbMesDel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDiaDel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbAnioAl, 0, 95, Short.MAX_VALUE)
                    .addComponent(cmbMesAl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDiaAl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbDiaDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDiaAl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMesDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMesAl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAnioDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAnioAl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Datos:");

        tablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaDatos);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Graficos:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Todas las proyecciones obtenidas son calculadas con formulas estadísticas a partir de las ventas realizadas, existen ocasiones en las que no se podrá ");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("calcular un resultado por falta de datos.");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("PROYECCIÓN DE VENTAS Y GANANCIAS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(156, 156, 156))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelGrafico.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(76, 76, 76)
                            .addComponent(jLabel4)
                            .addGap(124, 124, 124)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(664, 664, 664))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                            .addComponent(panelGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGrafico, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void cmbDiaDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDiaDelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDiaDelActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JComboBox<String> cmbAnioAl;
    private javax.swing.JComboBox<String> cmbAnioDel;
    private javax.swing.JComboBox<String> cmbDiaAl;
    private javax.swing.JComboBox<String> cmbDiaDel;
    private javax.swing.JComboBox<String> cmbMesAl;
    private javax.swing.JComboBox<String> cmbMesDel;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelGrafico;
    private javax.swing.JTable tablaDatos;
    // End of variables declaration//GEN-END:variables
}
