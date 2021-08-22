package br.com.pratudo.recipe.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElasticsearchRecipe {

    @JsonProperty("hits")
    private RecipeHits recipeHits;
}
