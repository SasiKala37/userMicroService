package com.bridgelabz.usermicroservice.repository;

public interface RedisRepository {

	public String find(String token);

    public void save(String token, String userId);
    
    public void delete(String token);
}
