package com.yazaki.printingservice.api;
import java.net.URI; import java.util.UUID; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import com.yazaki.printingservice.api.dto.PrinterDtos; import com.yazaki.printingservice.application.PrinterService; import com.yazaki.printingservice.domain.model.*; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid;
@RestController @RequestMapping("/api/v1/printers") @Tag(name="Printers")
public class PrinterController {
 private final PrinterService service; public PrinterController(PrinterService service){this.service=service;}
 @GetMapping Page<PrinterDtos.View> list(@RequestParam(required=false)String search,@RequestParam(required=false)PrinterType type,@RequestParam(required=false)PrinterStatus status,@RequestParam(required=false)Boolean enabled,@PageableDefault(size=20,sort="name")Pageable page){return service.search(search,type,status,enabled,page);}
 @GetMapping("/{id}") PrinterDtos.View get(@PathVariable UUID id){return service.get(id);}
 @PostMapping ResponseEntity<PrinterDtos.View> create(@Valid @RequestBody PrinterDtos.Upsert in){var p=service.create(in);return ResponseEntity.created(URI.create("/api/v1/printers/"+p.id())).body(p);}
 @PutMapping("/{id}") PrinterDtos.View replace(@PathVariable UUID id,@Valid @RequestBody PrinterDtos.Upsert in){return service.replace(id,in);}
 @PatchMapping("/{id}") PrinterDtos.View patch(@PathVariable UUID id,@Valid @RequestBody PrinterDtos.Patch in){return service.patch(id,in);}
 @PostMapping("/{id}/activate") PrinterDtos.View activate(@PathVariable UUID id){return service.enabled(id,true);} @PostMapping("/{id}/deactivate") PrinterDtos.View deactivate(@PathVariable UUID id){return service.enabled(id,false);}
}
