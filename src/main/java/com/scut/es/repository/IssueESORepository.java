package com.scut.es.repository;

import com.scut.es.eso.IssueESO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueESORepository extends ElasticsearchRepository<IssueESO, Long> {

    public List<IssueESO> findByTitle(String title);
    public Page<IssueESO> findByTitle(String title, Pageable pageable);
    //public IssueESO findByDescription(String description);


    public void deleteByTitle(String title);
    //public int  deleteByDescription(String description);
}
