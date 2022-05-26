package cvicenia.modifiersAukcia;

import cvicenia.modifiersAukcia.auction.Auction;
import cvicenia.modifiersAukcia.auction.Bid;

public class MainAukcia {

    public static void main(String[] args) {
       runAukciu();
    }

    public static void runAukciu() {
        Auction aukcia = new Auction();
        aukcia.startAuction();

        Bid b1 = Bid.builder().setBidderName("Hrac1").setPrice(10).build();
        aukcia.addBid(b1);

        Bid b2 = Bid.builder().setBidderName("Hrac2").setPrice(15).build();
        aukcia.addBid(b2);

        aukcia.stopAuction();

        System.out.println(aukcia.getAllBids());
        System.out.println("Najviac: "+aukcia.getHighestBid().get());
    }
}
