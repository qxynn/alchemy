public class TradeList {

    /** current trade in the list */
    public TradeNode current;

    /** previous trade in the list */
    //private TradeNode previous;

    /** list of trades */
    private TradeNode[] list;

    /** size of the list of trades */
    public int size;

    /** max capacity for the list */
    private int capacity = 100;

    /**
     * constructor for tradelist
     * sets size to 0
     * starts a new list
     */
    public TradeList() {
        list = new TradeNode[capacity];
        size = 0;
    }

    /**
     * adds a trade to the trade list
     * @param t trade to add
     */
    public void add(Trade t) {
        checkSize();
        TradeNode tn;
        if (size == 0) {
            tn = new TradeNode(t, null);
            current = tn;
            //previous = tn;
        }
        else {
            tn = new TradeNode(t, current);
            //previous = current;
            current = tn;
        }
        list[size] = tn;
        size++;
    }

    /**
     * ensures the list does not overflow
     * resets to 5 items if it is at 100 for efficiency
     */
    private void checkSize() {
        if (size == capacity) {
            TradeNode[] temp = list;
            list = new TradeNode[100];
            for (int i = 0; i < 5; i++) {
                list[i] = temp[capacity - 5 + i];
                list[0].previous = null;
            }
        }
        size = 5;
    }
    /**
     * gets the price change between the past 5 trades
     * @return price difference | 0.0 if invalid
     */
    public double delta5() {
        if (size < 5) {
            return 0.0;
        }
        return current.previous.previous.previous.previous.trade.price -
            current.trade.price;
    }

    /**
     * gets the price change between the past 3 trades
     * @return price difference | 0.0 if invalid
     */
    public double delta3() {
        if (size < 3) {
            return 0.0;
        }
        return current.previous.previous.previous.trade.price - 
            current.trade.price;
    }

    public double price() {
        return current.trade.price;
    }

    public Trade previous() {
        return current.previous.trade;
    }

    public double delta(double b) {
        return current.trade.price - b;
    }
}
