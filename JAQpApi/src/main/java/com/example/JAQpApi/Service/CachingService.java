package com.example.JAQpApi.Service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CachingService
{
    @CacheEvict(value = "QuizService::GetOwnedById", key = "#_id")
    public void EvictGetOwnedById(Integer _id)
    {
        return;
    }
}
