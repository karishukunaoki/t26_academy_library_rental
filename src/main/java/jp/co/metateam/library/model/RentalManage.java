package jp.co.metateam.library.model;

import java.util.List;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;


import javax.xml.crypto.Data;

import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
//貸出管理テーブルのEntity→DBのテーブルと対応する
@Table(name = "RentalManage")
public class RentalManage {

    //貸出管理番号//
    
    @Id//貸出管理番号
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    //社員番号
    //@ManyToOneを使用する際は型名をEntityに設定
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    private Account employeeId;

    //在庫番号
    //@ManyToOneを使用する際は型名をEntityに設定
    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
    private Stock stockId;

    //貸出ステータス//
    @Column(name = "status", nullable = false)
    private Integer rentalStatus;

    //貸出予定日//
    @Column(name = "expected_rental_on", nullable = false)
    private LocalDate expectedRentalOn;

    //返却予定日//
    @Column(name = "expected_return_on", nullable = false)
    private LocalDate expectedReturnOn;
    
    //貸出日時//
    @Column(name = "rentaled_at")
    private LocalDateTime rentalAt;

    //返却日時//
    @Column(name = "returned_at")
    private LocalDateTime returnAt;

    //キャンセル日時//
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    //登録日時//
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //更新日時//
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //getters//
    //値を取り出すメソッド
    public String getId() {
        return this.id;
    }
    public Account getEmployeeId() {
        return this.employeeId;
    }
    public Stock getStockId() {
        return this.stockId;
    }
    public Integer getRentalStatus() {
        return this.rentalStatus;
    }

    public LocalDate getExpectedRentalOn() {
        return this.expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return this.expectedReturnOn;
    }

    public LocalDateTime getRentalAt() {
        return this.rentalAt;
    }

    public LocalDateTime getReturnAt() {
        return this.returnAt;
    }

    public LocalDateTime getCanceledAt() {
        return this.canceledAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    //setters//
    //値をセットするメソッド
    public void setId(String id){
        this.id = id;
    }
    public void setEmployeeId(Account employeeId){
        this.employeeId = employeeId;
    }
    public void setStockId(Stock stockId){
        this.stockId = stockId;
    }

    public void setRentalStatus(Integer rentalStatus){
        this.rentalStatus = rentalStatus;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn){
        this.expectedRentalOn = expectedRentalOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn){
        this.expectedReturnOn = expectedReturnOn;
    }

    public void setRentalAt(LocalDateTime rentalAt){
        this.rentalAt = rentalAt;
    }

    public void setReturnAt(LocalDateTime returnAt){
        this.returnAt = returnAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt){
        this.canceledAt = canceledAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }






}
