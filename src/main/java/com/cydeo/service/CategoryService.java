package com.cydeo.service;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.entity.Company;

import java.util.List;

public interface CategoryService {

    public List<CategoryDTO> listAllCategories();

    CategoryDTO findById(long parseLong);

    CategoryDTO save(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void delete(Long categoryId);

    boolean isCategoryDescriptionUnique(CategoryDTO categoryDTO);

    //   List<CategoryDTO> listAllCategoriesByCompany();

}
