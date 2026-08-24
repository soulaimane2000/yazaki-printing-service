package com.yazaki.printingservice.infrastructure.adapter;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Component; import com.yazaki.printingservice.domain.model.PrinterType;
@Component public class FakeCupsPrintAdapter extends AbstractFakePrintAdapter { public FakeCupsPrintAdapter(@Value("${printing.adapters.document-mode:SUCCESS}") FakeAdapterMode mode){super(mode);} public PrinterType supports(){return PrinterType.DOCUMENT;} }
