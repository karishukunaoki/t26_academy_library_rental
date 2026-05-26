package jp.co.metateam.library.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "rental_manage")
public class RentalManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer rentalManageId;
    private String employeeId;
    private String stockId;
    private LocalDate expectedRentalOn;
    private LocalDate expectedReturnOn;
    private Integer status;

}
