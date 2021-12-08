import java.lang.reflect.Field;

final class AccountUtils {

    private AccountUtils() { }

    public static void increaseBalance(Account account, long amount) {
        try {
            Field field = account.getClass().getDeclaredField("balance");
            field.setAccessible(true);
            field.set(account, (long) field.get(account) + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}