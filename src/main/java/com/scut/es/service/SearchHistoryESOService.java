package com.scut.es.service;

import com.scut.es.eso.IssueESO;
import com.scut.es.eso.SearchHistoryESO;
import com.scut.es.repository.SearchHistoryESORepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchHistoryESOService {

    @Autowired
    private SearchHistoryESORepository searchHistoryESORepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchTemplate;

    public List<SearchHistoryESO> getSearchHistoryList(String Keyword) {
        List<SearchHistoryESO> res = searchHistoryESORepository.findByKeyword(Keyword);
        return res;
    }


    public List<SearchHistoryESO> getSearchHistoryByName(String searchName)
    {
        System.out.println("======= getSearchHistoryByName  -  searchName : " + searchName);

        try
        {

            BoolQueryBuilder re = QueryBuilders.boolQuery();

//            UserDTO userDTO = checkService.getUserByUserId(Long.valueOf(httpServletRequest.getAttribute(Constants.TRACK_TOKEN_USER_ID).toString()));
//            re.must(QueryBuilders.termQuery("userId", userDTO.getId()));

            List<String> matchFields = Arrays.asList("searchHistoryName");

            SortBuilder sortBuilder = SortBuilders.fieldSort("modified").order(SortOrder.DESC);

            PageRequest pageRequest = null;

            Page<SearchHistoryESO> searchHistoryESOS = superSearch(SearchHistoryESO.class, searchName, matchFields, Arrays.asList(re), sortBuilder, pageRequest);
            System.out.println("======= getSearchHistoryByName  -  get search result count : " + searchHistoryESOS.getContent().size());
            return searchHistoryESOS.getContent();

        }
        catch (Exception ex)
        {
            System.out.println("===== getSearchHistoryByName exception - " + ex.getMessage());
        }

        return new ArrayList<SearchHistoryESO>();
    }

    private <ESO> Page<ESO> superSearch(Class<ESO> clazz, String keyword, List<String> matchFields, List<QueryBuilder> filters, SortBuilder sort, Pageable pageable)
    {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withSearchType(SearchType.DFS_QUERY_THEN_FETCH);


        //step 1: 设置matchFields和fields
        DisMaxQueryBuilder disMaxQueryBuilder = null;

        if (StringUtils.isNotEmpty(keyword) && !CollectionUtils.isEmpty(matchFields)){

            disMaxQueryBuilder = QueryBuilders.disMaxQuery();

            List<BoolQueryBuilder> fieldQueries = matchFields.stream().map(field ->
                    {
                        BoolQueryBuilder re = QueryBuilders
                                .boolQuery()
                                .must(QueryBuilders.matchPhrasePrefixQuery(field, keyword).slop(50));

                        if (filters != null)
                            re.filter().addAll(filters);

                        return re;
                    }
            ).collect(Collectors.toList());

            disMaxQueryBuilder
                    .innerQueries()
                    .addAll(fieldQueries);
        }

        if (disMaxQueryBuilder != null)
        {
            nativeSearchQueryBuilder.withQuery(disMaxQueryBuilder);
        }
        else
        {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (filters != null)
                boolQueryBuilder.filter().addAll(filters);
            boolQueryBuilder.should(QueryBuilders.matchAllQuery());

            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        }

        //step2: 设置pageable
        if (pageable != null) {
            nativeSearchQueryBuilder.withPageable(pageable);
        }

        //step3: 设置sort
        if (sort != null) {
            nativeSearchQueryBuilder.withSort(sort);
        }

        String indexName = clazz.getAnnotation(Document.class).indexName();
        IndexCoordinates index = IndexCoordinates.of(indexName);

        return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), clazz, index);

    }
}
