package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.entity.Category;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<CategoryDTO> listAllCategories() {

        List<Category> categoriesList = categoryRepository.findAll();
        return categoriesList.stream().map(

                category ->
                        mapperUtil.convert(category, new CategoryDTO())).collect(Collectors.toList()
        );
    }

    @Override
    public CategoryDTO findById(long parseLong) {

        Optional<Category> category = categoryRepository.findById(parseLong);

        return mapperUtil.convert(category, new CategoryDTO());
    }


}