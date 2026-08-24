package com.yazaki.printingservice.application.port;
import com.yazaki.printingservice.domain.model.*;
public interface PrintAdapter { PrinterType supports(); AdapterResult submit(PrintJob job); }
