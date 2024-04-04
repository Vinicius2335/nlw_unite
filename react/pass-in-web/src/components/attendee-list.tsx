import {
  Search,
  MoreHorizontal,
  ChevronsLeft,
  ChevronLeft,
  ChevronRight,
  ChevronsRight
} from "lucide-react"
import { IconButton } from "./icon-button"
import { Table } from "./table/table"
import { TableHeader } from "./table/table-header"
import { TableCell } from "./table/table-cell"
import { TableRow } from "./table/table-row"

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
      <Table>
        <thead>
          <tr style={{ width: 48 }} className="border-b border-white/10">
            <TableHeader>
              <input
                type="checkbox"
                className="size-4 bg-black/20 rounded border border-white/10 text-orange-400 custom-checkbox"
              />
            </TableHeader>
            <TableHeader>Código</TableHeader>
            <TableHeader>Participante</TableHeader>
            <TableHeader>Data de inscrição</TableHeader>
            <TableHeader>Data de check-in</TableHeader>
            <TableHeader style={{ width: 64 }}></TableHeader>
          </tr>
        </thead>

        <tbody>
          {Array.from({ length: 10 }).map((_, i) => {
            return (
              <TableRow key={i}>
                <TableCell>
                  <input
                    type="checkbox"
                    className="size-4 bg-black/20 rounded border border-white/10 text-orange-400 custom-checkbox"
                  />
                </TableCell>
                <TableCell>ajsdkasdlasjdl</TableCell>
                <TableCell>
                  <div className="flex flex-col gap-1">
                    <span className="text-white font-semibold">Viniciusu Vieira</span>
                    <span>vinicius@email.com</span>
                  </div>
                </TableCell>
                <TableCell>03/04/2023</TableCell>
                <TableCell>03/04/2023</TableCell>
                <TableCell>
                  <IconButton transparent>
                    <MoreHorizontal className="size-4" />
                  </IconButton>
                </TableCell>
              </TableRow>
            )
          })}
        </tbody>

        <tfoot>
          <tr>
            <TableCell colSpan={3}>
              Mostrando 10 de 228 itens
            </TableCell>
            <TableCell colSpan={3}>
              <div className="flex items-center justify-end gap-8">
                <span>Pagina 1 de 23</span>

                <div className="flex gap-1.5">
                  <IconButton>
                    <ChevronsLeft className="size-4" />
                  </IconButton>
                  <IconButton>
                    <ChevronLeft className="size-4" />
                  </IconButton>
                  <IconButton>
                    <ChevronRight className="size-4" />
                  </IconButton>
                  <IconButton>
                    <ChevronsRight className="size-4" />
                  </IconButton>
                </div>
              </div>
            </TableCell>
          </tr>
        </tfoot>
      </Table>
    </div>
  )
}
