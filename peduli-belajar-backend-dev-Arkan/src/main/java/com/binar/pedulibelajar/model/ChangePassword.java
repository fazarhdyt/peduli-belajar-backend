import java.util.Scanner;

public class ChangePassword {
    private static String storedPassword = "old_password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Masukkan kata sandi lama:");
        String oldPassword = scanner.nextLine();

        System.out.println("Masukkan kata sandi baru:");
        String newPassword = scanner.nextLine();

        changePassword(oldPassword, newPassword);
    }

    public static void changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(storedPassword)) {
            storedPassword = newPassword;
            System.out.println("Kata sandi berhasil diubah.");
        } else {
            System.out.println("Kata sandi lama yang Anda masukkan salah.");
        }
    }
}
