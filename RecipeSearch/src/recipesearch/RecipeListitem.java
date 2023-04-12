package recipesearch;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.*;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.stage.Stage;


public class RecipeListitem extends AnchorPane {
    @FXML private ImageView recipeImage;
    @FXML private Text recipeTextHeadline;

    @FXML private ImageView cusineImageSmall;

    @FXML private ImageView mainingridientImage;

    @FXML private ImageView cuisineImage;

    @FXML private Text recipeDescription;

    @FXML private Label time;

    @FXML private Label price;

    private RecipeSearchController parentController;

    private Recipe recipe;

    public RecipeListitem(Recipe recipe, RecipeSearchController recipeSearchController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;
        recipeImage.setImage(recipe.getFXImage());
        recipeTextHeadline.setText(recipe.getName());
        cuisineImage.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        recipeDescription.setText(recipe.getDescription());
        time.setText(String.valueOf(recipe.getTime()) + " Minuter");
        price.setText(String.valueOf(recipe.getPrice()) + " kr");
        
    }


    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }



}
