package com.itkolleg.bookingsystem.repos;

import com.itkolleg.bookingsystem.domains.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskJPARepo extends JpaRepository<Desk, Long> {

}
