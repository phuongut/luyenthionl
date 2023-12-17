package com.example.postgresdemo.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postgresdemo.model.KetQua;

@Transactional
public interface KetQuaDAO extends JpaRepository<KetQua, Long> {
    List<KetQua> findAllByNguoiDungIdND(Long userId);

}
