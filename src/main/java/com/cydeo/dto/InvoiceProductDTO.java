package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDTO {

    private Long id;
    @NotNull
    @Range(min=1,max = 100)
    private Integer quantity;
    @NotNull
    @Min(value = 1)
    private BigDecimal price;
    @NotNull
    @Range(min=1,max = 20)
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQuantity;
    private InvoiceDTO invoice;
    @NotNull
    private ProductDTO product;

}
