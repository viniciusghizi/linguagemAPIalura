package ghizi.java;

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class NewMain {

    public static void criarPastaLogs(){
        String caminho = "C:/Temp/logs";
        File diretorio = new File(caminho);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
            System.out.println("Criou a pasta para os logs: " + caminho);
        }
    }
    
    
    public static void main(String[] args) {
        criarPastaLogs();
        SpringApplication.run(NewMain.class, args);
    }

}
