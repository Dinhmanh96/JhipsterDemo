package com.dinhmanh.project8.repository;

import com.dinhmanh.project8.domain.Cophong;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cophong entity.
 */
@SuppressWarnings("unused")
public interface CophongRepository extends JpaRepository<Cophong,Long> {

}
