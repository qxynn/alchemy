public class TradeNode {

    /** trade data */
    public Trade trade;

    /** previous trade */
    public TradeNode previous;

    /**
     * List node for the trade
     * @param t trade data
     * @param previous previous trade
     */
    public TradeNode(Trade t, TradeNode previous) {
        this.trade = t;
        this.previous = previous;
    }
}
