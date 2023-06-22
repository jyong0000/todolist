package com.example.todolist.controller;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.todolist.dto.TodoList;
import com.example.todolist.service.TodoListService;
import com.example.todolist.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TodoListController {

	@Autowired
	private TodoListService todolistService;
	
	@Autowired
	MyUtil myUtil;
	
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}

	@RequestMapping(value ="/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}
	
	@RequestMapping(value = "/created", method = RequestMethod.POST)
	public String createdOk(TodoList todolist, HttpServletRequest request, Model model) {
	 try {
		int maxNum = todolistService.maxNum();
		
		todolist.setNum(maxNum +1);
		
		todolistService.insertDate(todolist);
		
	} catch (Exception e) {
		e.printStackTrace();
		return "bbs/created";
	}
		return "redirect:/list";
	}
	
	@RequestMapping(value ="/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(TodoList todolist,  HttpServletRequest request, Model model) {
		try {
			String pageNum = request.getParameter("pageNum");
			int currentPage = 1;
			
			if(pageNum != null) currentPage = Integer.parseInt(pageNum);
			
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue == null) {
				searchKey = "subject";
				searchValue = "";
			} else {
				if(request.getMethod().equalsIgnoreCase("GET")) {
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
			}
			int dataCount = todolistService.getDataCount(searchKey, searchValue);
			
			int numPerPage = 5;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			if(currentPage > totalPage) currentPage = totalPage;
			
			int start = (currentPage - 1) * numPerPage +1;
			int end = currentPage * numPerPage;
			
			List<TodoList> lists = todolistService.getLists(searchKey, searchValue, start, end);
			
			String param = "";
				
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param = "searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			String listUrl = "/list";
			
			if(!param.equals("")) listUrl += "?" + param;
			
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			
			String articleUrl = "/article?pageNum=" + currentPage;
			
			if(!param.equals("")) {
				articleUrl += "&" + param;
				//article?pageNum=1&searchKey=subject&searchValue=춘식
			}
			model.addAttribute("lists", lists);	//전체게시물
			model.addAttribute("articleUrl", articleUrl);	
			model.addAttribute("pageIndexList", pageIndexList);	
			model.addAttribute("dataCount", dataCount); 
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "리스트를 불러오는 중 에러가 발생했습니다.");
		}
		return "bbs/list"; 

	}
	
	@RequestMapping(value= "/article", method = RequestMethod.GET)
	public String article(HttpServletRequest request, Model model) {
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			TodoList todolist = todolistService.getReadData(num);
			
			if(todolist == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}
			String param = "pageNum=" + pageNum;
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			model.addAttribute("todolist", todolist);
			model.addAttribute("params", param);
			model.addAttribute("pageNum", pageNum);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "bbs/article";
	}
}
