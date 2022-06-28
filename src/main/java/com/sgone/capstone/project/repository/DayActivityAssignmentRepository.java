package com.sgone.capstone.project.repository;

import com.sgone.capstone.project.model.DayActivityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DayActivityAssignmentRepository extends JpaRepository<DayActivityAssignment, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM day_activity_assignment WHERE day_activity_id = ?1",
            nativeQuery = true
    )
    Integer deleteByDayActivityId(Long dayActivityId);

    @Modifying
    @Query(value = "DELETE FROM day_activity_assignment WHERE application_user_id = :USER_ID " +
            "AND day_activity_id = :DAY_ACTIVITY_ID", nativeQuery = true)
    @Transactional
    void deleteUserFromActivity(@Param("USER_ID") Long userID, @Param("DAY_ACTIVITY_ID") Long dayActivityID);

    @Modifying
    @Query(value="UPDATE day_activity_assignment\n" +
            "SET should_pay = (ROUND(CAST(\n" +
            "(SELECT price FROM day_activity WHERE id = :DAY_ACTIVITY_ID)/(SELECT COUNT(*) FROM day_activity_assignment" +
            " WHERE day_activity_assignment.day_activity_id = :DAY_ACTIVITY_ID)\n" +
            "AS numeric), 2))\n" +
            "WHERE day_activity_id = :DAY_ACTIVITY_ID",
            nativeQuery = true)
    @Transactional
    void SplitCostEvenly(@Param("DAY_ACTIVITY_ID") Long dayActivityID);

    @Modifying
    @Query(value= "INSERT INTO day_activity_assignment " +
            "(paid, should_pay, application_user_id, day_activity_id) " +
            "values (0, 0, :USER_ID, :DAY_ACTIVITY_ID)", nativeQuery = true)
    @Transactional
    void insertEmptyRows(@Param("USER_ID") Long userID, @Param("DAY_ACTIVITY_ID") Long dayActivityID);

    @Modifying
    @Query(value="UPDATE day_activity_assignment\n" +
            "SET (paid, should_pay) = (:PAID, :SHOULD_PAY) WHERE " +
            "application_user_id = :USER_ID AND day_activity_id = :DAY_ACTIVITY_ID ", nativeQuery = true)
    @Transactional
    void changeActivityAssignmentRow(@Param("USER_ID") Long userID, @Param("DAY_ACTIVITY_ID") Long dayActivityID,
                                     @Param("PAID") Double paid, @Param("SHOULD_PAY") Double shouldPay);

    @Query(value="SELECT SUM(should_pay)\n" +
            "FROM day_activity_assignment\n" +
            "WHERE day_activity_id = :DAY_ACTIVITY_ID\n", nativeQuery = true)
    Double getShouldPayTotalByActivity(@Param("DAY_ACTIVITY_ID") Long dayActivityID);

    @Query(value="SELECT SUM(paid)\n" +
            "FROM day_activity_assignment\n" +
            "WHERE day_activity_id = :DAY_ACTIVITY_ID\n", nativeQuery = true)
    Double getPaidTotalByActivity(@Param("DAY_ACTIVITY_ID") Long dayActivityID);
}
