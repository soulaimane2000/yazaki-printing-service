import type { JobStatus } from './types';
export const formatDate=(value:string|null|undefined)=>value?new Intl.DateTimeFormat('en-GB',{dateStyle:'medium',timeStyle:'short'}).format(new Date(value)):'—';
export const shortId=(id:string)=>id.slice(0,8).toUpperCase();
export const statusTone=(status:string)=>['ONLINE','PRINTED'].includes(status)?'success':['FAILED','ERROR','OFFLINE'].includes(status)?'danger':['QUEUED','PROCESSING','RETRY_SCHEDULED'].includes(status)?'warning':['SUBMITTED'].includes(status)?'info':'neutral';
export const canRetry=(s:JobStatus)=>s==='FAILED'||s==='RETRY_SCHEDULED';
export const canCancel=(s:JobStatus)=>!['PRINTED','CANCELLED'].includes(s);
