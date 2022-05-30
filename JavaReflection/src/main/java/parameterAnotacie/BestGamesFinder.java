package parameterAnotacie;

import parameterAnotacie.anotacie.Anotacie.*;
import parameterAnotacie.databaza.Databaza;

import java.util.*;

//s anotaciami je lepšie vidieť čo od čoho závisí a aké je poradie volania metod
public class BestGamesFinder {

    private Databaza database = new Databaza();

    @Operation("VsetkyHry")
    public Set<String> getAllGames() {
        return database.readAllGames();
    }

    @Operation("hryToCena")
    public Map<String, Float> getGameToPrice(@ZavisiNa("VsetkyHry") Set<String> allGames) {
        return database.readGameToPrice(allGames);
    }

    @Operation("hryToRating")
    public Map<String, Float> getGameToRating(@ZavisiNa("VsetkyHry")Set<String> allGames) {
        return database.readGameToRatings(allGames);
    }

    @Operation("skoreHier")
    public SortedMap<Double, String> scoreGames(@ZavisiNa("hryToCena") Map<String, Float> gameToPrice,
                                                @ZavisiNa("hryToRating") Map<String, Float> gameToRating) {
        SortedMap<Double, String> gameToScore = new TreeMap<>(Collections.reverseOrder());
        for (String gameName : gameToPrice.keySet()) {
            double score = (double) gameToRating.get(gameName) / gameToPrice.get(gameName);
            gameToScore.put(score, gameName);
        }

        return gameToScore;
    }

    @FinalResult
    public List<String> getTopGames(@ZavisiNa("skoreHier") SortedMap<Double, String> gameToScore) {
        return new ArrayList<>(gameToScore.values());
    }
}
