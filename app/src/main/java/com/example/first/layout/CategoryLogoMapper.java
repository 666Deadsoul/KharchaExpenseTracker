package com.example.first.layout;

import com.example.first.R;
import java.util.HashMap;

//to map the category along with its name and logo
public class CategoryLogoMapper {
    private HashMap<String, Integer> categoryLogos;
    public CategoryLogoMapper() {
        categoryLogos = new HashMap<>();
        categoryLogos.put("Food", R.drawable.food);
        categoryLogos.put("Entertainment", R.drawable.entertainment);
        categoryLogos.put("Transport", R.drawable.transport);
        categoryLogos.put("Healthcare", R.drawable.healthcare);
        categoryLogos.put("Housing", R.drawable.house);
    }

    public Integer getLogoResource(String categoryName) {

        return categoryLogos.get(categoryName);
    }
}