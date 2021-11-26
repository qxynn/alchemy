public class Trade {

    /** price of the last matched trade | 3 */
    public double price;

    /** trades since the last trade | 9 */
    public int trades;

    /** number of current asks | 5 */
    public int asks;

    /** number of current bids | 4 */
    public int bids;

    /** price of current asks | 2 */
    public double askPrice;

    /** price of current bids | 1 */
    public double bidPrice;

    /** total volume traded in the day | 8 */
    public int volume;

    /**
     * contructor for trade
     * @param price price of the last matched trade
     * @param trades trades since the last trade
     * @param asks number of current asks
     * @param bids number of current bids
     * @param askPrice price of current asks
     * @param bidPrice price of current bids
     * @param volume total volume traded in the day
     */
    public Trade(double price, int trades, int asks, 
        int bids, double askPrice, double bidPrice, int volume) {
        setPrice(price);
        setTrades(trades);
        setAsks(asks);
        setBids(bids);
        setAskPrice(askPrice);
        setBidPrice(bidPrice);
        setVolume(volume);
    }

    private void setPrice(double price) {
        this.price = price;
    }

    private void setTrades(int trades) {
        this.trades = trades;
    }

    private void setAsks(int asks) {
        this.asks = asks;
    }

    private void setBids(int bids) {
        this.bids = bids;
    }

    private void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    private void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    private void setVolume(int volume) {
        this.volume = volume;
    }
}
