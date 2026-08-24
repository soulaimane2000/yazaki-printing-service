package com.yazaki.printingservice.application.port;
import com.yazaki.printingservice.domain.model.PrintAttemptResult;
public record AdapterResult(PrintAttemptResult result,String errorCode,String message) {
 public static AdapterResult success(){return new AdapterResult(PrintAttemptResult.SUCCESS,null,"Accepted by fake adapter");}
 public boolean successful(){return result==PrintAttemptResult.SUCCESS;}
 public boolean temporary(){return result==PrintAttemptResult.TEMPORARY_FAILURE||result==PrintAttemptResult.TIMEOUT||result==PrintAttemptResult.PRINTER_OFFLINE;}
}
