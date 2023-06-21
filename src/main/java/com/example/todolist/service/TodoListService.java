package com.example.todolist.service;

import java.util.List;
import com.example.todolist.dto.TodoList;

public interface TodoListService {
	public int maxNum() throws Exception;

	public void insertDate(TodoList todolist) throws Exception;

	public int getDataCount(String searchKey, String searchValue) throws Exception;

	public List<TodoList> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	
	
	
}
