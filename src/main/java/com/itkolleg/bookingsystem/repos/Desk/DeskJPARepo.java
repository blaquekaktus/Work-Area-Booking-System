package com.itkolleg.bookingsystem.repos.Desk;

@Repository
public interface DeskJPARepo extends JpaRepository<Desk, Long> {

    @Query("select a from Desk a")
    Page<Desk>findAllDesksByPage(Pageable pageable);
}