"use client";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();

  const handleLogout = () => {
    document.cookie = "token=; path=/; max-age=0";
    router.push("/");
  };

  // FUNÇÃO MÁGICA: Verifica se a URL atual combina com o botão do menu
  const getMenuClass = (caminho: string) => {
    if (caminho === "/dashboard" && pathname !== "/dashboard") {
      return "block px-4 py-3 rounded-lg text-zinc-400 hover:bg-zinc-800 hover:text-zinc-100 transition-colors border border-transparent";
    }
    
    if (pathname.startsWith(caminho)) {
      return "block px-4 py-3 rounded-lg bg-emerald-500/10 text-emerald-500 font-medium transition-colors border border-emerald-500/20";
    }
    
    return "block px-4 py-3 rounded-lg text-zinc-400 hover:bg-zinc-800 hover:text-zinc-100 transition-colors border border-transparent";
  };

  useEffect(() => {
    if (pathname === "/" || pathname.startsWith("/login")) {
      return;
    }

    const tokenRaw = document.cookie.split("; ").find(row => row.trim().startsWith("token="));
    const token = tokenRaw ? tokenRaw.split("=")[1]?.trim() : null;

    if (!token) {
      router.replace("/");
      return;
    }

    try {
      const payloadBase64 = token.split(".")[1];
      if (!payloadBase64) throw new Error("JWT inválido");

      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);

      const userRole = 
        payload.role || 
        payload.roles?.[0] || 
        payload.authorities?.[0]?.authority || 
        "UNKNOWN";

      // Bloqueio para aluno (mantido, mas com proteção contra loop)
      if (userRole === "ALUNO" || userRole === "ROLE_ALUNO") {
        alert("A visão do aluno ainda não foi implementada nesta versão do sistema.");
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        router.replace("/");
      }
    } catch (error) {
      console.error("Erro ao decodificar JWT:", error);
      // Limpa e redireciona apenas se ainda não estiver na raiz
      if (pathname !== "/") {
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        router.replace("/");
      }
    }
  }, [pathname, router]);

  return (
    <div className="min-h-screen bg-zinc-950 flex">
      {/* Menu Lateral (Sidebar) */}
      <aside className="w-64 bg-zinc-900 border-r border-zinc-800 flex flex-col fixed h-full z-10">
        {/* Logo */}
        <div className="p-6 border-b border-zinc-800">
          <h1 className="text-xl font-bold text-emerald-500 tracking-wider">
            Personal System
          </h1>
        </div>

        {/* Links de Navegação */}
        <nav className="flex-1 p-4 space-y-2">
          <Link href="/dashboard" className={getMenuClass("/dashboard")}>
            🏠 Início
          </Link>
          <Link href="/dashboard/alunos" className={getMenuClass("/dashboard/alunos")}>
            👥 Meus Alunos
          </Link>
          <Link href="/dashboard/exercicios" className={getMenuClass("/dashboard/exercicios")}>
            💪 Exercícios
          </Link>
          <Link href="/dashboard/treinos" className={getMenuClass("/dashboard/treinos")}>
            🏋️‍♂️ Planos de Treino
          </Link>
          <Link href="/dashboard/faturas" className={getMenuClass("/dashboard/faturas")}>
            💳 Faturas
          </Link>
          <Link href="/dashboard/avaliacoes" className={getMenuClass("/dashboard/avaliacoes")}>
            📏 Avaliações Físicas
          </Link>
        </nav>

        {/* Rodapé do Menu */}
        <div className="p-4 border-t border-zinc-800">
          <button
            onClick={handleLogout}
            className="flex items-center gap-3 text-red-400 hover:text-red-300 transition-colors w-full px-4 py-2 rounded-lg hover:bg-red-400/10"
          >
            <span className="w-8 h-8 rounded-full bg-zinc-800 flex items-center justify-center text-zinc-100 text-sm font-bold border border-zinc-700">
              N
            </span>
            Sair do Sistema
          </button>
        </div>
      </aside>

      {/* Conteúdo Principal */}
      <main className="flex-1 ml-64 p-8">
        <div className="max-w-7xl mx-auto">
          {children}
        </div>
      </main>
    </div>
  );
}