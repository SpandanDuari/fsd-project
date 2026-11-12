import { KeyRound, RefreshCw, Trash2, UserCheck, UserX } from "lucide-react";
import { useEffect, useState } from "react";
import api from "../lib/api";
import { Empty, ErrorState, Loading } from "../components/States";
import { useToast } from "../context/ToastContext";

const labels = {
  candidates: {
    title: "Candidate accounts",
    text: "Activate, deactivate, reset, or remove candidate access.",
    endpoint: "/admin/candidates",
  },
  recruiters: {
    title: "Recruiter accounts",
    text: "Manage recruiter access without changing published job history.",
    endpoint: "/admin/recruiters",
  },
};

export default function AdminAccounts({ type }) {
  const config = labels[type];
  const toast = useToast();
  const [accounts, setAccounts] = useState(null);
  const [error, setError] = useState(false);
  const [busy, setBusy] = useState(null);
  const [temporaryPassword, setTemporaryPassword] = useState(null);

  const load = () => {
    setAccounts(null);
    setError(false);
    api.get(config.endpoint).then((response) => setAccounts(response.data)).catch(() => {
      setAccounts([]);
      setError(true);
    });
  };

  useEffect(() => { load(); }, [type]);

  const changeStatus = async (account) => {
    setBusy(account.user_id);
    try {
      await api.patch(`/admin/accounts/${account.user_id}/status`, { active: !account.active });
      toast(account.active ? "Account deactivated" : "Account activated");
      load();
    } catch (err) {
      toast(err.response?.data?.error || "Could not update this account");
    } finally {
      setBusy(null);
    }
  };

  const resetPassword = async (account) => {
    setBusy(account.user_id);
    try {
      const response = await api.post(`/admin/accounts/${account.user_id}/reset-password`);
      setTemporaryPassword({ email: account.email, value: response.data.temporaryPassword });
      toast("Temporary password generated");
    } catch (err) {
      toast(err.response?.data?.error || "Could not reset this password");
    } finally {
      setBusy(null);
    }
  };

  const deleteAccount = async (account) => {
    if (!window.confirm(`Permanently delete the ${type.slice(0, -1)} account for ${account.email}?`)) return;
    setBusy(account.user_id);
    try {
      await api.delete(`/admin/accounts/${account.user_id}`);
      toast("Account deleted");
      load();
    } catch (err) {
      toast(err.response?.data?.error || "Could not delete this account");
    } finally {
      setBusy(null);
    }
  };

  const name = (account) => type === "candidates"
    ? `${account.first_name || ""} ${account.last_name || ""}`.trim() || account.email
    : account.name || account.email;

  return <section className="mx-auto max-w-[1500px] px-5 py-10 lg:px-8">
    <p className="mb-2 text-xs font-bold uppercase tracking-[.2em] text-[#3158e8]">account administration</p>
    <div className="flex flex-wrap items-start justify-between gap-4">
      <div><h1 className="text-3xl font-black tracking-tight text-[#172033] md:text-4xl">{config.title}</h1><p className="mt-2 text-sm text-slate-500">{config.text}</p></div>
      <button onClick={load} className="inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm font-bold text-slate-600"><RefreshCw size={16}/>Refresh</button>
    </div>
    {temporaryPassword && <div className="mt-6 rounded-2xl border border-amber-200 bg-amber-50 p-5 text-sm text-amber-900"><div className="flex items-start justify-between gap-4"><div><p className="font-black">Temporary password for {temporaryPassword.email}</p><p className="mt-2 break-all rounded-lg bg-white px-3 py-2 font-mono font-bold">{temporaryPassword.value}</p><p className="mt-2 text-xs">Share it securely and ask the user to change it.</p></div><button onClick={() => setTemporaryPassword(null)} className="font-bold">Dismiss</button></div></div>}
    <div className="mt-8 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
      {accounts === null ? <Loading label="Loading accounts..."/> : error ? <ErrorState text="The account list could not be loaded."/> : accounts.length === 0 ? <Empty title={`No ${type} found`} text="New accounts will appear here."/> : <div className="overflow-x-auto"><table className="w-full min-w-[850px] text-left text-sm"><thead className="border-y border-slate-100 text-xs uppercase tracking-wider text-slate-400"><tr><th className="px-3 py-3">Account</th><th className="px-3 py-3">Details</th><th className="px-3 py-3">Status</th><th className="px-3 py-3 text-right">Actions</th></tr></thead><tbody>{accounts.map((account) => <tr key={account.user_id} className="border-b border-slate-50"><td className="px-3 py-4"><p className="font-bold">{name(account)}</p><p className="text-slate-500">{account.email}</p></td><td className="px-3 py-4 text-slate-500">{type === "candidates" ? `${account.city || "No location"} · ${account.application_count || 0} applications` : `${account.company_name || "No company"} · ${account.location || "No location"}`}</td><td className="px-3 py-4"><span className={`rounded-md px-2.5 py-1 text-[11px] font-bold ${account.active ? "bg-emerald-100 text-emerald-700" : "bg-slate-100 text-slate-600"}`}>{account.active ? "Active" : "Deactivated"}</span></td><td className="px-3 py-4"><div className="flex justify-end gap-2"><button disabled={busy === account.user_id} onClick={() => changeStatus(account)} title={account.active ? "Deactivate account" : "Activate account"} className="rounded-lg border border-slate-200 p-2 text-slate-600 hover:border-blue-200 hover:text-[#3158e8] disabled:opacity-50">{account.active ? <UserX size={16}/> : <UserCheck size={16}/>}</button><button disabled={busy === account.user_id} onClick={() => resetPassword(account)} title="Reset password" className="rounded-lg border border-slate-200 p-2 text-slate-600 hover:border-blue-200 hover:text-[#3158e8] disabled:opacity-50"><KeyRound size={16}/></button><button disabled={busy === account.user_id} onClick={() => deleteAccount(account)} title="Delete account" className="rounded-lg border border-slate-200 p-2 text-red-600 hover:border-red-200 disabled:opacity-50"><Trash2 size={16}/></button></div></td></tr>)}</tbody></table></div>}
    </div>
  </section>;
}
