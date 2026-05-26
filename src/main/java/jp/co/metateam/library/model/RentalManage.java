package jp.co.metateam.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class RentalManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer employeeId;

    private LocalDate expectedRentalOn;

    private LocalDate expectedReturnOn;

    private Integer stockId;

    private String status;

    public Long getId() {
        return id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId1(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStockId(Integer stockId2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStockId'");
    }
}