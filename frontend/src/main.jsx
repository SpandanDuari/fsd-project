import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import "./index.css";
import Layout from "./components/Layout";
import { AuthProvider, useAuth } from "./context/AuthContext";
import { ToastProvider } from "./context/ToastContext";
import { Landing, SignupChoice, About, Jobs, JobDetail, Auth, Dashboard, JobManager, CandidateProfile } from "./pages/Pages";

function Protected({ children, role }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (role && user.role !== role) return <Navigate to={`/${user.role}/dashboard`} replace />;
  return children;
}
function App() {
  return <ToastProvider><AuthProvider><Layout><Routes>
    <Route path="/" element={<Landing />} /><Route path="/signup" element={<SignupChoice />} /><Route path="/about" element={<About />} />
    <Route path="/jobs" element={<Jobs />} /><Route path="/jobs/:id" element={<JobDetail />} />
    <Route path="/login" element={<Auth />} /><Route path="/register/candidate" element={<Auth register />} /><Route path="/register/recruiter" element={<Auth register role="recruiter" />} />
    <Route path="/candidate/dashboard" element={<Protected role="candidate"><Dashboard /></Protected>} />
    <Route path="/recruiter/dashboard" element={<Protected role="recruiter"><Dashboard role="recruiter" /></Protected>} />
    <Route path="/admin/dashboard" element={<Protected role="admin"><Dashboard role="admin" /></Protected>} />
    <Route path="/candidate/profile" element={<Protected role="candidate"><CandidateProfile /></Protected>} />
    <Route path="/recruiter/jobs/new" element={<Protected role="recruiter"><JobManager /></Protected>} />
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></Layout></AuthProvider></ToastProvider>;
}
createRoot(document.getElementById("root")).render(<React.StrictMode><BrowserRouter><App /></BrowserRouter></React.StrictMode>);
