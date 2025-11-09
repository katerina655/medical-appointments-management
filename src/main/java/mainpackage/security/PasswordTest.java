package mainpackage.security;

public class PasswordTest {
    public static void main(String[] args) {
        try {
            String password = "1234"; 
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(password, salt);

            System.out.println("Salt: " + salt);
            System.out.println("Hash: " + hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
