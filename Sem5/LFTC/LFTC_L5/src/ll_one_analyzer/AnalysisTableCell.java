package ll_one_analyzer;

import java.util.List;

public record AnalysisTableCell(List<String> productionRes, int productionIndex) {
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AnalysisTableCell cell)) {
            return false;
        }

        return productionRes.equals(cell.productionRes) && productionIndex == cell.productionIndex;
    }
}
