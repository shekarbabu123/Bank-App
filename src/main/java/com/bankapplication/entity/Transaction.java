package com.bankapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @Column(name = "transaction_reference_id")
    private String transactionRefId;
    @Column(name = "from_account_id")
    private Integer fromAccountId;
    @Column(name = "to_account_id")
    private Integer toAccountId;
    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Column(name = "transaction_currency")
    private String transactionCurrency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionRefId, that.transactionRefId) && Objects.equals(fromAccountId, that.fromAccountId) && Objects.equals(toAccountId, that.toAccountId) && Objects.equals(transactionAmount, that.transactionAmount) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(transactionCurrency, that.transactionCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionRefId, fromAccountId, toAccountId, transactionAmount, transactionDate, transactionCurrency);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionRefId='" + transactionRefId + '\'' +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                ", transactionDate=" + transactionDate +
                ", transactionCurrency='" + transactionCurrency + '\'' +
                '}';
    }
}
