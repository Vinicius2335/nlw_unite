import { Attendee } from "./entities-types"

export interface AttendeesPaginationResponse {
  currentItens: number
  totalItens: number
  attendees: Attendee[]
  totalPages: number
  currentPage: number
}
