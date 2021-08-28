package br.com.pratudo.recipe.service;

import br.com.pratudo.recipe.model.MethodOfPreparation;
import br.com.pratudo.recipe.model.Owner;
import br.com.pratudo.recipe.model.Recipe;
import br.com.pratudo.recipe.model.Time;
import br.com.pratudo.recipe.model.dto.RecipeDTO;
import br.com.pratudo.recipe.model.mapper.RecipeMapper;
import br.com.pratudo.recipe.repository.RecipeRepository;
import br.com.pratudo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeMapper recipeMapper;

    @Autowired
    SecurityUtils securityUtils;

    public Recipe createRecipe(final RecipeDTO recipeDTO) {
        recipeDTO.setOwner(buildInitialOwner());

        recipeDTO.setCreationDate(LocalDateTime.now());

        recipeDTO.setRate(BigDecimal.ZERO);

        recipeDTO.setRatings(Collections.emptyList());

        final MethodOfPreparation methodOfPreparation = recipeDTO.getMethodOfPreparation();
        methodOfPreparation.setTime(new Time());
        final Time totalMethodOfPreparationTime = methodOfPreparation.getTime();
        totalMethodOfPreparationTime.setTimeUnit(TimeUnit.MINUTES);
        totalMethodOfPreparationTime.setValue(methodOfPreparation.getTotalMinutesInMethodOfPreparation());

        recipeDTO.setComments(Collections.emptyList());

        return recipeRepository.save(recipeMapper.convertRecipeDTOToRecipe(recipeDTO));
    }

    private Owner buildInitialOwner() {
        return Owner.builder()
                ._id(securityUtils.getCurrent_Id())
                .name(securityUtils.getCurrentName())
                .build();
    }

    public Page<Recipe> getRecipesOrderByCreationDateDesc(final Pageable pageable) {
        return recipeRepository.findAllByOrderByCreationDateDesc(pageable);
    }

    public Page<Recipe> getRecipesByIngredients(final List<String> ingredients, final Pageable pageable) {
        return recipeRepository.findByIngredients(convertListToString(ingredients), pageable);
    }

    public Page<Recipe> getRecipesByName(final String name, final Pageable pageable) {
        return recipeRepository.findByName(name, pageable);
    }

    public Page<Recipe> getRecipesByTag(List<String> tags, Pageable pageable) {
        return recipeRepository.findByTagsContains(convertListToString(tags), pageable);
    }

    private String convertListToString(List<String> ingredients) {
        return ingredients.toString().replace("[", "").replace("]", "");
    }

}
