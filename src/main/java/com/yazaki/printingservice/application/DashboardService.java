package com.yazaki.printingservice.application;
import java.sql.Date; import java.time.*; import java.util.*; import org.springframework.data.domain.*; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import com.yazaki.printingservice.api.dto.*; import com.yazaki.printingservice.domain.model.*; import com.yazaki.printingservice.domain.repository.*;
@Service public class DashboardService {
 private final PrintJobRepository jobs; private final PrinterRepository printers; private final PrintJobService mapper; private final JdbcTemplate jdbc;
 public DashboardService(PrintJobRepository jobs,PrinterRepository printers,PrintJobService mapper,JdbcTemplate jdbc){this.jobs=jobs;this.printers=printers;this.mapper=mapper;this.jdbc=jdbc;}
 @Transactional(readOnly=true) public DashboardDtos.Summary summary(){long pending=count(PrintJobStatus.RECEIVED)+count(PrintJobStatus.VALIDATED)+count(PrintJobStatus.QUEUED)+count(PrintJobStatus.PROCESSING)+count(PrintJobStatus.RETRY_SCHEDULED);return new DashboardDtos.Summary(jobs.count(),pending,count(PrintJobStatus.SUBMITTED),count(PrintJobStatus.PRINTED),count(PrintJobStatus.FAILED),jdbc.queryForObject("select count(*) from print_attempt where attempt_number > 1",Long.class),printers.countByStatusAndEnabledTrue(PrinterStatus.ONLINE),printers.countByStatusAndEnabledTrue(PrinterStatus.OFFLINE));}
 @Transactional(readOnly=true) public Page<PrintJobDtos.Job> recent(Pageable p){return jobs.findAll(p).map(mapper::view);}
 @Transactional(readOnly=true) public List<DashboardDtos.PrinterStatusCount> printerStatus(){return Arrays.stream(PrinterStatus.values()).map(s->new DashboardDtos.PrinterStatusCount(s,printers.countByStatusAndEnabledTrue(s))).toList();}
 @Transactional(readOnly=true) public Page<DashboardDtos.ErrorItem> errors(Pageable p){return jobs.findAll((r,q,b)->b.equal(r.get("status"),PrintJobStatus.FAILED),p).map(j->new DashboardDtos.ErrorItem(j.getId(),j.getErrorCode(),j.getErrorMessage(),j.getCompletedAt()));}
 public List<DashboardDtos.Trend> trends(int days){return jdbc.query("select cast(created_at at time zone 'UTC' as date) day,count(*) total from print_job where created_at >= now() - (? * interval '1 day') group by day order by day",(rs,n)->new DashboardDtos.Trend(rs.getObject("day",LocalDate.class),rs.getLong("total")),days);}
 public List<DashboardDtos.Distribution> typeDistribution(){return jdbc.query("select print_type key,count(*) total from print_request group by print_type order by print_type",(rs,n)->new DashboardDtos.Distribution(rs.getString("key"),rs.getLong("total")));}
 public List<DashboardDtos.Distribution> statusDistribution(){return jdbc.query("select status key,count(*) total from print_job group by status order by status",(rs,n)->new DashboardDtos.Distribution(rs.getString("key"),rs.getLong("total")));}
 private long count(PrintJobStatus s){return jobs.countByStatus(s);}
}
