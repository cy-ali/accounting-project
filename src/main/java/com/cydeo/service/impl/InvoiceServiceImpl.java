package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyService companyService;
    private final InvoiceProductService invoiceProductService;

    private final MapperUtil mapperUtil;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, CompanyService companyService, @Lazy InvoiceProductService invoiceProductService, MapperUtil mapperUtil) {
        this.invoiceRepository = invoiceRepository;
        this.companyService = companyService;
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceDTO findById(Long id) {

        return mapperUtil.convert(invoiceRepository.findByIdAndIsDeleted(id,false), new InvoiceDTO());
    }


    @Override
    public InvoiceDTO save(InvoiceDTO invoice, InvoiceType type) {
        invoice.setCompany(companyService.getCompanyDTOByLoggedInUser());
        Invoice invoice1 = mapperUtil.convert(invoice, new Invoice());
        invoice1.setInvoiceType(type);
        invoice1.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice1.setDate(LocalDate.now());
        invoiceRepository.save(invoice1);
        return mapperUtil.convert(invoice1, new InvoiceDTO());


    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoice) {

        Optional<Invoice> invoice2 = invoiceRepository.findById(invoice.getId());

        Invoice updatedInvoice = mapperUtil.convert(invoice, new Invoice());
        updatedInvoice.setClientVendor(invoice2.get().getClientVendor());

        invoiceRepository.save(updatedInvoice);
        return mapperUtil.convert(updatedInvoice, new InvoiceDTO());
    }


    @Override
    public List<InvoiceDTO> listAllInvoice(InvoiceType type) {
        return invoiceRepository.findAllByInvoiceTypeOrderByInvoiceNoDesc(type).stream()
                .map(invoice -> calculateTotal(invoice.getId()))
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());

    }

    @Override
    public InvoiceDTO delete(Long id) {
        Invoice invoice = invoiceRepository.findByIdAndIsDeleted(id, false);
        InvoiceDTO invoiceDTO = mapperUtil.convert(invoice, new InvoiceDTO());
        if (invoiceDTO.getInvoiceStatus().getValue().equals("Awaiting Approval")) {
            invoice.setIsDeleted(true);
            invoiceRepository.save(invoice);
        }
        return invoiceDTO;
    }

    @Override
    public InvoiceDTO approve(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDTO());
    }

    @Override
    public InvoiceDTO createNewSalesInvoice() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo("S-00" + (invoiceRepository.findAllByInvoiceTypeOrderByInvoiceNoDesc(InvoiceType.SALES).size() + 1));
        invoiceDTO.setDate(LocalDate.now());

        return invoiceDTO;
    }

    @Override
    public InvoiceDTO createNewPurchasesInvoice() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo("P-00" + (invoiceRepository.findAllByInvoiceTypeOrderByInvoiceNoDesc(InvoiceType.PURCHASE).size() + 1));
        invoiceDTO.setDate(LocalDate.now());
        return invoiceDTO;

    }

    @Override
    public String findInvoiceId() {

        return String.valueOf(invoiceRepository.findAll().size());
    }

    private InvoiceDTO calculateTotal(Long id) {
        InvoiceDTO invoiceDTO=findById(id);
        List<InvoiceProductDTO> productList = invoiceProductService.listAllInvoiceProduct(id);
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        BigDecimal totalWithTax=BigDecimal.valueOf(0);
        BigDecimal tax=BigDecimal.valueOf(0);
        for (InvoiceProductDTO each : productList) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(each.getQuantity()).multiply(each.getPrice()));
            tax=tax.add(totalPrice.multiply(BigDecimal.valueOf(each.getTax().intValue())));
            totalWithTax=totalPrice.add(tax);
        }
        invoiceDTO.setPrice(totalPrice);
        invoiceDTO.setTax(tax.intValue()/100);
        invoiceDTO.setTotal(totalWithTax);


        return invoiceDTO;

    }
}









