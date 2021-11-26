public class Contract {

    /** open price of contract */
    public double oPrice;

    /** close price of contract */
    public double cPrice;

    /** profit of the contract */
    public double profit;

    /**
     * constructor for the contract
     * @param t the trade the contract is opened on
     */
    public Contract(Trade t) {
        oPrice = t.price;
    }

    public void setCPrice(Trade t) {
        double price = t.price;
        cPrice = price;
    }

    public double getProfit() {
        profit = (cPrice - oPrice) * 12.5 * 4;
        return profit;
    }

    public double getProfitFees() {
        profit = (cPrice - oPrice) * 12.5 * 4 - 2;
        return profit;
    }
}
