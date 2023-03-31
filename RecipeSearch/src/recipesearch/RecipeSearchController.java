
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML
    private FlowPane expandableRecipeList;
    @FXML
    private ComboBox mainIngredient;
    @FXML
    private ComboBox couisine;
    @FXML
    private RadioButton hard;
    @FXML
    private RadioButton medium;
    @FXML
    private RadioButton easy;
    @FXML
    private RadioButton all;
    @FXML
    private Spinner maxPrice;
    @FXML
    private Slider slideyFucker;
    @FXML
    private Text slideyFuckerLabel;
    @FXML
    private ImageView detailedImage;
    @FXML
    private Label detailedLabel;
    @FXML
    private AnchorPane searchPane;

    private Map<String, RecipeListitem> recipeListItemMap = new HashMap<String, RecipeListitem>();





    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRecipeHash();
        updateRecipeList();
        initMainCombobox();
        initCuisineCombobox();
        initToggles();
        initSpinner();
        sliderInit();



    }

    private void initRecipeHash() {
        System.out.println("get his");
        for (Recipe recipe : RecipeBackendController.getRecipes()) {
            RecipeListitem recipeListItem = new RecipeListitem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }
    }

    private void populateRecipeDetailView(Recipe recipe){
        detailedLabel.setText(recipe.getName());
        detailedImage.setImage(recipe.getFXImage());
    }
    @FXML
    public void closeRecipeView(){
        searchPane.toBack();
    }
    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        searchPane.toFront();
    }


    private void sliderInit() {
        slideyFucker.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() % 10 == 0) {


                    if (newValue != null && !newValue.equals(oldValue) ) {
//                        System.out.println("hello2");
                        RecipeBackendController.setMaxTime((Integer) newValue.intValue());
                        updateRecipeList();
                    }
                    newValue = newValue.intValue();
                    slideyFuckerLabel.setText(newValue.toString()+ " Minuter");
                    System.out.println(newValue);
                }

            }


        });
    }

    private void initSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0, 1);
        maxPrice.setValueFactory(valueFactory);
        maxPrice.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

                RecipeBackendController.setMaxPrice(newValue);
                updateRecipeList();
            }


        });

        maxPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                    RecipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });
    }

    private void initToggles() {
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        RadioButton[] rButtons = {all, easy, medium, hard};
        for(int i = 0; i < rButtons.length; i++){
            rButtons[i].setToggleGroup(difficultyToggleGroup);
        }
        all.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    System.out.println(selected.getText());
                    RecipeBackendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });
    }

    private void initCuisineCombobox() {
        couisine.getItems().addAll("Visa alla", "Sverige", "Grekland","Indien","Asien", "Afrika", "Frankrike");
        couisine.getSelectionModel().select("Visa alla");
        couisine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                RecipeBackendController.setCuisine(newValue);
                updateRecipeList();
            }
        });
    }

    private void initMainCombobox() {
        mainIngredient.getItems().addAll("Visa alla", "Kött",
                "Fisk",
                "Kyckling",
                "Vegetarisk");
        mainIngredient.getSelectionModel().select("Visa alla");
        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                RecipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
    }

    private void updateRecipeList(){
        expandableRecipeList.getChildren().clear();
        List<Recipe> recipes = RecipeBackendController.getRecipes();
        for(int i =0; i < recipes.size(); i++ ){
//            System.out.println(recipeListItemMap);
            RecipeListitem listitem = recipeListItemMap.get(recipes.get(i).getName());
            expandableRecipeList.getChildren().add(listitem);
        }


    }

}