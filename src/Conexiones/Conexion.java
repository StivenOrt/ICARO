package Conexiones;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    
    private static final String url = "jdbc:mysql://localhost:3306/icaro";
    private static final String usuario = "root";
    private static final String pass = "root";
    
    public static Connection conectar() {
    Connection conexion = null;
    try {
        conexion = DriverManager.getConnection(url, usuario, pass);
        System.out.println("Conexion exitosa");
    } catch (Exception e) {
        System.out.println("Error de conexion " + e);
    }
    return conexion;
}  public static void main(String[] args) {
        Conexion test = new Conexion();
        test.conectar();
    }
}


