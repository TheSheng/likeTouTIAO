package com.lisheng.project.repository;


import com.lisheng.project.domain.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;


public interface NewRepository extends PagingAndSortingRepository<NewEntity,Integer> ,JpaRepository<NewEntity,Integer> {

     Page findAllByType(String type,Pageable pageable);
     NewEntity findById(Long id);
     @Query(value = "select max(id) from NewEntity ")
     Long findMaxId();


     List<NewEntity> findAllByIdBetween(Long start,Long end);
     List<NewEntity> findAllByIdIn(Set<Long> ids);
    List<NewEntity>  findAllByIdIn(Set<Long> ids,Sort sort);
     Page<NewEntity> findAllByIdIn(Set<Long> ids,Pageable pageable);
//    @Query(value = "select s from User as s where s.username like concat('%',?1,'%') and s.password = ?2")
//    Page<User> findByUsernameLikeAndPassword(String username, Integer password, Pageable pageable);
}
