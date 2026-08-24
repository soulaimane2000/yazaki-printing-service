package com.yazaki.printingservice.infrastructure.adapter;
import com.yazaki.printingservice.application.port.*; import com.yazaki.printingservice.domain.model.*;
abstract class AbstractFakePrintAdapter implements PrintAdapter {
 private final FakeAdapterMode mode; AbstractFakePrintAdapter(FakeAdapterMode mode){this.mode=mode;}
 public AdapterResult submit(PrintJob job){return switch(mode){case SUCCESS->AdapterResult.success();case TIMEOUT->new AdapterResult(PrintAttemptResult.TIMEOUT,"FAKE_TIMEOUT","Simulated adapter timeout");case OFFLINE->new AdapterResult(PrintAttemptResult.PRINTER_OFFLINE,"FAKE_OFFLINE","Simulated printer offline");case PERMANENT_ERROR->new AdapterResult(PrintAttemptResult.PERMANENT_FAILURE,"FAKE_PERMANENT_ERROR","Simulated permanent adapter error");};}
}
