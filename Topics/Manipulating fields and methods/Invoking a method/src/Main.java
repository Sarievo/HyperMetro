import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodsDemo {

    public static void main(String[] args) {
        try {
            System.out.println(
                SomeClass.class.getDeclaredMethods()[0].invoke(new SomeClass())
            );
        } catch (Exception ignored) {}
    }
}

//class SomeClass {
//    public void method() {
//        System.out.println("I'm invoked!");
//    }
//}
