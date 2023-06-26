package com.itkolleg.bookingsystem.repos.Ressource;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RessourceJPARepo extends JpaRepository<Ressource, Long> {

    //Hier kommen spezifische Methoden rein, die nicht bereits von JpaRepository abgedeckt werden

    Ressource findRessourceById(Long id);

}
