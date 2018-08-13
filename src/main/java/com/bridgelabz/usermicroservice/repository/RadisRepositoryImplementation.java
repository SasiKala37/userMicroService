package com.bridgelabz.usermicroservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RadisRepositoryImplementation implements RedisRepository {

	@Value("${spring.cache.redis.key-prefix}")
	private String key;

	private RedisTemplate<String, String> redisTemplate;

	private HashOperations<String, String, String> hashOperations;

	@Autowired
	public RadisRepositoryImplementation(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		hashOperations = this.redisTemplate.opsForHash();
	}

	@Override
	public void save(String token, String userId) {
		hashOperations.put(key, token, userId);
	}

	@Override
	public String find(String token) {
		return hashOperations.get(key, token);
	}

	@Override
	public void delete(String token) {
		hashOperations.delete(token, key);

	}
}
