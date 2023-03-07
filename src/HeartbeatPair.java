import java.math.BigDecimal;

public class HeartbeatPair{
    private double time;
    private BigDecimal current;

    public HeartbeatPair(double time, BigDecimal current) {
        this.time = time;
        this.current = current;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }
}