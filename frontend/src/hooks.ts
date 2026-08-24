import { useCallback, useEffect, useState } from 'react';

export function useApi<T>(loader:()=>Promise<T>, dependencies:unknown[]=[], interval=0) {
  const [data,setData]=useState<T>(); const [loading,setLoading]=useState(true); const [error,setError]=useState('');
  const refresh=useCallback(async()=>{setLoading(true);setError('');try{setData(await loader());}catch(e){setError(e instanceof Error?e.message:'Unexpected error');}finally{setLoading(false);}},dependencies);
  useEffect(()=>{void refresh();if(!interval)return;const id=window.setInterval(()=>void refresh(),interval);return()=>window.clearInterval(id);},[refresh,interval]);
  return {data,loading,error,refresh,setData};
}
