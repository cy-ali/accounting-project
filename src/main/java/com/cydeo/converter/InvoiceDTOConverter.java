package com.cydeo.converter;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.service.InvoiceService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDTOConverter implements Converter<String, InvoiceDTO> {

    InvoiceService invoiceService;

    public InvoiceDTOConverter(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDTO convert(String source) {

        if (source == null || source.equals("")) {
            return null;
        }

        return invoiceService.findById(Long.parseLong(source));

    }

}
