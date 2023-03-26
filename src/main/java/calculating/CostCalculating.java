package calculating;


public class CostCalculating {
    private double totalCost = 0;
    private long totalMinutes = 0;
    private final String tariffType;

    public CostCalculating(String tariffType) {
        this.tariffType = tariffType;
        if (tariffType.equals("06")) {
            totalCost += 100;
        }
    }
    // Вычисление стоимости текущего звонка 
    public double calculate(String callType, long minutes) {
        double cost = 0;
        switch (tariffType) {
            case "03":
                cost = minutes * 1.5;
                break;
            case "06":
                if (totalMinutes < 300) {
                    totalMinutes += minutes;
                    if (totalMinutes > 300) {
                        cost += totalMinutes-300;
                    }
                } else {
                    cost += minutes;
                    totalMinutes += minutes;
                }
                break;
            case "11":
                if (callType.equals("01")) {
                    if (totalMinutes < 100) {
                        totalMinutes += minutes;
                        if (totalMinutes <= 100) {
                            cost += minutes * 0.5;
                        } else {
                            cost += (100 - (totalMinutes-minutes)) * 0.5 + (totalMinutes - 100) * 1.5;
                        }
                    } else {
                        cost += minutes * 1.5;
                        totalMinutes += minutes;
                    }
                }
                break;
            default:
                break;
        }
        totalCost += cost;
        return cost;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
