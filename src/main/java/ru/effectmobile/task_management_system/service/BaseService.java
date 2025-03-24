package ru.effectmobile.task_management_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BaseService<T> {

    Page<T> findAll(Pageable pageable);

    T findById(UUID id);

    T save(T entity);

    void deleteById(UUID id);
}
