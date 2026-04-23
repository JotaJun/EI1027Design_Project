package es.uji.ei1027.SgOviProject;

import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneradorPasswords {

    public static void main(String[] args) {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        int opcion;
        Scanner sc = new Scanner(System.in);

        System.out.println("Elige opción:");
        System.out.println("[0] --> contraseñas por defecto");
        System.out.println("[1] --> indicar contraseñas propias");

        opcion = Integer.parseInt(sc.nextLine());

        if (opcion == 0) {
            // OPCIÓN 0: Lista predefinida
            ArrayList<String> contrasenas = new ArrayList<>(List.of(
                    "passInstructor",
                    "userOviPass",
                    "passPAP",
                    "passTutor"
            ));

            System.out.println("\n--- CONTRASEÑAS POR DEFECTO ---");
            for (String pass : contrasenas) {
                String hash = encryptor.encryptPassword(pass);
                System.out.println(pass + " -> " + hash);
            }

        } else if (opcion == 1) {
            // OPCIÓN 1: Bucle infinito hasta que el usuario escriba "salir"
            System.out.println("\n--- MODO MANUAL (Escribe 'salir' para terminar) ---");

            while (true) {
                System.out.print("Introduce contraseña para encriptar: ");
                String contra = sc.nextLine();

                if (contra.equalsIgnoreCase("salir")) {
                    System.out.println("Saliendo del generador...");
                    break;
                }

                String hash = encryptor.encryptPassword(contra);
                System.out.println("Hash generado: " + hash + "\n");
            }
        } else {
            System.out.println("Opción no válida.");
        }

        sc.close();
    }
}
