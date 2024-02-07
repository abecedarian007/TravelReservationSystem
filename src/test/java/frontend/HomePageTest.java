package frontend;

public class HomePageTest {
    @org.junit.jupiter.api.Test
    void getWorkingDir() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory: " + currentWorkingDirectory);

    }

}
