package rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import persistence.hibernate.HibernateUtils;

import java.util.Properties;

@ComponentScan({"persistence", "rest"})
@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean(name = "props")
    public Properties getBdProperties() {
        Properties props = new Properties();

        try {
            props.load(StartRestServices.class.getResourceAsStream("/db.properties"));
            System.out.println("Repo properties set.");
            props.list(System.out);
        } catch (Exception e) {
            System.err.println("Cannot find db.properties " + e);
            return null;
        }

        return props;
    }
}
