package com.itkolleg.bookingsystem.repos.Desk;

import com.itkolleg.bookingsystem.domains.Desk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskJPARepo extends JpaRepository<Desk, Long> {

    @Query("select a from Desk a")
    Page<Desk> findAllDesksByPage(Pageable pageable);
}