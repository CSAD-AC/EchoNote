package uno.zhuchen.echonotejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EchoNoteJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchoNoteJavaApplication.class, args);
    }

}
