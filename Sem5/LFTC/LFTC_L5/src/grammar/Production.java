package grammar;

import java.util.List;

public record Production(String leftSide, List<String> rightSide) {

    @Override
    public String toString() {
        return leftSide + " -> " + String.join(" ", rightSide);
    }
}
