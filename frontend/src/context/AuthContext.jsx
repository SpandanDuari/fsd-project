import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem("hiretrack_user") || "null"));
  const login = (email, role = "candidate") => {
    const next = { email, role, name: email.split("@")[0] };
    localStorage.setItem("hiretrack_user", JSON.stringify(next));
    setUser(next);
  };
  const logout = () => { localStorage.removeItem("hiretrack_user"); localStorage.removeItem("hiretrack_token"); setUser(null); };
  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
}
export const useAuth = () => useContext(AuthContext);
