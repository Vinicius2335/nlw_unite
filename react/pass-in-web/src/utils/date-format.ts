import dayjs from "dayjs"
import relativeTime from "dayjs/plugin/relativeTime"
import ptBR from "dayjs/locale/pt-br"

dayjs.locale(ptBR)
dayjs.extend(relativeTime)

export function dateFormat(date: Date) {
  return dayjs().to(date)
}
