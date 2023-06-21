package com.example.todolist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todolist.dao.TodoListDao;
import com.example.todolist.dto.TodoList;

@Service
public class TodoListServiceImpl implements TodoListService{
	
	@Autowired
	private TodoListDao todolistMapper;

	@Override
	public int maxNum() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insertDate(TodoList todolist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TodoList> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
