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
		return todolistMapper.maxNum();
	}

	@Override
	public void insertDate(TodoList todolist) throws Exception {
		todolistMapper.insertData(todolist);
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		return todolistMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<TodoList> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		return todolistMapper.getLists(searchKey, searchValue, start, end);
	}

	@Override
	public TodoList getReadData(int num) throws Exception {
		return todolistMapper.getReadData(num);
	}

	@Override
	public void updateData(TodoList todolist) throws Exception {
		todolistMapper.updateData(todolist);
	}

	@Override
	public void deleteData(int num) throws Exception {
		todolistMapper.deleteData(num);
	}

}
