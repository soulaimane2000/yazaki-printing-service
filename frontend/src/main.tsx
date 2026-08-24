import React, { lazy, Suspense } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Layout from './Layout';
import { Skeleton } from './components';
import './styles.css';
import './enhancements.css';

const Dashboard = lazy(() => import('./pages/Dashboard'));
const Jobs = lazy(() => import('./pages/Jobs'));
const JobDetails = lazy(() => import('./pages/JobDetails'));
const Printers = lazy(() => import('./pages/Printers'));
const Errors = lazy(() => import('./pages/Errors'));
const Monitoring = lazy(() => import('./pages/Monitoring'));
const pending = <div className="panel"><Skeleton rows={6}/></div>;

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Suspense fallback={pending}>
        <Routes>
          <Route element={<Layout/>}>
            <Route index element={<Dashboard/>}/>
            <Route path="jobs" element={<Jobs/>}/>
            <Route path="jobs/:id" element={<JobDetails/>}/>
            <Route path="printers" element={<Printers/>}/>
            <Route path="errors" element={<Errors/>}/>
            <Route path="monitoring" element={<Monitoring/>}/>
            <Route path="*" element={<Dashboard/>}/>
          </Route>
        </Routes>
      </Suspense>
    </BrowserRouter>
  </React.StrictMode>
);
