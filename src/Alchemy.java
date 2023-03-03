import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Alchemy {

    public static int dayStart = 1;
    public static int days = 10;

    public static double localMaxProfit = 0;

    public static int day;

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

    public static String st = "";

    public static String rainbow = "";

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
        for (int n = dayStart; n <= dayStart + days; n++) {
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
            for (int i = 0; i < 50; i++) {
                String s = scnr.nextLine();
                Trade t = processString(s);
                trades.add(t);
                if (t.price != trades.previous().price) {
                    uniqueTrades.add(t);
                }
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
            print();
        }
        System.out.println();
        System.out.print("\n\n\n");
        System.out.print("GLOBAL PROFIT OVER " + days + " DAYS: " + globalProfit + " | " + globalProfitFees + "\n\n\n");
    }

    public static void analyze() {
        checkState();
        double p20 = trades.previous20();
        double average = (p20 + trades.current.trade.price) / 2;
        if (!open) {
            if (
        
            trades.current.trade.bids > trades.current.trade.asks * 2 &&
            uniqueTrades.current.trade.price > uniqueTrades.previous().price &&
            trades.current.trade.bidPrice > uniqueTrades.previous().price &&
            trades.current.trade.askPrice > uniqueTrades.previous().price &&
            trades.current.trade.trades == 1 &&
            trades.current.trade.bidPrice == trades.current.trade.price &&
            average - trades.current.trade.price > 0 &&
            p20 - trades.current.trade.price > 0.5
    
                ) {
                buy(trades.current.trade);
            }
        }
        else {
            p++;
            if (currentProfit() > localMaxProfit) {
                localMaxProfit = currentProfit();
            }
            if (
                trades.current.trade.bids < trades.previous().bids &&
                trades.current.trade.asks > trades.current.trade.bids &&
                trades.current.trade.asks > trades.previous().asks &&
                trades.current.trade.trades == 1 &&
                p20 - trades.current.trade.price < -0.25 &&
                p > 10 ||
                currentProfit() < -1
                ) {
                sell(trades.current.trade);
                p = 0;
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
        String s = "";
        int b = (int) Math.ceil((double) currentLines / 40);
        int n = (int) Math.ceil(((double) lines / b));
        for (int i = 0; i < n; i++) {
            s += "#";
        }
        for (int j = 0; j < 40 - n; j++) {
            s += "-";
        }
        String f = "";
        s = "[ " + ANSI_GREEN + s + ANSI_RESET + " ] ";
        f =  profit + " | " + profitFees + " | " + cSize + " | " + lines + "   " + check;
        st = s + f + "\n";
        //s = s + f;
        //System.out.print(s + "\r");
        
        String tp = "";
        if (profit < 0) {
            tp = ANSI_RED;
        }
        else if (profit > 0 && profit <= 500) {
            tp = ANSI_CYAN;
        }
        else if (profit > 500 && profit <= 1000) {
            tp = ANSI_YELLOW;
        }
        else if (profit > 1000 && profit <= 5000) {
            tp = ANSI_GREEN;
        }
        else if (profit > 5000) {
            tp = ANSI_PURPLE;
        }
        String tp1 = "";
        if (profitFees < 0) {
            tp1 = ANSI_RED;
        }
        else if (profitFees > 0 && profit <= 500) {
            tp1 = ANSI_CYAN;
        }
        else if (profitFees > 500 && profit <= 1000) {
            tp1 = ANSI_YELLOW;
        }
        else if (profitFees > 1000 && profit <= 5000) {
            tp1 = ANSI_GREEN;
        }
        else if (profitFees > 5000) {
            tp1 = ANSI_PURPLE;
        }
        String r = ANSI_RESET;
        
        
        rainbow = rainbow + profitFees + " ";
        //System.out.print(rainbow + "\n\n\n");
        //System.out.println("\n");
        System.out.print(s);
        System.out.format(
            "[ " + tp + "%7.2f" + r + " ][ " + tp1 + "%7.2f" + r + " ][ " + "%7d" + " ][ " + "%7d" + " ][ " + check + " ][ " + day + " / " + days + " ]" + "\r", profit, profitFees, cSize, lines);
        /*
        if (rainbow.length() > 150) {
            rainbow = rainbow.substring(rainbow.length() - 5);
        }
        */
        //String.format(tp + "%.350s" + ANSI_RESET + "\r", rainbow);
        //System.out.print(tp + rainbow + ANSI_RESET + "\r");
        //System.out.format(st + "%08.2d" + " | " + "%08.2d" + " | " + "%08.2d" + " | " + "%08.2d" + " | " + "%08.2d" + check + "\r", profit, profitFees, cSize, lines);
    }

    public static void initialize(int i) {

        day = i;

        trades = new TradeList();

        uniqueTrades = new TradeList();

        contracts = new Contract[1500];

        cSize = 0;

        testFile = "Alchemy/test-files/esdata" + i + ".csv";

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
