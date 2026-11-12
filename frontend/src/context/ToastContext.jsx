import { createContext, useContext, useState } from "react";
const ToastContext = createContext(() => {});
export function ToastProvider({ children }) {
  const [message, setMessage] = useState("");
  const notify = (text) => { setMessage(text); window.setTimeout(() => setMessage(""), 3200); };
  return <ToastContext.Provider value={notify}>{children}{message && <div className="fixed bottom-5 right-5 z-50 rounded-xl bg-ink px-5 py-3 text-sm font-semibold text-white shadow-xl" role="status">{message}</div>}</ToastContext.Provider>;
}
export const useToast = () => useContext(ToastContext);
