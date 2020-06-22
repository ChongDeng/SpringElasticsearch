package com.scut.es.controllers;

import com.scut.es.eso.IssueESO;
import com.scut.es.eso.SearchHistoryESO;
import com.scut.es.repository.SearchHistoryESORepository;
import com.scut.es.service.SearchHistoryESOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/searchHistory")
@RestController
public class SearchHistoryController {

    @Autowired
    SearchHistoryESOService searchHistoryESOService;

    @Autowired
    private SearchHistoryESORepository searchHistoryESORepository;

    @GetMapping("/dataCleanseV1")
    public String dataCleanseV1() {

        List<SearchHistoryESO> res = new ArrayList<>();
        searchHistoryESORepository.findAll().forEach(res::add);

        for(SearchHistoryESO searchHistoryESO : res)
        {
//            List<Long> values = searchHistoryESO.getAssignedOfCompanyIds();
//            if(values != null && values.size() > 0)
//            {
//                System.out.println("\nold data - \t");
//                values.forEach(item -> System.out.print(item + ";"));
//
//                List<Long> newValues = new ArrayList<>();
//                searchHistoryESO.setAssignedOfCompanyIds(newValues);
//
//                searchHistoryESORepository.save(searchHistoryESO);
//            }
//
//            List<Long> values = searchHistoryESO.getCreatorsOfCompanyIds();
//            if(values != null && values.size() > 0)
//            {
//                System.out.print("\nold data - \t");
//                values.forEach(item -> System.out.print(item + ";"));
//
//                List<Long> newValues = new ArrayList<>();
//                searchHistoryESO.setCreatorsOfCompanyIds(newValues);
//
//                searchHistoryESORepository.save(searchHistoryESO);
//            }
        }

        return "success";
    }

    @GetMapping("/dataCleanseV2")
    public String dataCleanseV2() {

        List<SearchHistoryESO> res = new ArrayList<>();
        searchHistoryESORepository.findAll().forEach(res::add);

        for(SearchHistoryESO searchHistoryESO : res)
        {
            String name = searchHistoryESO.getName();
            searchHistoryESO.setSearchHistoryName(name);
            searchHistoryESORepository.save(searchHistoryESO);
        }

        return "success";
    }

    @GetMapping("/getSearchHistoryByName")
    List<SearchHistoryESO> getSearchHistoryByName(@RequestParam(required = true) String searchName) {
        return searchHistoryESOService.getSearchHistoryByName(searchName);
    }

}
