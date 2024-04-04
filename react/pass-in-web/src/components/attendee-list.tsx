import {
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
  MoreHorizontal,
  Search
} from "lucide-react"
import { ChangeEvent, useState } from "react"
import { attendees } from '../data/attendees'
import { Attendee } from "../types/entities-types"
import { dateFormat } from "../utils/date-format"
import { IconButton } from "./icon-button"
import { Table } from "./table/table"
import { TableCell } from "./table/table-cell"
import { TableHeader } from "./table/table-header"
import { TableRow } from "./table/table-row"

export function AttendeeList() {
  const [searchInput, setSearchInput] = useState("")
  const [page, setPage] = useState(1)

  const totalPages = Math.ceil(attendees.length / 10)

  function onSearchInputChanged(event: ChangeEvent<HTMLInputElement>){
    setSearchInput(event.target.value)
  }

  function goToNextPage(){
    setPage(page + 1)
  }

  function goToPreviousPage(){
    setPage(page - 1)
  }

  function goToLastPage(){
    setPage(totalPages)
  }

  function goToFirstPage(){
    setPage(1)
  }

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center gap-3">
        <h1 className="text-2xl font-bold">Participantes</h1>

        <div className="px-3 w-72 py-1.5 border border-white/10 rounded-lg text-sm flex items-center gap-3 outline-none">
          <Search className="size-4 text-emerald-300" />
          <input
            className="bg-transparent flex-1 border-transparent"
            placeholder="Buscar participante..."
            onChange={onSearchInputChanged}
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
          {attendees.slice((page - 1) * 10, page * 10).map((attendee : Attendee) => {
            return (
              <TableRow key={attendee.id}>
                <TableCell>
                  <input
                    type="checkbox"
                    className="size-4 bg-black/20 rounded border border-white/10 text-orange-400 custom-checkbox"
                  />
                </TableCell>
                <TableCell>{attendee.id}</TableCell>
                <TableCell>
                  <div className="flex flex-col gap-1">
                    <span className="text-white font-semibold">{attendee.name}</span>
                    <span>{attendee.email}</span>
                  </div>
                </TableCell>
                <TableCell>{dateFormat(attendee.createdAt)}</TableCell>
                <TableCell>{dateFormat(attendee.checkedInAt)}</TableCell>
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
              Mostrando 10 de {attendees.length} itens
            </TableCell>
            <TableCell colSpan={3}>
              <div className="flex items-center justify-end gap-8">
                <span>Pagina {page} de {totalPages}</span>

                <div className="flex gap-1.5">
                  <IconButton onClick={goToFirstPage} disabled={page === 1}>
                    <ChevronsLeft className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToPreviousPage} disabled={page === 1}>
                    <ChevronLeft className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToNextPage} disabled={page === totalPages}>
                    <ChevronRight className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToLastPage} disabled={page === totalPages}>
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
