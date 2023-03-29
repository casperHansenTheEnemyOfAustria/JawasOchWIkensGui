
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML
    private FlowPane expandableRecipeList;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    private void updateRecipeList(){
        expandableRecipeList.getChildren().clear();
        List<Recipe> recipes = RecipeBackendController.getRecipes();
        for(int i =0; i > recipes.size(); i++ ){
            RecipeListitem listitem = new RecipeListitem(recipes.get(i), this );
            expandableRecipeList.getChildren().add(listitem);
        }
    }

}