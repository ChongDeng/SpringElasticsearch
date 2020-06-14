package com.scut.es.service;

import com.scut.es.eso.SearchHistoryESO;
import com.scut.es.repository.SearchHistoryESORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryESOService {

    @Autowired
    private SearchHistoryESORepository searchHistoryESORepository;

    public List<SearchHistoryESO> getSearchHistoryList(String Keyword) {
        List<SearchHistoryESO> res = searchHistoryESORepository.findByKeyword(Keyword);
        return res;
    }
}
