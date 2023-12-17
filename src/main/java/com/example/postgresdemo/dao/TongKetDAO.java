package com.example.postgresdemo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postgresdemo.model.Tongket;

@Transactional
public interface TongKetDAO extends JpaRepository<Tongket, Integer> {
    List<Tongket> findAllByNguoiDungIdND(Long userId);

    List<Tongket> findByNguoiDungIdND(String idND);
}
