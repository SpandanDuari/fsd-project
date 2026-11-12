import { Link, NavLink, useNavigate } from "react-router-dom";
import {
  Bell, BriefcaseBusiness, ChevronDown, FileText,
  LayoutDashboard, LogOut, Menu, Search, Settings, ShieldCheck, Sparkles,
  X, Plus
} from "lucide-react";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";

const navigation = {
  candidate: [
    ["Dashboard", "/candidate/dashboard", LayoutDashboard],
    ["Jobs", "/jobs", BriefcaseBusiness],
    ["Applications", "/applications", FileText],
  ],
  recruiter: [
    ["Dashboard", "/recruiter/dashboard", LayoutDashboard],
    ["Jobs", "/jobs", BriefcaseBusiness],
    ["Applications", "/applications", FileText],
  ],
  admin: [
    ["Dashboard", "/admin/dashboard", LayoutDashboard],
    ["Jobs", "/jobs", BriefcaseBusiness],
    ["Applications", "/applications", FileText],
  ],
};

export default function Layout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileOpen, setMobileOpen] = useState(false);
  const dashboard = user?.role === "recruiter" ? "/recruiter/dashboard" : user?.role === "admin" ? "/admin/dashboard" : "/candidate/dashboard";

  return (
    <div className="min-h-screen bg-[#f7f8fc] text-[#172033]">
      {user && (
        <aside className={`${mobileOpen ? "translate-x-0" : "-translate-x-full"} fixed inset-y-0 left-0 z-40 w-64 border-r border-slate-200 bg-white transition-transform lg:translate-x-0`}>
          <div className="flex h-full flex-col px-5 py-6">
            <Link to={dashboard} className="flex items-center gap-3 px-2 text-xl font-black tracking-tight">
              <span className="grid h-10 w-10 place-items-center rounded-xl bg-[#3158e8] text-white shadow-lg shadow-blue-200"><BriefcaseBusiness size={20}/></span>
              <span>Hire<span className="text-[#3158e8]">Track</span></span>
            </Link>
            <div className="mt-10 px-2 text-[10px] font-bold uppercase tracking-[.2em] text-slate-400">Workspace</div>
            <nav className="mt-3 space-y-1">
              {(navigation[user.role] || navigation.candidate).map(([label, path, Icon]) => <NavLink key={label} to={path} onClick={() => setMobileOpen(false)} className={({isActive}) => `group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold transition ${isActive ? "bg-blue-50 text-[#3158e8]" : "text-slate-500 hover:bg-slate-50 hover:text-slate-900"}`}><Icon size={18}/>{label}</NavLink>)}
              {user.role === "candidate" && <NavLink to="/candidate/profile" className="group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><FileText size={18}/>Profile & resume</NavLink>}
              {user.role === "recruiter" && <NavLink to="/recruiter/jobs/new" className="group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><Plus size={18}/>Post a job</NavLink>}
              {user.role === "admin" && <><NavLink to="/admin/accounts/candidates" className="group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><ShieldCheck size={18}/>Candidate accounts</NavLink><NavLink to="/admin/accounts/recruiters" className="group flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><ShieldCheck size={18}/>Recruiter accounts</NavLink></>}
            </nav>
            <div className="mt-8 px-2 text-[10px] font-bold uppercase tracking-[.2em] text-slate-400">Preferences</div>
            <nav className="mt-3 space-y-1">
              <NavLink to="/about" className="flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><Settings size={18}/>Settings</NavLink>
              <NavLink to="/about" className="flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-semibold text-slate-500 hover:bg-slate-50"><ShieldCheck size={18}/>Help center</NavLink>
            </nav>
            <div className="mt-auto rounded-2xl bg-slate-50 p-4">
              <div className="flex items-center gap-3"><div className="grid h-9 w-9 place-items-center rounded-full bg-[#dbe5ff] font-bold text-[#3158e8]">{(user.name || user.email)[0].toUpperCase()}</div><div className="min-w-0"><p className="truncate text-sm font-bold">{user.name || "HireTrack user"}</p><p className="truncate text-xs text-slate-500">{user.role}</p></div></div>
              <button onClick={() => { logout(); navigate("/"); }} className="mt-4 flex items-center gap-2 text-xs font-semibold text-slate-500 hover:text-red-600"><LogOut size={14}/>Sign out</button>
            </div>
          </div>
        </aside>
      )}
      <div className={user ? "lg:pl-64" : ""}>
        <header className="sticky top-0 z-30 border-b border-slate-200/80 bg-white/90 backdrop-blur">
          <div className="mx-auto flex h-[72px] max-w-[1500px] items-center justify-between gap-5 px-5 lg:px-8">
            <div className="flex items-center gap-3">
              {user && <button className="lg:hidden" onClick={() => setMobileOpen(!mobileOpen)}>{mobileOpen ? <X/> : <Menu/>}</button>}
              {!user && <Link to="/" className="flex items-center gap-3 text-xl font-black"><span className="grid h-10 w-10 place-items-center rounded-xl bg-[#3158e8] text-white"><BriefcaseBusiness size={20}/></span>Hire<span className="text-[#3158e8]">Track</span></Link>}
              {user && <div className="hidden text-sm font-semibold text-slate-400 sm:block">Workspace <span className="mx-2">/</span> <span className="text-slate-700">{user.role === "recruiter" ? "Recruiter dashboard" : "Overview"}</span></div>}
            </div>
            <div className="hidden max-w-md flex-1 items-center gap-3 rounded-xl border border-slate-200 bg-slate-50 px-4 py-2.5 md:flex"><Search size={17} className="text-slate-400"/><input className="w-full bg-transparent text-sm outline-none placeholder:text-slate-400" placeholder="Search anything..." /></div>
            <div className="flex items-center gap-3">
              {user && <button className="relative rounded-xl p-2.5 text-slate-500 hover:bg-slate-100"><Bell size={18}/><span className="absolute right-2 top-2 h-1.5 w-1.5 rounded-full bg-[#3158e8]"/></button>}
              {user ? <div className="flex items-center gap-2"><Link to={dashboard} className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-2 py-1.5"><span className="grid h-8 w-8 place-items-center rounded-lg bg-[#172033] text-xs font-bold text-white">{(user.name || user.email)[0].toUpperCase()}</span><span className="hidden text-sm font-semibold sm:block">{user.name || "Account"}</span><ChevronDown size={15} className="text-slate-400"/></Link><button type="button" onClick={() => { logout(); setMobileOpen(false); navigate("/login", { replace: true }); }} className="rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-sm font-bold text-slate-600 hover:border-red-200 hover:text-red-600">Sign out</button></div> : <Link to="/login" className="rounded-xl bg-[#172033] px-5 py-2.5 text-sm font-bold text-white">Sign in</Link>}
            </div>
          </div>
        </header>
        <main>{children}</main>
      </div>
    </div>
  );
}
