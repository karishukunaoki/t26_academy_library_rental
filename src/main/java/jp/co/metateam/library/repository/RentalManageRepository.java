package jp.co.metateam.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.metateam.library.model.RentalManageDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalManageRepository extends JpaRepository<RentalManageDto, Long> {
    boolean existsByStockIdAndExpectedRentalOnLessThanEqualAndExpectedReturnOnGreaterThanEqual(
            String stockId,
            String returnDate,
            String rentalDate);

    @Query(value = """
            SELECT COUNT(*)
            FROM rental_manage rm
            WHERE rm.stock_id = :stockId
            AND rm.status IN (0, 1)
            AND rm.expected_rental_on <= :returnDate
            AND rm.expected_return_on >= :rentalDate
            """, nativeQuery = true)
    Long existsDuplicateRentalPeriod(
            @Param("stockId") String stockId,
            @Param("rentalDate") String rentalDate,
            @Param("returnDate") String returnDate);
}
