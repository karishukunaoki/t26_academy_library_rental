package jp.co.metateam.library.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import jp.co.metateam.library.model.RentalManageDto;

import lombok.RequiredArgsConstructor;

@Repository

@RequiredArgsConstructor

public class RentalRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insert(RentalManageDto dto) {

        String sql = """

            INSERT INTO rental (

                employee_id,

                stock_id,

                status,

                rental_date_on,

                return_date_on

            ) VALUES (?, ?, ?, ?, ?)

            """;

        jdbcTemplate.update(

                sql,

                dto.getEmployeeId(),

                dto.getStockId(),

                dto.getStatus(),

                dto.getExpectedRentalOn(),

                dto.getExpectedReturnOn()

        );

    }

}
 