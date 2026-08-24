import type { ApiError, CreateJobInput, DashboardError, DashboardSummary, Distribution, JobFilters, JobTrend, Page, Printer, PrinterInput, PrintAttempt, PrintEvent, PrintJob, StatusCount } from './types';

const baseUrl = (import.meta.env.VITE_API_BASE_URL ?? '').replace(/\/$/, '');
export class ApiRequestError extends Error { constructor(public status:number, public code:string, message:string, public fieldErrors:Record<string,string>={}) { super(message); } }

async function request<T>(path:string, init?:RequestInit):Promise<T> {
  let response:Response;
  try { response = await fetch(`${baseUrl}${path}`, { ...init, headers:{'Content-Type':'application/json', ...init?.headers} }); }
  catch { throw new ApiRequestError(0, 'NETWORK_ERROR', 'Backend unavailable. Check the Spring Boot service.'); }
  if (!response.ok) { let body:Partial<ApiError>={}; try { body=await response.json(); } catch { /* non-JSON error */ } throw new ApiRequestError(response.status, body.code ?? 'HTTP_ERROR', body.message ?? `Request failed (${response.status})`, body.fieldErrors); }
  return response.status === 204 ? undefined as T : response.json();
}
const query=(values:Record<string,unknown>)=>{const p=new URLSearchParams();Object.entries(values).forEach(([k,v])=>{if(v!==undefined&&v!==null&&v!=='')p.set(k,String(v));});return p.toString();};

export const api = {
  health:()=>request<{status:string}>('/actuator/health'),
  jobs:(filters:JobFilters={})=>request<Page<PrintJob>>(`/api/v1/print-jobs?${query({...filters,sort:'createdAt,desc'})}`),
  job:(id:string)=>request<PrintJob>(`/api/v1/print-jobs/${id}`),
  events:(id:string)=>request<PrintEvent[]>(`/api/v1/print-jobs/${id}/events`),
  attempts:(id:string)=>request<PrintAttempt[]>(`/api/v1/print-jobs/${id}/attempts`),
  createJob:(body:CreateJobInput)=>request<PrintJob>('/api/v1/print-jobs',{method:'POST',body:JSON.stringify(body)}),
  jobAction:(id:string,action:'retry'|'reprint'|'cancel')=>request<PrintJob>(`/api/v1/print-jobs/${id}/${action}`,{method:'POST'}),
  printers:(values:Record<string,unknown>={})=>request<Page<Printer>>(`/api/v1/printers?${query({...values,sort:'name,asc'})}`),
  printer:(id:string)=>request<Printer>(`/api/v1/printers/${id}`),
  createPrinter:(body:PrinterInput)=>request<Printer>('/api/v1/printers',{method:'POST',body:JSON.stringify(body)}),
  updatePrinter:(id:string,body:PrinterInput)=>request<Printer>(`/api/v1/printers/${id}`,{method:'PUT',body:JSON.stringify(body)}),
  togglePrinter:(id:string,enabled:boolean)=>request<Printer>(`/api/v1/printers/${id}/${enabled?'activate':'deactivate'}`,{method:'POST'}),
  summary:()=>request<DashboardSummary>('/api/v1/dashboard/summary'),
  recentJobs:(size=8)=>request<Page<PrintJob>>(`/api/v1/dashboard/recent-jobs?size=${size}&sort=createdAt,desc`),
  printerStatus:()=>request<StatusCount[]>('/api/v1/dashboard/printer-status'),
  errors:(page=0,size=20)=>request<Page<DashboardError>>(`/api/v1/dashboard/errors?page=${page}&size=${size}&sort=createdAt,desc`),
  trends:(days=14)=>request<JobTrend[]>(`/api/v1/dashboard/job-trends?days=${days}`),
  typeDistribution:()=>request<Distribution[]>('/api/v1/dashboard/type-distribution'),
  statusDistribution:()=>request<Distribution[]>('/api/v1/dashboard/status-distribution')
};
