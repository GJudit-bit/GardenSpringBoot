package com.example.garden.repository;

import com.example.garden.model.ExpenseSummary;
import com.example.garden.model.Registration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {


    @Query("select r from Registration r where to_char(r.createdDate, 'yyyy-MM-dd') = :date and r.createdBy = :username")
    List<Registration> findRegistrationsByDate(@Param("date") String date, @Param("username") String username, Sort sort);

    @Query("select r from Registration r where to_char(r.createdDate, 'yyyy-MM-dd')>= ?1 and to_char(r.createdDate, 'yyyy-MM-dd') <= ?2 and r.createdBy = ?3")
    List<Registration> findRegistrationsByDateRange( String dateFrom, String dateTo, String username, Sort sort);

    @Query(value="select sum(price) as sum_value, c.code, expense from registration r inner join currency c on r.currency_id=c.id where to_char(r.created_date, 'yyyy-MM-dd')= ?1 and r.created_by = ?2 group by  c.code, expense order by expense", nativeQuery = true)
    List<ExpenseSummary> findSumRegistrationsByDate(String date, String username);

    @Query(value="select sum(price) as sum_value, c.code, expense from registration r inner join currency c on r.currency_id=c.id where to_char(r.created_date, 'yyyy-MM-dd')>= ?1 and to_char(r.created_date, 'yyyy-MM-dd') <= ?2 and r.created_by = ?3 group by  c.code, expense order by expense", nativeQuery = true)
    List<ExpenseSummary> findSumRegistrationsByDateRange(String dateFrom, String dateTo, String username);
}
