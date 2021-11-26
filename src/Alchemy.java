import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Alchemy {

    public static int days = 7;

    public static int currentLines;

    public static int globalProfit = 0;

    public static int globalProfitFees = 0;

    public static TradeList trades;

    public static TradeList uniqueTrades;

    public static Contract[] contracts;

    public static int cSize;

    public static String testFile;

    public static boolean open;

    public static int p = 0;

    public static int iterations;

    public static int numTrades;
    
    public static double profit;
    public static double profitFees;

    public static double maxProfit;
    public static double maxLoss;

    public static int successful;
    public static int negative;
    public static int zero;
    public static double ratio;

    public static int testint;

    public static int lines;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static State s;

    public static void main(String[] args) throws Exception {
        for (int n = 1; n <= days; n++) {
            initialize(n);
            File test = new File(testFile);
            Scanner scnr = new Scanner(test);
            scnr.nextLine();
            System.out.println();
            for (int i = 0; i < 10; i++) {
                String s = scnr.nextLine();
                Trade t = processString(s);
                trades.add(t);
            }
            //for (int i = 0; i < iterations; i++) {
            while (scnr.hasNextLine()) {
                String s = scnr.nextLine();
                Trade t = processString(s);
                trades.add(t);
                if (t.price != trades.previous().price) {
                    uniqueTrades.add(t);
                }
                analyze();
                numTrades++;
                lines++;
                progressBar();
            }
            if (open) {
                sell(trades.current.trade);
            }
            scnr.close();
            globalProfit += profit;
            globalProfitFees += profitFees;
            //print();
        }
        System.out.print("\n\n\n");
        System.out.print("GLOBAL PROFIT OVER " + days + " DAYS: " + globalProfit + " | " + globalProfitFees + "\n\n\n");
    }

    public static void analyze() {
        checkState();
        if (!open) {
            if (trades.current.trade.asks > trades.previous().asks &&
                trades.current.trade.bidPrice > trades.previous().bidPrice &&
                trades.delta3() == 0
                ) {
                buy(trades.current.trade);
            }
            p++;
        }
        else {
            if (currentProfit() < 0 || currentProfit() > 0) {
                sell(trades.current.trade);
            }
        }
    }

    public static void checkState() {
        if (trades.price() < trades.previous().price && 
            trades.current.trade.bids < trades.previous().bids &&
            trades.current.trade.askPrice < trades.previous().askPrice) {
                s = State.BEAR;
        }
        else if (trades.price() > trades.previous().price && 
            trades.current.trade.askPrice > trades.previous().askPrice && 
            trades.delta3() > .25 &&
            (s == State.BEAR_INCREASING || s == State.BULL)) {
                s = State.BULL;
        }
        else if (trades.current.trade.bids < trades.previous().bids && 
            trades.price() < trades.previous().price &&
            s == State.BULL) {
            s = State.BULL_DECREASING;
        }
        else if (trades.current.trade.bids > trades.previous().bids && 
            trades.current.trade.trades > trades.previous().trades &&
            trades.delta3() > 0 &&
            (s == State.BEAR || s == State.BEAR_INCREASING)) {
            s = State.BEAR_INCREASING;
        }
    }

    enum State {
        BULL,
        BEAR,
        BULL_DECREASING,
        BEAR_INCREASING,
        BULLISH,
        BEARISH
    }

    public static void buy(Trade t) {
        contracts[cSize] = new Contract(t);
        cSize++;
        //System.out.println("NEW CONTRACT PURCHASE - " + trades.current.trade.price);
        open = true;
    }

    public static void sell(Trade t) {
        contracts[cSize - 1].setCPrice(t);
        //System.out.println("CONTRACT SOLD - " + trades.current.trade.price);
        if (contracts[cSize - 1].getProfit() > 0) {
            //System.out.println("PROFIT - " + ANSI_GREEN + "$" + contracts[cSize - 1].getProfit() + ANSI_RESET);
        }
        else if (contracts[cSize - 1].getProfit() == 0.0) {
            //System.out.println("PROFIT - " + ANSI_CYAN + "$" + contracts[cSize - 1].getProfit() + ANSI_RESET);
        }
        else {
            //System.out.println("PROFIT - " + ANSI_RED + "$" + contracts[cSize - 1].getProfit() + ANSI_RESET);
        }
        profitFees += contracts[cSize - 1].getProfitFees();
        profit += contracts[cSize - 1].getProfit();
        //System.out.println();
        open = false;
        maxProfit = Math.max(contracts[cSize - 1].getProfit(), maxProfit);
        maxLoss = Math.min(contracts[cSize - 1].getProfit(), maxLoss);

        if (contracts[cSize - 1].getProfit() == 0.0) {
            zero++;
        }
        else if (contracts[cSize - 1].getProfit() > 0) {
            successful++;
        }
        else if (contracts[cSize - 1].getProfit() < 0) {
            negative++;
        }
        p = 0;
    }

    public static Trade processString(String s) {
        Scanner scnr = new Scanner(s);
        scnr.useDelimiter(",");
        double bidPrice = scnr.nextDouble();
        double askPrice = scnr.nextDouble();
        double price = scnr.nextDouble();
        int bids = scnr.nextInt();
        int asks = scnr.nextInt();
        int volume = scnr.nextInt();
        int trades = scnr.nextInt();
        Trade t = new Trade(price, trades, asks, bids, askPrice, bidPrice, volume);
        //System.out.println("NEW TRADE - " + t.price + " - " + t.bids);
        scnr.close();
        return t;
    }

    public static void print() {
        ratio = ((double) successful / cSize) * 100;
        if (profit > 0) {
            System.out.println("\n\nGROSS PROFIT - " + ANSI_GREEN + "$" + profit + ANSI_RESET);
            System.out.println("TOTAL PROFIT - " + ANSI_GREEN + "$" + profitFees + ANSI_RESET);
        }
        else {
            System.out.println("\n\nGROSS PROFIT - " + ANSI_RED + "$" + profit + ANSI_RESET);
            System.out.println("TOTAL PROFIT - " + ANSI_RED + "$" + profitFees + ANSI_RESET);
        }
        System.out.println(ANSI_GREEN + "PROFITABLE - " + successful + ANSI_CYAN + 
            " | ZERO RETURN - " + zero + ANSI_RED + " | LOSS - " + negative + ANSI_RESET);
        System.out.println("MAX PROFIT - " + ANSI_GREEN + "$" + maxProfit + ANSI_RESET);
        System.out.println("MAX LOSS - " + ANSI_RED + "$" + maxLoss + ANSI_RESET);
        if (ratio >= 65) {
            System.out.println("SUCCESSFUL TRADES BY % - " + ANSI_GREEN + ratio + ANSI_RESET);
        }
        else if (ratio >= 50 && ratio < 65) {
            System.out.println("SUCCESSFUL TRADES BY % - " + ANSI_CYAN + ratio + ANSI_RESET);
        }
        else {
            System.out.println("SUCCESSFUL TRADES BY % - " + ANSI_RED + ratio + ANSI_RESET);
        }
        System.out.println("TOTAL CONTRACTS OPENED - " + cSize);
        System.out.println("TOTAL DATA ANALYZED - " + iterations);
        System.out.println("TOTAL TRADES CONSIDERED - " + numTrades);
        System.out.println("TOTAL TIME - null");
    }

    public static double currentPrice() {
        return contracts[cSize - 1].oPrice;
    }

    public static double currentProfit() {
        return trades.delta(contracts[cSize - 1].oPrice);
    }

    public static void progressBar() {
        String check = null;
        if (profit > 0) {
            check = ANSI_GREEN + "\u2713" + ANSI_RESET;
        }
        else {
            check = ANSI_RED + "x" + ANSI_RESET;
        }
        int b = (int) Math.ceil((double) currentLines / 40);
        int n = (int) Math.ceil(((double) lines / b));
        String s = "";
        for (int i = 0; i < n; i++) {
            s += "#";
        }
        for (int j = 0; j < 40 - n; j++) {
            s += "-";
        }
        System.out.print("[ " + ANSI_GREEN + s + ANSI_RESET + " ] " + profit + " | " + profitFees + " | " + cSize + " | " +
            lines + "   " + check + "\r");
    }

    public static void initialize(int i) {

        trades = new TradeList();

        uniqueTrades = new TradeList();

        contracts = new Contract[1500];

        cSize = 0;

        testFile = "test-files/esdata" + i + ".csv";

        open = false;

        p = 0;

        iterations = 12800;

        numTrades = 0;
        
        profit = 0;
        profitFees = 0;

        maxProfit = 0;
        maxLoss = 0;

        successful = 0;
        negative = 0;
        zero = 0;
        ratio = 0.0;

        testint = 0;

        lines = 0;
        
        currentLines = 0;
        File f = new File(testFile);
        Scanner m = null;
        try {
            m = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("file error");
        }
        while (m.hasNextLine()) {
            m.nextLine();
            currentLines += 1;
        }
        m.close();
        
    }
}
