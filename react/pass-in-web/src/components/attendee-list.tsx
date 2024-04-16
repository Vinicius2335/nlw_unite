import {
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
  MoreHorizontal,
  Search
} from "lucide-react"
import { ChangeEvent, useEffect, useState } from "react"
import { Attendee } from "../types/entities-types"
import { AttendeesPaginationResponse } from "../types/responses-types"
import { dateFormat } from "../utils/date-format"
import { IconButton } from "./icon-button"
import { Table } from "./table/table"
import { TableCell } from "./table/table-cell"
import { TableHeader } from "./table/table-header"
import { TableRow } from "./table/table-row"

// URL State -> armazenar no endereço da url da aplicação, todo aquale estado que vem de um input do usuário
// e que queremos persistir quando recarregar a página ou enviar o link para outra pessoa

export function AttendeeList() {
  const [searchInput, setSearchInput] = useState(() => {
    const url = new URL(window.location.toString())

    if(url.searchParams.has('search')){
      return url.searchParams.get('search') ?? ""
    }

    return ""
  })

  const [page, setPage] = useState(() => {
    const url = new URL(window.location.toString())

    if(url.searchParams.has('page')){
      return Number(url.searchParams.get('page')) - 1
    }

    return 0
  })

  const [response, setResponse] = useState<AttendeesPaginationResponse>(
    {} as AttendeesPaginationResponse
  )
  const [attendees, setAttendees] = useState<Attendee[]>([])

  const totalPages = response.totalPages
  const totalItens = response.totalItens
  const currentPage = response.currentPage
  const currentItens = response.currentItens

  function onSearchInputChanged(event: ChangeEvent<HTMLInputElement>) {
    setCurrentSearch(event.target.value)
    setPage(0)
  }

  function goToNextPage() {
    setCurrentPage(page + 1)
  }

  function goToPreviousPage() {
    setCurrentPage(page - 1)
  }

  function goToLastPage() {
    setCurrentPage(totalPages - 1)
  }

  function goToFirstPage() {
    setCurrentPage(0)
  }

  function setCurrentPage(page: number){
    // recarrega a pagina inteira ao mudar a url do navegador
    // const searchParams = new URLSearchParams(window.location.search)
    // searchParams.set("page", "1")
    // window.location.search = searchParams.toString()

    // somente muda a url do navegador, não recarrega a pagina inteira
    const url = new URL(window.location.toString())
    url.searchParams.set("page", String(page + 1))
    window.history.pushState({}, "", url)
    setPage(page)
  }

  function setCurrentSearch(search : string){
    const url = new URL(window.location.toString())
    url.searchParams.set("search", search)
    window.history.pushState({}, "", url)
    setSearchInput(search)
  }

  useEffect(() => {
    const url = new URL("http://localhost:8080/events/058423d2-6a2f-4cf5-bf5d-d3dc040c23b4/attendees")
    url.searchParams.set("name", searchInput)
    url.searchParams.set("page", String(page))

    fetch(url)
      .then(response => response.json())
      .then((data: AttendeesPaginationResponse) => {
        setAttendees(data.attendees)
        setResponse(data)
      })
  }, [page, searchInput])

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center gap-3">
        <h1 className="text-2xl font-bold">Participantes</h1>

        <div className="px-3 w-72 py-1.5 border border-white/10 rounded-lg text-sm flex items-center gap-3 outline-none">
          <Search className="size-4 text-emerald-300" />
          <input
            className="bg-transparent flex-1 border-transparent focus:ring-0"
            placeholder="Buscar participante..."
            onChange={onSearchInputChanged}
            value={searchInput}
          />
        </div>
      </div>

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
          {attendees.map((attendee: Attendee) => {
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
                <TableCell>
                  {attendee.checkInAt === null ? (
                    <span className="text-zinc-500">Não fez check-in</span>
                  ) : (
                    dateFormat(attendee.checkInAt)
                  )}
                </TableCell>
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
              Mostrando {currentItens} de {totalItens} itens
            </TableCell>
            <TableCell colSpan={3}>
              <div className="flex items-center justify-end gap-8">
                <span>
                  Pagina {totalPages === 0 ? 0 : currentPage + 1} de {totalPages}
                </span>

                <div className="flex gap-1.5">
                  <IconButton onClick={goToFirstPage} disabled={page === 0}>
                    <ChevronsLeft className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToPreviousPage} disabled={page === 0}>
                    <ChevronLeft className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToNextPage} disabled={page === totalPages - 1}>
                    <ChevronRight className="size-4" />
                  </IconButton>
                  <IconButton onClick={goToLastPage} disabled={page === totalPages - 1}>
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
