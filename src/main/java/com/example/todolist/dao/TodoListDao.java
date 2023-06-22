package com.example.todolist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.todolist.dto.TodoList;

@Mapper
public interface TodoListDao {
	public int maxNum() throws Exception;
	
	public void insertData(TodoList todolist) throws Exception;
	
	public int getDataCount(String searchKey, String searchValue) throws Exception;
	
	public List<TodoList> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	public TodoList getReadData(int num) throws Exception;
	
	public void updateData(TodoList todolist) throws Exception;
	
	public void deleteData(int num) throws Exception;	
}
