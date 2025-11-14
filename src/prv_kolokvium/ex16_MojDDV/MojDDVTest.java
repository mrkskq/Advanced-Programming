package prv_kolokvium.ex16_MojDDV;

import java.io.*;
import java.util.*;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String msg) {
        super(msg);
    }
}

class Receipt{
    private String id;
    private ArrayList<Integer> prices;
    private ArrayList<Double> pricesWithTaxes;
    private int totalPrice;

    public Receipt(){
        prices = new ArrayList<>();
        pricesWithTaxes = new ArrayList<>();
        totalPrice = 0;
    }

    public Receipt(String id, ArrayList<Integer> prices) {
        this.id = id;
        this.prices = prices;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPricesWithTaxes(ArrayList<Double> pricesWithTaxes) {
        this.pricesWithTaxes = pricesWithTaxes;
    }

    public String getId() {
        return id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public ArrayList<Double> getPricesWithTaxes() {
        return pricesWithTaxes;
    }
}

class MojDDV{
    private List<Receipt> receipts;

    public MojDDV(){
        receipts = new LinkedList<Receipt>();
    }

    private double calculatePriceWithTax(int amount, char type){
        if (type == 'A'){
            return amount * 0.18;
        }
        else if (type == 'B'){
            return amount * 0.05;}
        else {
            return 0;
        }
    }

    public void readRecords(InputStream in) throws IOException, AmountNotAllowedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }

            String[] parts = line.split("\\s+");
            String id = parts[0];
            ArrayList<Integer> prices = new ArrayList<>();
            ArrayList<Double> pricesWithTaxes = new ArrayList<>();

            for (int i = 1; i < parts.length; i+=2) {
                int price = Integer.parseInt(parts[i]);
                char type = parts[i+1].charAt(0);
                prices.add(price);

                double priceWithTax = calculatePriceWithTax(price, type);
                pricesWithTaxes.add(priceWithTax);
            }

            int totalPrice = prices.stream().mapToInt(p -> p).sum();
            try {
                Receipt receipt = new Receipt(id, prices);
                if (totalPrice > 30000){
                    throw new AmountNotAllowedException("Receipt with amount "+totalPrice+" is not allowed to be scanned");
                }
                receipt.setTotalPrice(totalPrice);
                receipts.add(receipt);
                receipt.setPricesWithTaxes(pricesWithTaxes);
            }
            catch (AmountNotAllowedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        for (Receipt receipt : receipts) {
            double totalPriceWithTax = receipt.getPricesWithTaxes().stream().mapToDouble(p -> p * 0.15).sum();
            System.out.printf("%s %s %.2f%n", receipt.getId(), receipt.getTotalPrice(), totalPriceWithTax);
        }
    }
}

public class MojDDVTest {

    public static void main(String[] args) throws AmountNotAllowedException, IOException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
