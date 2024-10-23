public class FipElem {
    int code;
    int TSIndex;

    public FipElem(int code, int TSIndex) {
        this.code = code;
        this.TSIndex = TSIndex;
    }

    public FipElem(int code) {
        this.code = code;
        this.TSIndex = -1;
    }

    public int getCode() {
        return code;
    }

    public int getTSIndex() {
        return TSIndex;
    }
}