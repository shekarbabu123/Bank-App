package com.bankapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {

	private static final long serialVersionUID = 7156526077883281623L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer accountId;
	@Column(name = "balance_amount")
	
	private BigDecimal balanceAmount;
	@Column(name = "limit_amount")
	private BigDecimal limitAmount;
	@Column(name = "lien_amount")
	private BigDecimal lienAmount;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return Objects.equals(accountId, account.accountId) && Objects.equals(balanceAmount, account.balanceAmount) && Objects.equals(limitAmount, account.limitAmount) && Objects.equals(lienAmount, account.lienAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, balanceAmount, limitAmount, lienAmount);
	}

	
	
	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balanceAmount=" + balanceAmount + ", limitAmount=" + limitAmount
				+ ", lienAmount=" + lienAmount + "]";
	}

//	@Override
//	public String toString() {
//		return "Account{" +
//				"accountId=" + accountId +
//				", balanceAmount='" + balanceAmount + '\'' +
//				", limitAmount='" + limitAmount + '\'' +
//				", lienAmount='" + lienAmount + '\'' +
//				'}';
	
	
	
}
