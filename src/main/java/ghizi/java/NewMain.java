package ghizi.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ghizi.java", "controller"})
public class NewMain {

    
    public static void main(String[] args) {
        SpringApplication.run(NewMain.class, args);
    }

}
