package com.scut.es.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.es.bo.AddIssueESBO;
import com.scut.es.bo.PageSizeRequest;
import com.scut.es.eso.IssueESO;
import com.scut.es.repository.IssueESORepository;
import com.scut.es.repository.SearchHistoryESORepository;
import com.scut.es.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueESOService {

    @Autowired
    private IssueESORepository issueESORepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchTemplate;

    //case 1 : insert by json
    public String insertByJson() throws JsonProcessingException {

        String jsonStr = "{\n" +
                "    \"id\": 777,\n" +  //注：因为IssueESO类中确实有id成员！
                "    \"componentPartId\": 18,\n" +
                "    \"assignedToId\": 27,\n" +
                "    \"issueTypeId\": 1,\n" +
                "    \"issueTypeVal\": null,\n" +
                "    \"title\": \"chong tesla musk\",\n" +
                "    \"description\": \"<p>1、附件没有添加入口</p><p><br></p><p>2、在下部评论对话框内，添加图片，没有显示图片，也没有添加失败提示，点击确定，出现一条空评论</p><p><br></p><img class=\\\"attachment\\\" src=\\\"http://1.1.1.1/test.png\\\" title=\\\"3.png\\\" data-type=\\\"1\\\" data-src=\\\"\\\"><p><br></p>\",\n" +
                "    \"creator\": 51,\n" +
                "    \"priority\": 1,\n" +
                "    \"priorityStr\": \"HIGHEST\",\n" +
                "    \"status\": 14,\n" +
                "    \"statusStr\": null,\n" +
                "    \"severity\": 2,\n" +
                "    \"severityStr\": \"MAJOR\",\n" +
                "    \"isClose\": false,\n" +
                "    \"internalOnly\": false,\n" +
                "    \"createdBy\": \"chaojun\",\n" +
                "    \"created\": 1587698952000,\n" +
                "    \"modifiedBy\": \"james\",\n" +
                "    \"modified\": 1587958372790,\n" +
                "    \"projectId\": 12,\n" +
                "    \"projectTicketNo\": \"22\",\n" +
                "    \"isDelete\": false,\n" +
                "    \"assignedToUserName\": null,\n" +
                "    \"projectName\": null,\n" +
                "    \"componentPart\": null,\n" +
                "    \"systemBoard\": null,\n" +
                "    \"issueId\": 37,\n" +
                "    \"systemBoardIdList\": null\n" +
                "  }";

        ObjectMapper mapper = new ObjectMapper();
        IssueESO issueESO = mapper.readValue(jsonStr, IssueESO.class);

        issueESORepository.save(issueESO);
        return "success";
    }

    //case 1.2 : insert/update by http post
    public String insertOrUpdateIssueES(AddIssueESBO addIssueESBO)
    {
        IssueESO issueESO  = BeanUtils.convert(addIssueESBO, IssueESO.class);
        issueESORepository.save(issueESO);

        return "success";
    }


    //case 2: query list by keyword
    public List<IssueESO> getIssueList(String keyword)
    {
        List<IssueESO> res = issueESORepository.findByTitle(keyword);
        return res;
    }

    //case 2.2: getAll
    public List<IssueESO> getAll()
    {
        //方法1
//        List<IssueESO> res = new ArrayList<>();
//        for(IssueESO issueESO : issueESORepository.findAll())
//        {
//            res.add(issueESO);
//        }

        //方法2
        List<IssueESO> res = new ArrayList<>();
        issueESORepository.findAll().forEach(res::add);
        return res;
    }


    //case 2.3: query list by pageable
    public List<IssueESO> searchWithPage(PageSizeRequest pageSizeRequest, String keyword) {
        //方法1: 不带排序
        //Pageable pageable = PageRequest.of(1, 10);

        //方法2: 带排序： 根据id降序来排序
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<IssueESO> issues = issueESORepository.findByTitle(keyword, pageable);
        String content = issues.getContent().toString();
        List<IssueESO> res = issues.getContent();
        return res;
    }

    //case 2.4: advancedSearch
    public List<IssueESO> advancedSearch(PageSizeRequest pageSizeRequest, String keyword) {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));


        //case 1
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchQuery("title", keyword));

        //case 2: 查询多个字段： 例如搜索"stuck"
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .must(QueryBuilders.multiMatchQuery( keyword,"title", "description"));

        //case 3: wildcard query: 中文keyword只能是一个字，英文keyword只能是一个单词或者单词的一部分
        //BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        //        .must(QueryBuilders.wildcardQuery("title", "*" + keyword + "*"));

        //case 4: 例如搜索“是一个”，或者英文单词（或者英文单词的一部分），但不能是“o run str”或者“to run stress”这种
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.wildcardQuery("title.keyword", "*" + keyword + "*"));

        Page<IssueESO> issues = issueESORepository.search(queryBuilder, pageable);
        String content = issues.getContent().toString();
        List<IssueESO> res = issues.getContent();
        return res;
    }

    //case 2.5: advancedSearch2
    public List<IssueESO> advancedSearch2(PageSizeRequest pageSizeRequest, String keyword) {

        // case 1： match query
//        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", keyword);
//        Page<IssueESO> issues = (Page<IssueESO>) issueESORepository.search(queryBuilder);
//        List<IssueESO> res = issues.getContent();

        // case 2: term query:  不好使，例如搜索“是”
//        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title.keyword", keyword);
//        Page<IssueESO> issues = (Page<IssueESO>) issueESORepository.search(queryBuilder);
//        List<IssueESO> res = issues.getContent();


        //case 3: 比较完美的查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchPhraseQuery("title", keyword).slop(50));
        queryBuilder.withPageable(PageRequest.of(0, 10));
        Page<IssueESO> items =  issueESORepository.search(queryBuilder.build());
        List<IssueESO> res = items.getContent();

        //case 4: filters
        BoolQueryBuilder re = QueryBuilders.boolQuery();
        setAdvancedSearchFilters(re);

        List<String> matchFields = setMatchFields();

        SortBuilder sortBuilder = SortBuilders.fieldSort("modified").order(SortOrder.DESC);

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<IssueESO> issues = superSearch(IssueESO.class, keyword, matchFields, Arrays.asList(re), sortBuilder, pageRequest);

        return issues.getContent();
    }

    //case 2.6 queryNull
    public List<IssueESO> queryNull(String keyword, boolean isNull, int pageNumber, int pageSize)
    {

        BoolQueryBuilder re = QueryBuilders.boolQuery();

        BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
        if(isNull == true){
            subQuery.mustNot(QueryBuilders.existsQuery("assignedToId"));
        }
        else{
            subQuery.must(QueryBuilders.existsQuery("assignedToId"));
        }
        re.must(subQuery);

        List<String> matchFields = setMatchFields();

        SortBuilder sortBuilder = SortBuilders.fieldSort("modified").order(SortOrder.DESC);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<IssueESO> issues = superSearch(IssueESO.class, keyword, matchFields, Arrays.asList(re), sortBuilder, pageRequest);

        return issues.getContent();
    }


    //case 3: delete by specific field
    public String deleteByTitle(String keyword)
    {
        issueESORepository.deleteByTitle(keyword);
        return "success";
    }

    //case 3.2: delete by id
    public String deleteById(Long id) {
        issueESORepository.deleteById(id);
        return "success";
    }

//    // case 3.3 batch delete
//    public String deleteAll(List<Long> ids) {
//        issueESORepository.deleteAll(ids);
//        return "success";
//    }

    //case 4: data clean
    public String dataClean()
    {
        List<IssueESO> res = new ArrayList<>();
        issueESORepository.findAll().forEach(res::add);

        for(IssueESO issueESO : res)
        {
            List<Long> values = issueESO.getSystemBoardIdList();
            if(values == null || values.size() == 0)
            {
                List<Long> ids = new ArrayList<>(); ids.add(3L);
                issueESO.setSystemBoardIdList(ids);
                issueESORepository.save(issueESO);
            }
        }

        return "success";
    }


    private void setAdvancedSearchFilters(BoolQueryBuilder re)
    {
        try
        {
//           re.must(QueryBuilders.termQuery("isDelete", false));

//            //Step 1: projectIds sub query
//            List<Long> projectIds = Arrays.asList(1L, 2L, 3L);
//            if(projectIds != null && projectIds.size() > 0)
//            {
//                BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//                for(Long projectId : projectIds)
//                {
//                    subQuery.should(QueryBuilders.termQuery("projectId", projectId));
//                }
//                re.must(subQuery);
//            }
//
//            //step 2: createdDaysRange sub query
//            Long endTimeStamp = new Date().getTime();
//            int createdDaysRange = 1;
//            Long startTimeStamp = endTimeStamp - (createdDaysRange + 1) * 24 * 3600 * 1000L;
//            re.must(QueryBuilders.rangeQuery("created").gte(startTimeStamp).lte(endTimeStamp));
//
//
//            //step 3: isClose sub query
//            BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//            subQuery.should(QueryBuilders.termQuery("isClose", false));
//            re.must(subQuery);

              //step 13: issueCustomFields sub query
//            List<List<Long>> customFieldItemIdsList = advancedSearchEntity.getCustomFieldItemIds();
//            if(customFieldItemIdsList != null && customFieldItemIdsList.size() > 0)
//            {
//                BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//
//                for(List<Long> customFieldItemIds : customFieldItemIdsList)
//                {
//                    if(customFieldItemIds != null && customFieldItemIds.size() > 0)
//                    {
//                        BoolQueryBuilder fieldItemQuery = QueryBuilders.boolQuery();
//
//                        for(Long customFieldItemId : customFieldItemIds)
//                        {
//                            BoolQueryBuilder valuesQuery = QueryBuilders.boolQuery();
//                            valuesQuery.should(QueryBuilders.termQuery("customFields.value1", customFieldItemId));
//                            valuesQuery.should(QueryBuilders.termQuery("customFields.value4", customFieldItemId));
//
//                            QueryBuilder builder = QueryBuilders.nestedQuery("customFields",
//                                    QueryBuilders.boolQuery().
//                                            must(valuesQuery),
//                                    ScoreMode.Total);
//
//                            fieldItemQuery.should(builder); //'or' logic
//                        }
//
//                        subQuery.must(fieldItemQuery);  //'and' logic
//                    }
//                }
//
//                re.must(subQuery);
//            }

            //step 13: issueCustomFields sub query
//                List<Long> customFieldItemIds = advancedSearchEntity.getCustomFieldItemIds();
//                if(customFieldItemIds != null && customFieldItemIds.size() > 0)
//                {
//                    for(Long customFieldItemId : customFieldItemIds)
//                    {
//                        BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//
//                        BoolQueryBuilder valuesQuery = QueryBuilders.boolQuery();
//                        valuesQuery.should(QueryBuilders.termQuery("customFields.value1", customFieldItemId));
//                        valuesQuery.should(QueryBuilders.termQuery("customFields.value4", customFieldItemId));
//
//                        QueryBuilder builder = QueryBuilders.nestedQuery("customFields",
//                                QueryBuilders.boolQuery().
//                                        must(valuesQuery),
//                                ScoreMode.Total);
//
//                        subQuery.must(builder);
//
//                        re.must(subQuery);
//                    }
//                }


            //step 13: issueCustomFields sub query
//                List<Long> customFieldItemIds = advancedSearchEntity.getCustomFieldItemIds();
//                if(customFieldItemIds != null && customFieldItemIds.size() > 0)
//                {
//                    BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//
//                    for(Long customFieldItemId : customFieldItemIds)
//                    {
//                        BoolQueryBuilder valuesQuery = QueryBuilders.boolQuery();
//                        valuesQuery.should(QueryBuilders.termQuery("customFields.value1", customFieldItemId));
//                        valuesQuery.should(QueryBuilders.termQuery("customFields.value4", customFieldItemId));
//
//                        QueryBuilder builder = QueryBuilders.nestedQuery("customFields",
//                                QueryBuilders.boolQuery().
//                                        must(valuesQuery),
//                                ScoreMode.Total);
//
//                        subQuery.should(builder);
//                    }
//
//                    re.must(subQuery);
//                }

            //step 13: issueCustomFields sub query
//                List<CustomField> customFields = advancedSearchEntity.getCustomFields();
//                if(customFields != null && customFields.size() > 0)
//                {
//                    BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
//
//                    for(CustomField customField : customFields)
//                    {
//                        List<String> values =  customField.getFieldValues();
//                        if(values != null && values.size() > 0)
//                        {
//                            BoolQueryBuilder valuesQuery = QueryBuilders.boolQuery();
//                            for(String value: values)
//                            {
//                                valuesQuery.should(QueryBuilders.termQuery("customFields.fieldValues.keyword", value));
//                            }
//
//                            TermQueryBuilder nameQuery = QueryBuilders.termQuery("customFields.fieldName.keyword", customField.getFieldName());
//
//                            QueryBuilder builder = QueryBuilders.nestedQuery("customFields",
//                                    QueryBuilders.boolQuery().
//                                            must(valuesQuery).
//                                            must(nameQuery),
//                                    ScoreMode.Total);
//
//                            subQuery.should(builder);
//                        }
//                    }
//
//                    re.must(subQuery);
//                }

        }
        catch(Exception ex)
        {
            System.out.println("setAdvancedSearchFilters exception: " + ex.getMessage());
        }
    }

    private List<String> setMatchFields()
    {
        List<String> matchFields = Arrays.asList("title", "description", "projectTicketNo");
        return matchFields;
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
