package com.example.searchservice.controller;

import com.example.common.util.Result;
import com.example.searchservice.service.SearchService;
import com.example.searchservice.vo.SearchParam;
import com.example.searchservice.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CJW
 * @since 2024/5/13
 */
@Slf4j
@RequestMapping("/search")
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/product/list")
    public Result<SearchResult> searchProduct(SearchParam searchParam) {
        System.out.println(searchParam);
        SearchResult searchResult = searchService.searchProduct(searchParam);
        return Result.success(searchResult);
    }
}
