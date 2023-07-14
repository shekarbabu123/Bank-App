package com.bankapplication.model;


import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequest  {

    @JsonProperty(value = "userName")
    @NotNull(message = "{user.username.absent}")
    @NotEmpty
    private String userName;
    @JsonProperty(value="lastName")
    @NotNull(message="{user.lastName.absent}")
    @NotEmpty
    private String lastName;
    @JsonProperty(value="address")
    @NotNull(message="user.address.absent")
    @NotEmpty
    private String address;

    @JsonProperty(value = "password")
    @NotNull(message = "{user.password.absent}")
    @NotEmpty
    private String password;
    @JsonProperty(value = "balanceAmount")
    @NotNull(message = "{user.balance.amount.absent}")
    @Min(value = 500,message = "{user.min_balance}")
    private BigDecimal balanceAmount;
	@Override
	public String toString() {
		return "CreateAccountRequest [userName=" + userName + ", lastName=" + lastName + ", address=" + address
				+ ", password=" + password + ", balanceAmount=" + balanceAmount + "]";
	}

    
}
