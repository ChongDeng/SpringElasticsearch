package com.scut.es.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.es.bo.AddIssueESBO;
import com.scut.es.bo.PageSizeRequest;
import com.scut.es.eso.IssueESO;
import com.scut.es.eso.SearchHistoryESO;
import com.scut.es.repository.IssueESORepository;
import com.scut.es.repository.SearchHistoryESORepository;
import com.scut.es.service.IssueESOService;
import com.scut.es.service.SearchHistoryESOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    IssueESOService issueESOService;

    @Autowired
    SearchHistoryESOService searchHistoryESOService;

    //========================   issue ========================

    //case 1 : insert by json
    @PostMapping("/insertByJson")
    //public String insertOrUpdateIssueES() throws JsonProcessingException {
    public String insertByJson() throws JsonProcessingException {
        return issueESOService.insertByJson();
    }

    //case 1.2 : insert/update by http post
    @PostMapping("/insertOrUpdateIssueES")
    String insertOrUpdateIssueES(@RequestBody AddIssueESBO addIssueESBO) {
        return issueESOService.insertOrUpdateIssueES(addIssueESBO);
    }

    //case 2: query list by keyword
    @GetMapping("/query")
    public List<IssueESO> getIssueList() {
        return issueESOService.getIssueList("test");
    }

    //case 2.2: getAll
    @GetMapping("/getAll")
    public List<IssueESO> getAll() {
        return issueESOService.getAll();
    }


    //case 2.3: query list by pageable
    @GetMapping("/search")
    public List<IssueESO> search(PageSizeRequest pageSizeRequest,
                                 @RequestParam(required = false)  String keyword)
    {
        return issueESOService.search(pageSizeRequest, keyword);
    }

    //case 2.4: advanced search
    @GetMapping("/advancedSearch")
    public List<IssueESO> advancedSearch(@RequestParam(required = false)  String keyword)
    {
        PageSizeRequest pageSizeRequest = new PageSizeRequest();
        pageSizeRequest.setPageNum(1);
        pageSizeRequest.setPageSize(10);

        return issueESOService.advancedSearch(pageSizeRequest, keyword);
    }

    //case 2.5: advanced search2
    @GetMapping("/advancedSearch2")
    public List<IssueESO> advancedSearch2(@RequestParam(required = false)  String keyword)
    {
        PageSizeRequest pageSizeRequest = new PageSizeRequest();
        pageSizeRequest.setPageNum(1);
        pageSizeRequest.setPageSize(10);

        return issueESOService.advancedSearch2(pageSizeRequest, keyword);
    }

    //case 3: delete by specific field
    @GetMapping("/deleteByTitle")
    public String deleteByTitle() {
        return issueESOService.deleteByTitle("chong");
    }

    //case 3.2: delete by id
    @GetMapping("/deleteById")
    public String deleteById() {
        return issueESOService.deleteById(261L);
    }

    //case 3.3: delete all


    //case 4: data clean
    @GetMapping("/dataClean")
    public String dataClean() {
        return issueESOService.dataClean();
    }

    //========================   search history ========================

    @GetMapping("/test2")
    public List<SearchHistoryESO> getSearchHistoryList() {
        List<SearchHistoryESO> res = searchHistoryESOService.getSearchHistoryList("项目");
        return res;
    }
}


