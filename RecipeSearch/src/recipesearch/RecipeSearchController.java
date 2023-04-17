
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.image.WritableImage;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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

    @FXML
    private Text recipeDescription;

    @FXML
    private Text recipeInstruction;

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
        populateMainIngredientComboBox();
        populateCuisineComboBox();
        Platform.runLater(()->mainIngredient.requestFocus());



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
        recipeDescription.setText(recipe.getDescription());
        recipeInstruction.setText(recipe.getInstruction());
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


                    if (newValue != null && !newValue.equals(oldValue)
                            //&& !slideyFucker.isValueChanging()
                    ) {
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
    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }


    public Image getCuisineImage(String country) {
        String iconPath;
        switch(country){
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }

    public Image getDifficultyImage(String difficulty){
        String iconPath;
        switch(difficulty) {
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }

    public Image getMainIngridientImage(String ingridient){
        String iconPath;
        switch(ingridient) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_beef.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grönsaker":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredient.setButtonCell(cellFactory.call(null));
        mainIngredient.setCellFactory(cellFactory);
    }
    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        couisine.setButtonCell(cellFactory.call(null));
        couisine.setCellFactory(cellFactory);
    }

}