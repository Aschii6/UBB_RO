package test;

import model.Position;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class RestTest {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://localhost:8080/trap-game/games";

    private static <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        long gameId = 9;

        Position pos1 = new Position(2, 2);
        Position pos2 = new Position(3, 3);

        Position returnedPos = new Position();

       execute(() -> {
           restTemplate.put(URL + '/' + gameId + "/traps", pos1, returnedPos);
           return returnedPos;
       });

       execute(() -> {
           restTemplate.put(URL + '/' + gameId + "/traps", pos2, returnedPos);
           return returnedPos;
       });
    }
}
