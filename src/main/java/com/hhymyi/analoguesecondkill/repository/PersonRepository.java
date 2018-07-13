package com.hhymyi.analoguesecondkill.repository;

import com.hhymyi.analoguesecondkill.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "select id,age,name,version from person where id=?1 for update", nativeQuery = true)
    Person getPersionByIdForUpdate(Long id);
}
