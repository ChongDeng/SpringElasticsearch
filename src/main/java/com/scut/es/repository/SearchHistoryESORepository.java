package com.scut.es.repository;

import com.scut.es.eso.IssueESO;
import com.scut.es.eso.SearchHistoryESO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryESORepository extends ElasticsearchRepository<SearchHistoryESO, String> {
    public List<SearchHistoryESO> findByKeyword(String keyword);
}
