package com.example.todolist.controller;


import java.io.UnsupportedEncodingException;
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
	
	@RequestMapping(value ="/created", method = RequestMethod.POST)
	public String createdOk(TodoList todolist, HttpServletRequest request, Model model) {
	 try {
		int maxNum = todolistService.maxNum();
		
		todolist.setNum(maxNum +1);
		
		todolistService.insertData(todolist);
		
	} catch (Exception e) {
		e.printStackTrace();
		return "bbs/created";
	}
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(TodoList todolist, HttpServletRequest request, Model model) {
	    try {
	        String pageNum = request.getParameter("pageNum"); // 바뀌는 페이지 번호
	        int currentPage = 1; // 현재 페이지 번호(디폴트는 1)

	        if (pageNum != null)
	            currentPage = Integer.parseInt(pageNum);

	        String searchKey = request.getParameter("searchKey"); // 검색 키워드(subject, content, name)
	        String searchValue = request.getParameter("searchValue"); // 검색어

	        if (searchValue == null) {
	            searchKey = "subject"; // 검색 키워드의 디폴트는 subject
	            searchValue = ""; // 검색어의 디폴트는 빈문자열
	        } else {
	            if (request.getMethod().equalsIgnoreCase("GET")) {
	                // get 방식으로 request가 왔다면
	                // 쿼리 파라미터의 값()을 디코딩해준다.
	                searchValue = URLDecoder.decode(searchValue, "UTF-8");
	            }
	        }

	        int dataCount = todolistService.getDataCount(searchKey, searchValue);

	        int numPerPage = 5;
	        int totalPage = myUtil.getPageCount(numPerPage, dataCount);

	        if (currentPage > totalPage)
	            currentPage = totalPage;

	        int start = (currentPage - 1) * numPerPage + 1;
	        int end = currentPage * numPerPage;

	        List<TodoList> lists = todolistService.getLists(searchKey, searchValue, start, end);

	        String param = "";

	        if (searchValue != null && !searchValue.equals("")) {
	            // 검색어가 있다면
	            param = "searchKey=" + searchKey;
	            param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); // 컴퓨터의 언어로 인코딩
	        }
	        String listUrl = "/list";

	        if (!param.equals(""))
	            listUrl += "?" + param;

	        String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

	        String articleUrl = "/article?pageNum=" + currentPage;

	        if (!param.equals("")) {
	            articleUrl += "&" + param;
	            // article?pageNum=1&searchKey=subject&searchValue=춘식
	        }
	        model.addAttribute("lists", lists); // 전체게시물
	        model.addAttribute("articleUrl", articleUrl);
	        model.addAttribute("pageIndexList", pageIndexList);
	        model.addAttribute("dataCount", dataCount);
	        model.addAttribute("searchKey", searchKey); // 추가
	        model.addAttribute("searchValue", searchValue); // 추가
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
	
	@RequestMapping(value ="/updated", method = RequestMethod.GET)
	public String updated(HttpServletRequest request, Model model) {
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
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			model.addAttribute("todolist", todolist);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("params", param);
			model.addAttribute("searchKey", searchKey);
			model.addAttribute("searchValue", searchValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bbs/updated"; 
	}
	
	@RequestMapping(value = "/updated_ok", method = RequestMethod.POST)
	public String updatedOK(TodoList todolist, HttpServletRequest request, Model model) {
		String pageNum = request.getParameter("pageNum"); 
		String searchKey = request.getParameter("searchKey"); 
		String searchValue = request.getParameter("searchValue"); 
		String param = "?pageNum=" + pageNum;
		
		try {
			todolist.setContent(todolist.getContent().replaceAll("<br/>", "\r\n"));
			todolistService.updateData(todolist);
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 수정 중 에러가 발생했습니다.", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
			return "redirect:/list" + param;
	}
	
	@RequestMapping(value = "/deleted_ok", method= {RequestMethod.GET})
	public String deleteOK(HttpServletRequest request, Model model) {
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey"); 
		String searchValue = request.getParameter("searchValue"); 
		String param = "?pageNum=" + pageNum;
		
		try {
			todolistService.deleteData(num);
			
			
			if(searchValue != null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 삭제 중 에러가 발생했습니다.", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		return "redirect:/list" + param;
	}
}
