package services;

import dao.RecipeDao;
import entities.Medication;
import entities.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeServices {

    RecipeDao recipeDao = new RecipeDao();


    public void createRecipe(Recipe recipe) {
        List<Recipe> recipes = recipeDao.getAllInstance();
        List<String> recipeRec = new ArrayList<>();
        if (recipes.isEmpty()) {
            recipeDao.createInstance(recipe);
        } else {
            recipes.forEach(recipe1 -> recipeRec.add(recipe1.getRecommendations()));
            if (recipeRec.contains(recipe.getRecommendations())) {
                System.out.println("this recipe already exist");
            } else {
                recipeDao.createInstance(recipe);
            }
        }
    }


    public Recipe addMedicationsToRecipe(@NotNull Recipe recipe, List<Medication> newMedications) {
        recipeDao.addMedToRecipe(recipe, newMedications);
        return recipe;
    }

    public Recipe getRecipeById(int id) {
        return recipeDao.getInstanceById(id);
    }

    public void readAllRecipes() {
        List<Recipe> recipes = recipeDao.getAllInstance();
        for (Recipe recipe : recipes) {
            System.out.println("Recipe " + recipe.getRecommendations() + ", id = " + recipe.getId() + "\n");
            recipe.getMedications().forEach(System.out::println);
            System.out.println();
        }
    }
}
