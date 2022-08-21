package com.example.onlineshop.repository;

import com.example.onlineshop.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

}
