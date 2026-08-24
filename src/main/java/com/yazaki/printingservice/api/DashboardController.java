package com.yazaki.printingservice.api;
import java.util.*; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.web.bind.annotation.*; import com.yazaki.printingservice.api.dto.*; import com.yazaki.printingservice.application.DashboardService; import io.swagger.v3.oas.annotations.tags.Tag;
@RestController @RequestMapping("/api/v1/dashboard") @Tag(name="Dashboard")
public class DashboardController { private final DashboardService service; public DashboardController(DashboardService service){this.service=service;}
 @GetMapping("/summary") DashboardDtos.Summary summary(){return service.summary();}
 @GetMapping("/recent-jobs") Page<PrintJobDtos.Job> recent(@PageableDefault(size=10,sort="createdAt",direction=Sort.Direction.DESC)Pageable p){return service.recent(p);}
 @GetMapping("/printer-status") List<DashboardDtos.PrinterStatusCount> printerStatus(){return service.printerStatus();}
 @GetMapping("/errors") Page<DashboardDtos.ErrorItem> errors(@PageableDefault(size=10,sort="createdAt",direction=Sort.Direction.DESC)Pageable p){return service.errors(p);}
 @GetMapping("/job-trends") List<DashboardDtos.Trend> trends(@RequestParam(defaultValue="14") int days){return service.trends(Math.min(Math.max(days,1),365));}
 @GetMapping("/type-distribution") List<DashboardDtos.Distribution> typeDistribution(){return service.typeDistribution();}
 @GetMapping("/status-distribution") List<DashboardDtos.Distribution> statusDistribution(){return service.statusDistribution();}
}
