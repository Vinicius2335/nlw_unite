import {
  Search,
  MoreHorizontal,
  ChevronsLeft,
  ChevronLeft,
  ChevronRight,
  ChevronsRight
} from "lucide-react"

export function AttendeeList() {
  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center gap-3">
        <h1 className="text-2xl font-bold">Participantes</h1>

        <div className="px-3 w-72 py-1.5 border border-white/10 rounded-lg text-sm flex items-center gap-3 outline-none">
          <Search className="size-4 text-emerald-300" />
          <input
            className="bg-transparent flex-1 border-transparent"
            placeholder="Buscar participante..."
          />
        </div>
      </div>

      {/* não é possivel criar arredondamento em tabelas, precisamos criar uma div por volta da tabela */}
      <div className="border border-white/10 rounded-lg">
        <table className="w-full">
          <thead>
            <tr style={{ width: 48 }} className="border-b border-white/10">
              <th className="py-3 px-4 text-sm font-semibold text-left">
                <input
                  type="checkbox"
                  className="size-4 bg-black/20 rounded border border-white/10 text-orange-400 custom-checkbox"
                />
              </th>
              <th className="py-3 px-4 text-sm font-semibold text-left">Código</th>
              <th className="py-3 px-4 text-sm font-semibold text-left">Participante</th>
              <th className="py-3 px-4 text-sm font-semibold text-left">Data de inscrição</th>
              <th className="py-3 px-4 text-sm font-semibold text-left">Data de check-in</th>
              <th style={{ width: 64 }} className="py-3 px-4 text-sm font-semibold text-left"></th>
            </tr>
          </thead>

          <tbody>
            {Array.from({ length: 10 }).map((_, i) => {
              return (
                <tr className="border-b border-white/10 hover:bg-white/5" key={i}>
                  <td className="py-3 px-4 text-sm text-zinc-300">
                    <input
                      type="checkbox"
                      className="size-4 bg-black/20 rounded border border-white/10 text-orange-400 custom-checkbox"
                    />
                  </td>
                  <td className="py-3 px-4 text-sm text-zinc-300">ajsdkasdlasjdl</td>
                  <td className="py-3 px-4 text-sm text-zinc-300">
                    <div className="flex flex-col gap-1">
                      <span className="text-white font-semibold">Viniciusu Vieira</span>
                      <span>vinicius@email.com</span>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-sm text-zinc-300">03/04/2023</td>
                  <td className="py-3 px-4 text-sm text-zinc-300">03/04/2023</td>
                  <td className="py-3 px-4 text-sm text-zinc-300">
                    <button className="bg-black/20 border border-white/10 rounded-md p-1.5">
                      <MoreHorizontal className="size-4" />
                    </button>
                  </td>
                </tr>
              )
            })}
          </tbody>

          <tfoot>
            <tr>
              <td colSpan={3} className="py-3 px-4 text-sm text-zinc-300">
                Mostrando 10 de 228 itens
              </td>
              <td colSpan={3} className="py-3 px-4 text-sm text-zinc-300 text-right">
                <div className="flex items-center justify-end gap-8">
                  <span>Pagina 1 de 23</span>

                  <div className="flex gap-1.5">
                    <button className="bg-white/10 border border-white/10 rounded-md p-1.5">
                      <ChevronsLeft className="size-4" />
                    </button>
                    <button className="bg-white/10 border border-white/10 rounded-md p-1.5">
                      <ChevronLeft className="size-4" />
                    </button>
                    <button className="bg-white/10 border border-white/10 rounded-md p-1.5">
                      <ChevronRight className="size-4" />
                    </button>
                    <button className="bg-white/10 border border-white/10 rounded-md p-1.5">
                      <ChevronsRight className="size-4" />
                    </button>
                  </div>
                </div>
              </td>
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
  )
}
