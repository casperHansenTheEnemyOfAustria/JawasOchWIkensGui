package recipesearch;

import se.chalmers.ait.dat215.lab2.*;
import java.util.List;
import java.util.Arrays;



public class RecipeBackendController {
    private static RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private static String cuisine = null;
    private static String mainIngredient = null;
    private static String difficulty = null;
    private static int maxPrice = 0;
    private static int maxTime = 0;

    public static List<Recipe> getRecipes(){
        return db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
    }
    public void setCuisine(String cuisine){
        List<String> check = Arrays.asList("Sverige", "Grekland","Indien","Asien", "Afrika", "Frankrike");
        if(check.contains(cuisine)) {
            this.cuisine = cuisine;
        }
    }
    public void setMainIngredient(String mainIngredient){
        List<String> check = Arrays.asList("Kött",
                "Fisk",
                "Kyckling",
                "Vegetarisk");
        if(check.contains(mainIngredient)) {
            this.mainIngredient = mainIngredient;
        }
    }
    public void setDifficulty(String difficulty){
        List<String> check = Arrays.asList("Lätt",
                "Mellan",
                "Svår");
        if(check.contains(mainIngredient)) {
            this.difficulty = difficulty;
        }
    }
    public void setMaxPrice(int maxPrice){
        if(maxPrice > 0) {
            this.maxPrice = maxPrice;
        }
    }
    public void setMaxTime(int maxTime){
        List<Integer> check = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150);
        if(check.contains(maxTime)) {
            this.maxTime = maxTime;
        }
    }
}
